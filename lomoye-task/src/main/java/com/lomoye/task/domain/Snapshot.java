package com.lomoye.task.domain;

import com.lomoye.common.domain.CommonDomain;

/**
 * 存储用户参数的快照.
 * 后续需要找一些好的方案保存用户详情页的内容
 * Created by tommy on 2015/8/7.
 */
public class Snapshot extends CommonDomain {
    private Long shopId;

    private String type;  //Discount, Reward?

    private String content;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
