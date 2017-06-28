package com.lomoye.task;

import com.lomoye.common.concurrent.ThreadUtil;
import com.lomoye.common.web.SpringContextHolder;
import com.lomoye.task.domain.SystemInfo;
import com.lomoye.task.manager.SystemInfoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 获取SystemInfo的工具类
 * Created by tommy on 2016/12/17.
 */
public class SystemInfoUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemInfoUtil.class);

    private static SystemInfo systemInfo = null;

    public synchronized static SystemInfo initSystemInfo(String instance, String desc) {
        if (systemInfo != null) {
            return systemInfo;
        }
        SystemInfoManager systemInfoManager = (SystemInfoManager) SpringContextHolder.getBean("systemInfoManagerImpl");
        if (systemInfoManager == null) {
            throw new RuntimeException("SystemInfoManager is null in initialization");
        }

        systemInfo = systemInfoManager.generateSystemInfo(instance, desc);
        return systemInfo;
    }

    public static SystemInfo getSystemInfo() {
        for (int i = 0; i < 20; i++) {
            if (systemInfo != null) {
                return systemInfo;
            } else {
                ThreadUtil.safeSleep(15, TimeUnit.SECONDS, "wait getSystemInfo sleep");
            }
        }
        LOGGER.error("System Info is empty");
        System.exit(-1);  //5分钟没出来就退出
        return systemInfo;
    }

    public static String getInstance() {
        SystemInfo systemInfo = getSystemInfo();
        return systemInfo.getInstance();
    }

    public static Long getSystemInfoId() {
        SystemInfo systemInfo = getSystemInfo();
        return systemInfo.getId();
    }
}
