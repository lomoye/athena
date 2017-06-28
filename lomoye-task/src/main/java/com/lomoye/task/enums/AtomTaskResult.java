package com.lomoye.task.enums;

/**
 * 子任务处理的结果
 * 1:成功
 * -1:失败
 * 0:无需修改
 * Created by tommy on 2017/2/20.
 */
public class AtomTaskResult {

    public static final int SUCCESS = 1; //成功
    public static final int FAILED = -1; //失败了，但是没有数据异常问题
    public static final int UNCHANGED = 0; //无需修改的
}
