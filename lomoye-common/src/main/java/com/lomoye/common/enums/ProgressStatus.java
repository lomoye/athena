package com.lomoye.common.enums;

/**
 * 任务执行的情况.
 */
public class ProgressStatus {
    public static final String WAIT = "wait"; //任务正在等待处理

    public static final String IN_PROGRESS = "in_progress";//正在处理中

    public static final String SUCCESS = "success"; //处理完成

    public static final String FAILED = "failed"; //任务处理失败
}
