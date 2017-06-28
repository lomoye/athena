package com.lomoye.common.model;

import java.util.List;

/**
 * Created by tommy on 2016/1/28.
 * 批量任务返回结果
 */
public class BatchResultModel<S, U, F> {
    private long totalNumber;

    private long successNumber;

    private long failedNumber;

    private long unchangedNumber;

    private List<S> successList;

    private List<U> unchangedList;

    private List<F> failedList;

    private Long id; //对应的上传之类的对象的id

    public BatchResultModel(long totalNumber, long successNumber, long failedNumber, long unchangedNumber) {
        this.totalNumber = totalNumber;
        this.successNumber = successNumber;
        this.failedNumber = failedNumber;
        this.unchangedNumber = unchangedNumber;
    }

    public BatchResultModel(long totalNumber, long successNumber, long failedNumber, long unchangedNumber, Long id) {
        this.totalNumber = totalNumber;
        this.successNumber = successNumber;
        this.failedNumber = failedNumber;
        this.unchangedNumber = unchangedNumber;
        this.id = id;
    }

    public long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(long totalNumber) {
        this.totalNumber = totalNumber;
    }

    public long getSuccessNumber() {
        return successNumber;
    }

    public void setSuccessNumber(long successNumber) {
        this.successNumber = successNumber;
    }

    public long getFailedNumber() {
        return failedNumber;
    }

    public void setFailedNumber(long failedNumber) {
        this.failedNumber = failedNumber;
    }

    public long getUnchangedNumber() {
        return unchangedNumber;
    }

    public void setUnchangedNumber(long unchangedNumber) {
        this.unchangedNumber = unchangedNumber;
    }

    public List<S> getSuccessList() {
        return successList;
    }

    public void setSuccessList(List<S> successList) {
        this.successList = successList;
    }

    public List<U> getUnchangedList() {
        return unchangedList;
    }

    public void setUnchangedList(List<U> unchangedList) {
        this.unchangedList = unchangedList;
    }

    public List<F> getFailedList() {
        return failedList;
    }

    public void setFailedList(List<F> failedList) {
        this.failedList = failedList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
