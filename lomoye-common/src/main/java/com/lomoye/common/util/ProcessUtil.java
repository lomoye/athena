package com.lomoye.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * 进程管理相关的工具类
 * Created by tommy on 2017/1/10.
 */
public class ProcessUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessUtil.class);

    private static long processId = -1;

    public static synchronized long getProcessId() {
        if (processId != -1) {
            return processId;
        }
        processId = doGetProcessId();
        return processId;
    }

    private static long doGetProcessId() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        try {
            return Long.valueOf(runtimeMXBean.getName().split("@")[0]);
        } catch (Exception e) {
            LOGGER.warn("get process id failed", e);
            return 0;
        }
    }
}
