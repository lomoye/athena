package com.lomoye.common.dto;

import java.util.List;

/**
 * Created by tommy on 2015/6/2.
 */
public class ResultList<T> extends Result {

    private List<T> data;

    public ResultList(String resultCode, String resultMessage, List<T> data) {
        super(resultCode, resultMessage);
        this.data = data;
    }

    public ResultList(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
