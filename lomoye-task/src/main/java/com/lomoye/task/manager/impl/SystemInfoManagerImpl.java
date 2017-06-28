package com.lomoye.task.manager.impl;

import com.lomoye.common.dao.BasicMapper;
import com.lomoye.common.manager.AbstractManager;
import com.lomoye.common.util.NetUtils;
import com.lomoye.task.dao.SystemInfoMapper;
import com.lomoye.task.domain.SystemInfo;
import com.lomoye.task.manager.SystemInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SystemInfoManagerImpl extends AbstractManager<SystemInfo> implements SystemInfoManager {
    @Autowired
    private SystemInfoMapper mapper;

    @Override
    protected BasicMapper<Long, SystemInfo> getMapper() {
        return mapper;
    }

    @Override
    public SystemInfo generateSystemInfo(String instance, String desc) {
        String ip = NetUtils.getHostIp();
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setInstance(instance);
        systemInfo.setIp(ip);
        systemInfo.setDesc(desc);
        save(systemInfo);
        return systemInfo;
    }
}
