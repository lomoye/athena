package com.lomoye.task.manager.impl;

import com.lomoye.common.dao.BasicMapper;
import com.lomoye.common.dao.OrderCondition;
import com.lomoye.common.manager.AbstractManager;
import com.lomoye.task.dao.TaskStatMapper;
import com.lomoye.task.domain.TaskStat;
import com.lomoye.task.manager.TaskStatManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class TaskStatManagerImpl extends AbstractManager<TaskStat> implements TaskStatManager {
    @Autowired
    private TaskStatMapper mapper;

    @Override
    protected BasicMapper<Long, TaskStat> getMapper() {
        return mapper;
    }

    @Override
    public TaskStat getTaskStatByType(Long shopId, String type) {
        TaskStat stat = new TaskStat();
        stat.setShopId(shopId);
        stat.setType(type);
        TaskStat dbStat = mapper.selectOne(stat);
        if (dbStat == null) {
            stat.setFailedNum(0L);
            stat.setSuccessNum(0L);
            stat.setAbnormalNum(0L);
            stat.setNotInformedNum(0L);
            stat.setTaskNum(0L);
            mapper.insert(stat);
            return stat;
        }
        return dbStat;
    }

    @Override
    public List<TaskStat> getTaskStatByTypes(Long shopId, List<String> types) {
        return mapper.selectTaskStatByTypes(shopId, types);
    }

    @Override
    public List<TaskStat> getTaskStatByShop(Long shopId) {
        TaskStat stat = new TaskStat();
        stat.setShopId(shopId);
        List<OrderCondition> conditions = new ArrayList<>();
        conditions.add(new OrderCondition("id", "desc"));

        List<TaskStat> stats = mapper.selectByCondition(stat, conditions);
        return nonEmptyList(stats);
    }
}
