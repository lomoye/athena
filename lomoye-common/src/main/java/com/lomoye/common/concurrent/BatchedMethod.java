package com.lomoye.common.concurrent;

import com.lomoye.common.model.ResultModel;

import java.util.List;

/**
 * Created by tommy on 2015/9/8.
 */
public interface BatchedMethod<T> {
    ResultModel<T> getResultByIds(List<Long> ids);
}
