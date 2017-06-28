package com.lomoye.common.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BasicMapper<K, T> {
    List<T> selectByCondition(@Param("condition") T condition, @Param("orderConditions") List<OrderCondition> orderConditions);

    T selectByPrimaryKey(K key);

    T selectOne(@Param("condition") T condition);

    long selectCount(@Param("condition") T condition);

    int insert(T record);

    int updateByPrimaryKey(T record);

//    int updateByCondition(@Param("condition")T condition, T record);

    int deleteByPrimaryKey(K key);

    int deleteByCondition(@Param("condition") T condition);

}
