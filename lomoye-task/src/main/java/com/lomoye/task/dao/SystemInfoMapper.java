package com.lomoye.task.dao;

import com.lomoye.common.dao.PagedMapper;
import com.lomoye.task.domain.SystemInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemInfoMapper extends PagedMapper<Long, SystemInfo> {
}
