package com.lomoye.task.domain;

import com.lomoye.common.domain.CommonDomain;

/**
 * 代表一台机器上一个服务的唯一id.
 * 重启回滚数据时需要.
 * Created by tommy on 2015/8/7.
 */
public class SystemInfo extends CommonDomain {
    private String instance; //服务的实例编号(diablo-web-0, diablo-web-0等,后续如果一台机器部署同一个Module的多个实例需要使用,默认0)

    private String ip;  //系统所在ip

    private String desc; //系统描述

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
