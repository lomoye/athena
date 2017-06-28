package com.lomoye.common.file.upload;

import com.lomoye.common.model.ResultModel;

/**
 * Created by tommy on 2016/2/2.
 */
public interface LineObjectChecker<T> {

    //返回false的将不被处理, 如果不需要继续执行下去，需要checker自己抛出异常
    boolean checkLine(T obj, ResultModel<T> result);
}
