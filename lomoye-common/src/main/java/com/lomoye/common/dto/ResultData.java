package com.lomoye.common.dto;

public class ResultData<T> extends Result {

    private T data;

    public ResultData(String resultCode, String resultMessage, T data) {
        super(resultCode, resultMessage);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResultData(T data) {
        this.data = data;
    }

    public ResultData() {
    }
}
