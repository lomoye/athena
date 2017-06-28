package com.lomoye.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tommy on 2015/9/8.
 */
public class ResultModel<T> {
    private long count = 0;

    private List<T> data = new ArrayList<>();

    private String resultCode="";

    private String resultMessage="";

    private boolean isSuccess = false;

    public ResultModel(long count, List<T> data, String resultCode, String resultMessage, boolean isSuccess) {
        this.count = count;
        this.data = data;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.isSuccess = isSuccess;
    }

    public ResultModel(List<T> data, String resultCode, String resultMessage, boolean isSuccess) {
        this.data = data;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.isSuccess = isSuccess;
    }

    public ResultModel(List<T> data) {
        this.data = data;
        this.count = data.size();
        this.isSuccess = true;
    }

    public ResultModel() {
        this.isSuccess = true;
    }


    public ResultModel(String resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.isSuccess = false;
    }

    public void addData(List<T> added){
        this.data.addAll(added);
        count += added.size();
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
