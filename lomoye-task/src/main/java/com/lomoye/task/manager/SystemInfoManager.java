package com.lomoye.task.manager;

import com.lomoye.common.manager.DomainManager;
import com.lomoye.task.domain.SystemInfo;


public interface SystemInfoManager extends DomainManager<Long, SystemInfo> {

    SystemInfo generateSystemInfo(String instance, String desc);
}
