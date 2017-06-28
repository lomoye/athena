package com.lomoye.common.util;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装下google guava的splitter
 * Created by tommy on 2016/1/17.
 */
public class SplitterUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SplitterUtil.class);

    /**
     * @param defaultValue == null 表示不需要,直接忽略
     */
    public static List<Integer> splitToIntegerList(String str, String splitter, Integer defaultValue) {
        if (Strings.isNullOrEmpty(str)) {
            return new ArrayList<>();
        }
        List<String> strings = Splitter.on(splitter).trimResults().omitEmptyStrings().splitToList(str);
        List<Integer> intList = new ArrayList<>();
        for (String s : strings) {
            try {
                Integer v = Integer.valueOf(s);
                intList.add(v);
            } catch (Exception e) {
                LOGGER.error("Convert String to Integer error|str=" + s);
                if (defaultValue != null) {
                    intList.add(defaultValue);
                }
            }
        }
        return intList;
    }

    public static List<Long> splitToLongList(String str, String splitter, Long defaultValue) {
        if (Strings.isNullOrEmpty(str)) {
            return new ArrayList<>();
        }
        List<String> strings = Splitter.on(splitter).trimResults().omitEmptyStrings().splitToList(str);
        List<Long> longList = new ArrayList<>();
        for (String s : strings) {
            try {
                Long v = Long.valueOf(s);
                longList.add(v);
            } catch (Exception e) {
                LOGGER.error("Convert String to Long error|str=" + s);
                if (defaultValue != null) {
                    longList.add(defaultValue);
                }
            }
        }
        return longList;
    }

    public static List<String> splitToStringList(String str, String splitter) {
        if (Strings.isNullOrEmpty(str)) {
            return new ArrayList<>();
        }
        return Splitter.on(splitter).trimResults().omitEmptyStrings().splitToList(str);
    }
}
