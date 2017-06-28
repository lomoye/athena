package com.lomoye.common.dao;

import com.lomoye.common.model.PagedModel;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

public class Page {

    private static final String ORDER_BY_DESC = "desc";

    private static final String ORDER_BY_ASC = "asc";

    private Long pageSize;

    private Long pageNo;

    private List<OrderCondition> orderConditions = new ArrayList<OrderCondition>();

    public Page(PagedModel pagedModel){
        this(pagedModel.getPageSize(), pagedModel.getPageNo());
        if(!Strings.isNullOrEmpty(pagedModel.getColumn()) && !Strings.isNullOrEmpty(pagedModel.getOrderby())){
            if(pagedModel.getOrderby().equalsIgnoreCase(ORDER_BY_ASC)){
                orderConditions.add(new OrderCondition(pagedModel.getColumn(), ORDER_BY_ASC));
            }
            if(pagedModel.getOrderby().equalsIgnoreCase(ORDER_BY_DESC)){
                orderConditions.add(new OrderCondition(pagedModel.getColumn(), ORDER_BY_DESC));
            }
        }

    }

    public Page(Long pageSize, Long pageNo) {
        super();
        if (pageSize == null) {
            pageSize = 10L;
        }
        if (pageNo == null) {
            pageNo = 0L;
        }
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }

    public Long getPageStart() {
        return pageSize * pageNo;
    }

    public void addOrder(String column, String orderBy) {
        if (!ORDER_BY_DESC.equalsIgnoreCase(orderBy) && !ORDER_BY_ASC.equalsIgnoreCase(orderBy)) {
            throw new UnsupportedOperationException("orderBy should be asc or desc");
        }
        OrderCondition oc = new OrderCondition(column, orderBy);
        orderConditions.add(oc);
    }

    public static String getOrderByDesc() {
        return ORDER_BY_DESC;
    }

    public static String getOrderByAsc() {
        return ORDER_BY_ASC;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public List<OrderCondition> getOrderConditions() {
        return orderConditions;
    }

    public void setOrderConditions(List<OrderCondition> orderConditions) {
        this.orderConditions = orderConditions;
    }
}
