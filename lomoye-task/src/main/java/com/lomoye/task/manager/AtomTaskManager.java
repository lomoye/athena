package com.lomoye.task.manager;

import com.lomoye.common.manager.DomainManager;
import com.lomoye.task.domain.AtomTask;

public interface AtomTaskManager<A extends AtomTask> extends DomainManager<Long, A> {

}
