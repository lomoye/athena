package com.lomoye.common.util;

import java.math.BigDecimal;

/**
 * Created by tommy on 2015/8/14.
 */
public class BigDecimalUtil {
    //BigDecimal 的equals认为2.0 和2.00不等的
    public static boolean valueEquals(BigDecimal a, BigDecimal b) {
        return (a == null && b == null) || (a != null && b != null && (a.compareTo(b) == 0));
    }
}
