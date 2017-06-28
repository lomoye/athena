package com.lomoye.common.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tommy on 2015/5/31.
 * 基础的Domain
 */
public class CommonDomain implements Serializable{
    Long id;
    Date createTime;
    Date modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
