package com.lomoye.common.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PagedMapper<K, T> extends BasicMapper<K, T> {

    List<T> selectWithPage(@Param("condition") T condition, @Param("page") Page page);
}
