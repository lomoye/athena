package com.lomoye.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 转换和钱相关的一些代码.
 */
public class MoneyUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoneyUtil.class);

    /****
     * 把形如200.07的String 转成20007
     ****/
    public static Long conventStringToLong(String money) {
        if (money == null) {
            return null;
        }
        BigDecimal decimal;
        try {
            decimal = new BigDecimal(money);
        } catch (RuntimeException e) {
            LOGGER.warn("Convent money to decimal error |" + money);
            return null;
        }
        decimal = decimal.multiply(new BigDecimal(100L));  //转成分为单位的 *100
        return decimal.longValue();
    }

    /***
     * 把整数分转成以元为单位的，scale决定了需要几位小数
     * 20007 -> "200.07"
     */
    public static String convertCentToString(Long cent, int scale) {
        if (cent == null) {
            return null;
        }
        return new BigDecimal(cent).multiply(new BigDecimal("0.01")).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /***
     * 把整数分转成以元为单位的，scale决定了需要几位小数
     * 20007 -> "200.07"
     */
    public static BigDecimal convertCentToBigDecimal(Long cent, int scale) {
        if (cent == null) {
            return null;
        }
        return new BigDecimal(cent).multiply(new BigDecimal("0.01")).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }
}
