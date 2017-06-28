package com.lomoye.common.domain;

/**
 * 带版本号的对象,使用updateWithVersion更新.
 */
public class VersionedDomain extends CommonDomain {
    private Long version;  //更新使用的版本号

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
