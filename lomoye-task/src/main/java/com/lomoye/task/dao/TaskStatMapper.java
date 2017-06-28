package com.lomoye.task.dao;

import com.lomoye.common.dao.PagedMapper;
import com.lomoye.task.domain.TaskStat;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaskStatMapper extends PagedMapper<Long, TaskStat> {
    List<TaskStat> selectTaskStatByTypes(@Param("shopId") Long shopId, @Param("types") List<String> types);
}
