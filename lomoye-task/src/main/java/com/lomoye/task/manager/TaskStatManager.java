package com.lomoye.task.manager;

import com.lomoye.common.manager.DomainManager;
import com.lomoye.task.domain.TaskStat;

import java.util.List;


public interface TaskStatManager extends DomainManager<Long, TaskStat> {

    TaskStat getTaskStatByType(Long shopId, String type);

    List<TaskStat> getTaskStatByTypes(Long shopId, List<String> types);

    List<TaskStat> getTaskStatByShop(Long shopId);
}
