package com.lomoye.common.constant;

/**
 * 枚举类型的常量必须实现的方法
 * Created by tommy on 2016/1/16.
 */
public interface ConstantEnum<T> {
    boolean isValid(T value);
}
