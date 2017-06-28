package com.lomoye.task;

import com.lomoye.common.concurrent.ThreadUtil;
import com.lomoye.task.domain.AtomTask;
import com.lomoye.task.domain.BatchTask;
import com.lomoye.task.domain.Snapshot;
import com.lomoye.task.domain.TaskStat;
import com.lomoye.task.enums.AtomTaskResult;
import com.lomoye.task.enums.AtomTaskStatus;
import com.lomoye.task.enums.TaskAction;
import com.lomoye.task.enums.TaskInformedType;
import com.lomoye.task.manager.AtomTaskManager;
import com.lomoye.task.manager.BatchTaskManager;
import com.lomoye.task.manager.SnapshotManager;
import com.lomoye.task.manager.TaskStatManager;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 批量任务执行器的基础逻辑
 * Created by tommy on 2015/8/29.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class BatchTaskExecutor<B extends BatchTask, A extends AtomTask> extends Thread {
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private SnapshotManager snapshotManager;
    @Autowired
    private TaskStatManager taskStatManager;

    protected B batchTask;
    protected LinkedList<A> availableAtomTasks;
    private List<Thread> threads = new ArrayList<>();

    private long totalCount = 0;
    private long unchangedCount = 0;
    private long successCount = 0;
    private long failedCount = 0;
    private long times = 0;
    private long maxUpdateCached = 0;


    public BatchTaskExecutor() {

    }

    public void init(B batchTask, List<A> atomTasks) {
        this.batchTask = batchTask;
        this.availableAtomTasks = new LinkedList<>(atomTasks);

        this.setName(batchTaskName() + "-executor-thread");
        this.maxUpdateCached = getMaxUpdatesCached();
    }

    @Override
    public void run() {
        if (availableAtomTasks.isEmpty()) {
            LOGGER.info(batchTaskName() + "|" + batchTask.getId() + "| with none changes");
            updateTaskAndObject();
            return;
        }
        try {
            doAtomTask();
        } catch (Throwable e) {
            LOGGER.warn("batchExecutor Exception happen", e);
            onRollback(batchTask);
        }
    }

    /**
     * 子类可以继承该方法，做一些初始化的工作的
     */
    protected void doAtomTask() {
        int poolSize = Math.min(5, availableAtomTasks.size());
        for (int i = 0; i < poolSize; i++) {
            Thread thread = new Thread(new AtomTaskProcessor());
            thread.setName(batchTask.getShopId() + "-" + batchTask.getId() + "-" + atomTaskName() + "-" + i);
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            ThreadUtil.safeJoin(thread, batchTaskName() + "Executor join");
        }
        updateTaskAndObject();
    }

    private void updateTaskAndObject() {
        //最后统一处理, 保证一致性?
        B dbBatchTask = batchTaskManager().getById(batchTask.getId());
        dbBatchTask.setTotalNum(dbBatchTask.getTotalNum() + totalCount);
        dbBatchTask.setUnchangedNum(dbBatchTask.getUnchangedNum() + unchangedCount);
        dbBatchTask.setSuccessNum(dbBatchTask.getSuccessNum() + successCount);
        dbBatchTask.setFailedNum(dbBatchTask.getFailedNum() + failedCount);

        TaskStat stat = taskStatManager.getTaskStatByType(dbBatchTask.getShopId(), batchTaskName());
        Long changed = dbBatchTask.getTotalNum() - dbBatchTask.getUnchangedNum();
        if (!changed.equals(dbBatchTask.getSuccessNum() + dbBatchTask.getFailedNum())) {
            //数字不对,原因是什么呢？
            stat.setAbnormalNum(stat.getAbnormalNum() + 1);
            stat.setNotInformedNum(stat.getNotInformedNum() + 1);
            dbBatchTask.setInformed(TaskInformedType.NEED_INFORM);
            onBatchTaskAbnormal(dbBatchTask, stat);
        } else if (changed.equals(dbBatchTask.getSuccessNum())) {
            //全部成功
            stat.setSuccessNum(stat.getSuccessNum() + 1);
            dbBatchTask.setInformed(TaskInformedType.NO_INFORM);
            onBatchTaskSuccess(dbBatchTask, stat);
        } else if (changed.equals(dbBatchTask.getFailedNum())) {
            //全部失败
            stat.setFailedNum(stat.getFailedNum() + 1);
            stat.setNotInformedNum(stat.getNotInformedNum() + 1);
            dbBatchTask.setInformed(TaskInformedType.NEED_INFORM);
            onBatchTaskFailed(dbBatchTask, stat);
        } else {
            //有成功有失败的,认为还是成功的,依赖于对应的业务需求
            stat.setNotInformedNum(stat.getNotInformedNum() + 1);
            dbBatchTask.setInformed(TaskInformedType.NEED_INFORM);
            stat.setSuccessNum(stat.getSuccessNum() + 1);
            onBatchTaskPartial(dbBatchTask, stat);
        }
        stat.setTaskNum(stat.getTaskNum() + 1);
        taskStatManager.update(stat);

        Snapshot snap = new Snapshot();
        snap.setShopId(batchTask.getShopId());
        snap.setContent(getBatchObjectSnap(batchTask));
        snap.setType(batchTaskName());
        if (!Strings.isNullOrEmpty(snap.getContent())) {
            snapshotManager.save(snap);
            dbBatchTask.setAfterSnapId(snap.getId());
        }
        batchTaskManager().update(dbBatchTask);
    }

    private void processAtomTask(A atomTask) {
        atomTask.setStatus(AtomTaskStatus.STARTED);
        atomTaskManager().update(atomTask);
        int result;
        try {
            switch (atomTask.getAction()) {
                case TaskAction.CREATE:
                    result = processAtomTaskAdd(atomTask);
                    break;
                case TaskAction.STOP:
                    result = processAtomTaskStop(atomTask);
                    break;
                case TaskAction.UPDATE:
                    result = processAtomTaskUpdate(atomTask);
                    break;
                default:
                    LOGGER.error(atomTaskName() + " action error |shopId={}|atomTaskId={}|action={}", atomTask.getShopId(), atomTask.getId(), atomTask.getAction());
                    result = AtomTaskResult.FAILED;
            }
        } catch (Throwable e) {
            LOGGER.warn("processAtomTask exception happen|shopId={}|atomTaskId={}", atomTask.getShopId(), atomTask.getId(), e);
            result = AtomTaskResult.FAILED;
        }
        if (result == AtomTaskResult.SUCCESS) {
            updateItemProcessNum(atomTask.getBatchTaskId(), 0, 0, 1, 0);
            atomTask.setStatus(AtomTaskStatus.SUCCESS);
        } else if (result == AtomTaskResult.FAILED) {
            updateItemProcessNum(atomTask.getBatchTaskId(), 0, 0, 0, 1);
            atomTask.setStatus(AtomTaskStatus.FAILED);
        } else if (result == AtomTaskResult.UNCHANGED) {
            updateItemProcessNum(atomTask.getBatchTaskId(), 0, 1, 0, 0);
            atomTask.setStatus(AtomTaskStatus.UNCHANGED);
        } else {
            LOGGER.error(atomTaskName() + " unknown process result |shopId={}|atomTaskId={}|result={} ", atomTask.getShopId(), atomTask.getId(), result);
            updateItemProcessNum(atomTask.getBatchTaskId(), 0, 0, 0, 1);
            atomTask.setStatus(AtomTaskStatus.FAILED);
        }
        atomTaskManager().update(atomTask);
    }

    private synchronized A getAtomTask() {
        if (!availableAtomTasks.isEmpty()) {
            return availableAtomTasks.removeFirst();
        } else {
            return null;
        }
    }

    private synchronized void updateItemProcessNum(Long batchTaskId, long total, long unchanged, long success, long failed) {
        totalCount += total;
        unchangedCount += unchanged;
        successCount += success;
        failedCount += failed;
        times++;
        if (times < maxUpdateCached) {
            return;
        }
        batchTaskManager().updateAtomProcessNum(batchTaskId, totalCount, unchangedCount, successCount, failedCount);
        times = 0;
        totalCount = 0;
        unchangedCount = 0;
        successCount = 0;
        failedCount = 0;
    }

    class AtomTaskProcessor implements Runnable {
        @Override
        public void run() {
            while (true) {
                A atomTask = getAtomTask();
                if (atomTask == null) {
                    return;
                }
                try {
                    processAtomTask(atomTask);
                } catch (Throwable e) {
                    LOGGER.warn("Thread exception ", e);
                    atomTask.setStatus(AtomTaskStatus.ABNORMAL);
                    atomTask.setErrorMessage(e.getMessage());
                    atomTaskManager().update(atomTask);
                    updateItemProcessNum(atomTask.getBatchTaskId(), 0, 0, 0, 1);
                }
            }
        }
    }

    abstract protected String batchTaskName();

    abstract protected String atomTaskName();

    abstract protected BatchTaskManager<B> batchTaskManager();

    abstract protected AtomTaskManager<A> atomTaskManager();

    abstract protected long getMaxUpdatesCached();

    abstract protected String getBatchObjectSnap(B batchTask);


    abstract protected void onBatchTaskAbnormal(B batchTask, TaskStat stat);

    abstract protected void onBatchTaskSuccess(B batchTask, TaskStat stat);

    abstract protected void onBatchTaskFailed(B batchTask, TaskStat stat);

    abstract protected void onBatchTaskPartial(B batchTask, TaskStat stat);

    abstract protected long onRollback(B batchTask);


    abstract protected int processAtomTaskAdd(A atomTask);  //1 处理成功,  0 无需修改, -1处理失败

    abstract protected int processAtomTaskStop(A atomTask);  //1 处理成功,  0 无需修改, -1处理失败

    abstract protected int processAtomTaskUpdate(A atomTask);  //1 处理成功,  0 无需修改, -1处理失败
}
