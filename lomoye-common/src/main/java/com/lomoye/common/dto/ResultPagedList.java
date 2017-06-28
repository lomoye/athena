package com.lomoye.common.dto;

import java.util.List;

/**
 * Created by tommy on 2015/8/6.
 */
public class ResultPagedList<T> extends Result {
    private List<T> data;

    private Long pageSize = 10L;

    private Long pageNo = 0L;

    private Long count = 0L;

    public ResultPagedList() {
    }


    public ResultPagedList(List<T> data, Long count, com.lomoye.common.model.PagedModel paged) {
        this.data = data;
        this.count = count;
        this.pageSize = paged.getPageSize();
        this.pageNo = paged.getPageNo();
    }

    public ResultPagedList(String resultCode, String resultMessage, List<T> data) {
        super(resultCode, resultMessage);
        this.data = data;
    }

    public ResultPagedList(Long pageSize, Long pageNo, Long count) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
