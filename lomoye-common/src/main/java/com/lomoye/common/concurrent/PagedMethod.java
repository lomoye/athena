package com.lomoye.common.concurrent;

import com.lomoye.common.model.PagedModel;
import com.lomoye.common.model.ResultModel;

/**
 * Created by tommy on 2015/9/8.
 */
public interface PagedMethod<T> {
    ResultModel<T> getResultByPage(PagedModel paged);
}
