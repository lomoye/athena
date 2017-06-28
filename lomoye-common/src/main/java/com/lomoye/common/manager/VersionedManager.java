package com.lomoye.common.manager;

import com.lomoye.common.domain.VersionedDomain;

/**
 * Created by tommy on 2016/6/3.
 */
public interface VersionedManager<K, T extends VersionedDomain> extends DomainManager<K, T>{
    int updateByPrimaryKeyWithVersion(T obj);
}
