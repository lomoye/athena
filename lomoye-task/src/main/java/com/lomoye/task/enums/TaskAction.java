package com.lomoye.task.enums;

/**
 * 子任务的类型
 * Created by tommy on 2015/8/7.
 */
public class TaskAction {
    public static final String CREATE = "CREATE";
    public static final String UPDATE = "UPDATE";
    public static final String STOP = "STOP";


    //BatchTask还增加两种类型(AtomTask不要用)
    public static final String RETRY = "RETRY";  //重试
    public static final String ROLLBACK = "ROLLBACK";  //回滚
}
