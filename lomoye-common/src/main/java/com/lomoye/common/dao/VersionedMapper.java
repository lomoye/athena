package com.lomoye.common.dao;

import com.lomoye.common.domain.VersionedDomain;

/**
 * Created by tommy on 2016/6/3.
 */
public interface VersionedMapper<K, T extends VersionedDomain> extends BasicMapper<K, T> {
    int updateByPrimaryKeyWithVersion(T record);
}
