package com.lomoye.task.domain;

import com.lomoye.common.domain.CommonDomain;

/**
 * 每个类型的成功失败数量,以及未通知用户的数量
 * Created by tommy on 2015/8/31.
 */
public class TaskStat extends CommonDomain {
    private Long shopId;

    private String type;

    private Long taskNum;

    private Long successNum;

    private Long failedNum;

    private Long abnormalNum;

    private Long notInformedNum;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Long taskNum) {
        this.taskNum = taskNum;
    }

    public Long getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(Long successNum) {
        this.successNum = successNum;
    }

    public Long getFailedNum() {
        return failedNum;
    }

    public void setFailedNum(Long failedNum) {
        this.failedNum = failedNum;
    }

    public Long getAbnormalNum() {
        return abnormalNum;
    }

    public void setAbnormalNum(Long abnormalNum) {
        this.abnormalNum = abnormalNum;
    }

    public Long getNotInformedNum() {
        return notInformedNum;
    }

    public void setNotInformedNum(Long notInformedNum) {
        this.notInformedNum = notInformedNum;
    }
}
