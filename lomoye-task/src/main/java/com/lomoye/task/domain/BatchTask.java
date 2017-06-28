package com.lomoye.task.domain;

import com.lomoye.common.domain.CommonDomain;

/**
 * 代表一个批处理任务
 * Created by tommy on 2015/8/29.
 */
public class BatchTask extends CommonDomain {
    private Long version;    //使用的版本,主要是反解析json时候可能需要

    private String type;    //类型

    private Long systemInfoId;     //启动的id, 必须是递增的

    private String systemInstance;      //ref: SystemInfo.instance

    private Long shopId;

    private String action;  //create,update,stop

    private String operator;  //userName. 为空表示系统后台

    private String description;   //restart from xxx. backend delete xxx

    private Long beforeSnapId; //之前的打折快照Id

    private Long afterSnapId;

    private Long paramSnapId;

    private String status;  //submitted, dispatched, success, failed

    private Boolean allShop;

    private Long totalNum;  //子任务的数量

    private Long unchangedNum; //成功的数量

    private Long successNum; //成功的数量

    private Long failedNum;  //失败的数量

    private String errorCode;  //错误码

    private String errorMessage; //失败的出错信息

    private Integer informed;  //0 need inform. 1 informed

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSystemInfoId() {
        return systemInfoId;
    }

    public void setSystemInfoId(Long systemInfoId) {
        this.systemInfoId = systemInfoId;
    }

    public String getSystemInstance() {
        return systemInstance;
    }

    public void setSystemInstance(String systemInstance) {
        this.systemInstance = systemInstance;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getBeforeSnapId() {
        return beforeSnapId;
    }

    public void setBeforeSnapId(Long beforeSnapId) {
        this.beforeSnapId = beforeSnapId;
    }

    public Long getAfterSnapId() {
        return afterSnapId;
    }

    public void setAfterSnapId(Long afterSnapId) {
        this.afterSnapId = afterSnapId;
    }

    public Long getParamSnapId() {
        return paramSnapId;
    }

    public void setParamSnapId(Long paramSnapId) {
        this.paramSnapId = paramSnapId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getAllShop() {
        return allShop;
    }

    public void setAllShop(Boolean allShop) {
        this.allShop = allShop;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getUnchangedNum() {
        return unchangedNum;
    }

    public void setUnchangedNum(Long unchangedNum) {
        this.unchangedNum = unchangedNum;
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

    public Integer getInformed() {
        return informed;
    }

    public void setInformed(Integer informed) {
        this.informed = informed;
    }
}
