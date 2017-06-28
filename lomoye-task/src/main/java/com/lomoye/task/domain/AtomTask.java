package com.lomoye.task.domain;

import com.lomoye.common.domain.CommonDomain;

/**
 * 批处理任务中一个子任务
 * Created by tommy on 2015/8/29.
 */
public class AtomTask extends CommonDomain {
    private Long shopId;

    private Long batchTaskId;  //对应的AsyncBatchTask 的Id

    private String action;  //create,update, delete

    private String param;   //对应的参数

    private String status; //success, failed, submitted

    private String description; // 描述信息

    private String errorCode;

    private String errorMessage;

    private String subErrorCode; //淘宝subCode

    private String subErrorMessage; //淘宝subMsg

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getBatchTaskId() {
        return batchTaskId;
    }

    public void setBatchTaskId(Long batchTaskId) {
        this.batchTaskId = batchTaskId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSubErrorCode() {
        return subErrorCode;
    }

    public void setSubErrorCode(String subErrorCode) {
        this.subErrorCode = subErrorCode;
    }

    public String getSubErrorMessage() {
        return subErrorMessage;
    }

    public void setSubErrorMessage(String subErrorMessage) {
        this.subErrorMessage = subErrorMessage;
    }
}
