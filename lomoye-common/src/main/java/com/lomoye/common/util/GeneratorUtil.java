package com.lomoye.common.util;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tommy on 2016/8/28.
 */
public class GeneratorUtil {

    //包含start,不包含end,defaultValue不能够为空
    public static <T> Map<Integer, T> getSequentialMap(int start, int end, T defaultValue) {
        Preconditions.checkNotNull(defaultValue);

        Map<Integer, T> map = new HashMap<>();
        for (int i = start; i < end; i++) {
            map.put(i, defaultValue);
        }
        return map;
    }
}
