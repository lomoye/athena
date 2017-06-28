package com.lomoye.common.concurrent;

import com.lomoye.common.model.PagedModel;
import com.lomoye.common.model.ResultModel;

/**
 * Created by tommy on 2015/9/8.
 */
public class PagedFetcher<T> {
    private long pageSize;

    private long totalNum;

    private long startPageNo;

    private boolean useMethodReturnCount;

    private PagedMethod method;

    public PagedFetcher(long pageSize, long totalNum, long startPageNo) {
        this.pageSize = pageSize;
        this.totalNum = totalNum;
        this.startPageNo = startPageNo;
        this.useMethodReturnCount = false;
    }

    public PagedFetcher(long pageSize, long startPageNo) {
        this.pageSize = pageSize;
        this.startPageNo = startPageNo;
        this.useMethodReturnCount = true;
    }

    public PagedFetcher(long pageSize) {
        this.pageSize = pageSize;
        this.startPageNo = 0;
        this.useMethodReturnCount = true;
    }

    public ResultModel<T> fetchData(){
        PagedModel paged = new PagedModel(pageSize, startPageNo);
        ResultModel<T> res= new ResultModel<>();

        if(useMethodReturnCount){
            ResultModel<T> firstPage = method.getResultByPage(paged);
            if(!firstPage.isSuccess() || firstPage.getCount() == 0){
                return firstPage;
            }
            totalNum = firstPage.getCount();
            res.addData(firstPage.getData());
            paged.setPageNo(paged.getPageNo() + 1);
        }
        for(long i =0; i<(totalNum + pageSize -1)/pageSize; i++){
            ResultModel<T> pagedResult = method.getResultByPage(paged);
            if(!pagedResult.isSuccess()){
                return pagedResult;
            }
            paged.setPageNo(paged.getPageNo() + 1);
            res.addData(pagedResult.getData());
        }
        return res;
    }

    public void setMethod(PagedMethod method) {
        this.method = method;
    }
}
