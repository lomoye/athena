package com.lomoye.common.manager;

import com.lomoye.common.dao.OrderCondition;
import com.lomoye.common.dao.Page;
import com.lomoye.common.domain.CommonDomain;

import java.util.List;

/**
 * Created by tommy on 2015/7/24.
 */
public interface DomainManager<K, T extends CommonDomain> {
    int save(T obj);

    int update(T obj);

    int saveOrUpdate(T obj);

    T getById(K key);

    T getByCondition(T condition);

    List<T> listByCondition(T condition, List<OrderCondition> orderConditions);

    long count(T obj);

    int deleteById(K key);

    int deleteByCondition(T condition);

    List<T> listWithPage(T condition, Page page);
}
