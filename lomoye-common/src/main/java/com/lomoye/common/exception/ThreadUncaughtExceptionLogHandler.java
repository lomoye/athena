package com.lomoye.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tommy on 2016/1/12.
 */
public class ThreadUncaughtExceptionLogHandler implements Thread.UncaughtExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUncaughtExceptionLogHandler.class);


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOGGER.error("Thread-|" + t.getName() +" | get uncaught exception:", e);
    }
}
