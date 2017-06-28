package com.lomoye.common.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 支持任务处理的消息队列. 不支持Null. 后续需要加入队列长度的限制?
 */
public abstract class TaskQueue<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskQueue.class);

    private static final Long DEFAULT_TAKE_ONCE_TIMEOUT = 20L * 60 * 1000;  //默认20分钟打一条日志

    private static final Long DEFAULT_PUT_ONCE_TIMEOUT = 20L * 60 * 1000;  //默认20分钟打一条日志

    //锁相关的一些变量
    private final Lock lock = new ReentrantLock();

    private final Condition notEmpty = lock.newCondition();

    //队列相关变量
    private LinkedList<T> waitQueue = new LinkedList<>();  //待处理的任务列表

    private LinkedList<T> readyQueue = new LinkedList<>();  //可以处理的任务列表

    private LinkedList<T> processQueue = new LinkedList<>(); //正在处理的任务

    private TaskQueueStat taskQueueStat = new TaskQueueStat(); //Queue的统计信息维护

    //从ReadyQueue取出任务,并且放到processQueue
    public T take(boolean needTimeOut) {
        while (true) {
            try {
                T task = takeOnce();
                if (needTimeOut || task != null) {
                    return task;
                }
            } catch (Exception e) {
                LOGGER.warn(getQueueName() + " takeOnce exception", e);
            }
        }
    }

    //将任务插入到TaskQueue
    public boolean put(T task, boolean needTimeOut) {
        if (task == null) {
            LOGGER.warn(getQueueName() + " task should not be null");
            return false;
        }
        while (true) {
            try {
                boolean res = putOnce(task);
                if (needTimeOut || res) {
                    return res;
                }
            } catch (ExistTaskException e) {
                throw e;
            } catch (Exception e) {
                LOGGER.warn(getQueueName() + " putOnce exception", e);
            }
        }
    }

    //任务处理完成,必须保证从processQueue删除:!!important.推荐写到finally方法里面
    public boolean finish(T task, long time) {
        if (task == null) {
            LOGGER.info("null task exists");
            return true;
        }
        try {
            lock.lock(); //必须保证清理成功
            processQueue.remove(task);
            updateQueueStat(time); //更新下统计信息
            if (!supportMultiplePerClass(task)) {
                return true;  //不支持同类型有多个的直接返回,wait队列里不会有相同的
            }
            for (Iterator<T> iter = waitQueue.iterator(); iter.hasNext(); ) {
                T wait = iter.next();
                if (isSameClass(wait, task)) {
                    iter.remove();
                    readyQueue.addLast(wait);
                    notEmpty.signal();
                    break;    //同一类型的同一时刻只能有一个放到就绪队列里面
                }
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    private T takeOnce() throws InterruptedException {
        long start = System.currentTimeMillis();
        if (lock.tryLock(getTakeOnceTimeOut(), TimeUnit.MILLISECONDS)) {
            try {
                T value = doTake(getTakeOnceTimeOut() - (System.currentTimeMillis() - start));
                if (value == null) {
                    LOGGER.info(getQueueName() + " doTakeOnce do take timeout");
                }
                return value;
            } finally {
                lock.unlock();
            }
        } else {
            LOGGER.warn(getQueueName() + " doTakeOnce get lock timeout");
            return null;
        }
    }

    private T doTake(long timeOut) throws InterruptedException {
        long nanos = TimeUnit.MILLISECONDS.toNanos(timeOut);  //换成纳秒
        while (readyQueue.isEmpty()) {
            if (nanos <= 0L) {
                return null;
            }
            nanos = notEmpty.awaitNanos(nanos);
        }
        T task = readyQueue.remove(0);  //从就绪队列取
        processQueue.add(task);  //放到处理的队列
        return task;
    }

    private boolean putOnce(T task) throws InterruptedException {
        if (lock.tryLock(getPutOnceTimeOut(), TimeUnit.MILLISECONDS)) {
            try {
                if (waitQueue.size() > 50000 || readyQueue.size() > 50000) {
                    LOGGER.warn(getQueueName() + " wait_size=" + waitQueue.size() + "|ready_size=" + readyQueue.size());
                }
                if (supportMultiplePerClass(task)) {
                    addToQueueForMultiple(task); //支持多个同类任务的
                    return true;
                }
                //以下处理不支持多个同类型任务的.必须之前不存在
                for (T t : waitQueue) {
                    if (isSameClass(t, task)) {
                        throw new ExistTaskException("task exist");
                    }    //不支持同类任务,同时同类任务已经存在了,直接返回
                }
                for (T t : readyQueue) {
                    if (isSameClass(t, task)) {
                        throw new ExistTaskException("task exist");
                    }    //不支持同类任务,同时同类任务已经存在了,直接返回
                }
                for (T t : processQueue) {
                    if (isSameClass(t, task)) {
                        throw new ExistTaskException("task exist");
                    }   //不支持同类任务,同时同类任务已经存在了,直接返回
                }
                readyQueue.addLast(task);
                notEmpty.signal();
                return true;
            } finally {
                lock.unlock();
            }
        } else {
            LOGGER.warn(getQueueName() + " doTakeOnce get lock timeout");
            return false;
        }
    }


    private boolean addToQueueForMultiple(T task) {
        boolean hasReady = false;
        for (T t : readyQueue) {
            if (isSameClass(t, task)) {
                hasReady = true;
                break;
            }
        }
        if (hasReady) {
            waitQueue.addLast(task);
            return false;
        }
        for (T t : processQueue) {
            if (isSameClass(t, task)) {
                hasReady = true;
            }
        }
        if (hasReady) {
            waitQueue.addLast(task);
            return false;
        } else {
            readyQueue.addLast(task); //同类型没有就绪的
            notEmpty.signal();
            return true;
        }
    }

    private void updateQueueStat(long time) {
        taskQueueStat.setFinished(taskQueueStat.getFinished() + 1);
        taskQueueStat.setTotalTime(taskQueueStat.getTotalTime() + time);
        taskQueueStat.setAvgTime(taskQueueStat.getTotalTime() / taskQueueStat.getFinished());
        if (taskQueueStat.getMaxTime() < time) {
            taskQueueStat.setMaxTime(time);
        }
        if (taskQueueStat.getMinTime() > time) {
            taskQueueStat.setMinTime(time);
        }
    }

    public TaskQueueStat getQueueStat() {
        try {
            lock.lock(); //必须保证锁成功
            TaskQueueStat old = taskQueueStat;
            old.setWait(waitQueue.size());
            old.setReady(readyQueue.size());
            old.setProcess(processQueue.size());

            this.taskQueueStat = new TaskQueueStat();
            taskQueueStat.setMinTime(old.getMinTime());
            taskQueueStat.setMaxTime(old.getMaxTime());
            taskQueueStat.setThreadNum(old.getThreadNum());

            return old;
        } finally {
            lock.unlock();
        }
    }

    public void setThreadNum(long threadNum) {
        try {
            lock.lock(); //必须保证锁成功
            taskQueueStat.setThreadNum(threadNum);
        } finally {
            lock.unlock();
        }
    }

    public abstract String getQueueName();

    protected boolean isSameClass(T t1, T t2) {
        return false; //默认所有的都不相同
    }  //同一类型的并且supportMultiple=false时，相同的任务会直接被抛弃

    protected boolean supportMultiplePerClass(T t) {
        return false;   //同一类型是否支持多个任务排队,不能同时多线程跑,但是能排队
    }

    protected long getTakeOnceTimeOut() {
        return DEFAULT_TAKE_ONCE_TIMEOUT;  //可以重载
    }

    protected long getPutOnceTimeOut() {
        return DEFAULT_PUT_ONCE_TIMEOUT;  //可以重载
    }
}
