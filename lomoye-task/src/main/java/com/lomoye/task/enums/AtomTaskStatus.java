package com.lomoye.task.enums;

/**
 * 批处理子任务的状态
 * Created by tommy on 2015/8/7.
 */
public class AtomTaskStatus {
    public static final String SUBMITTED = "SUBMITTED";
    public static final String STARTED = "STARTED";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED"; //失败了，但是没有数据异常问题
    public static final String ABNORMAL = "ABNORMAL";  //无法处理的,可能有异常
    public static final String UNCHANGED = "UNCHANGED"; //无需修改的
}
