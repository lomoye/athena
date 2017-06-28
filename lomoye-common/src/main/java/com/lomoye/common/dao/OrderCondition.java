package com.lomoye.common.dao;

public class OrderCondition {
    private String column;

    private String orderBy;

    public OrderCondition(String column, String orderBy) {
        super();
        this.column = column;
        this.orderBy = orderBy;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
