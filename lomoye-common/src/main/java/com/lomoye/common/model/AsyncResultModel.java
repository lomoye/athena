package com.lomoye.common.model;

/**
 * Created by tommy on 2017/4/6.
 * 异步任务提交时返回的结果对象
 */
public class AsyncResultModel<T> {
    private Long taskId; //任务的id,通过这个来轮训

    private T attachment; //相关的数据对象

    public AsyncResultModel(Long taskId, T attachment) {
        this.taskId = taskId;
        this.attachment = attachment;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public T getAttachment() {
        return attachment;
    }

    public void setAttachment(T attachment) {
        this.attachment = attachment;
    }
}
