package com.lomoye.task.manager;

import com.lomoye.common.manager.DomainManager;
import com.lomoye.task.domain.BatchTask;


public interface BatchTaskManager<B extends BatchTask> extends DomainManager<Long, B> {
    void updateAtomProcessNum(Long batchTaskId, long total, long unchanged, long success, long failed);
}
