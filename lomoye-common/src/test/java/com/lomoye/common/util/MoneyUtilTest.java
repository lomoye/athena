package com.lomoye.common.util;

import junit.framework.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * 测试注意点:
 * 空指针,异常格式
 * Created by tommy on 2016/7/28.
 */
public class MoneyUtilTest {

    @Test
    public void testConventStringToLong() throws Exception {
        String money = null;
        Assert.assertTrue(MoneyUtil.conventStringToLong(money) == null);

        money = "3";
        Assert.assertTrue(MoneyUtil.conventStringToLong(money).equals(300L));

        money = "3.25";
        Assert.assertTrue(MoneyUtil.conventStringToLong(money).equals(325L));

        money = "3.25sc";
        Assert.assertTrue(MoneyUtil.conventStringToLong(money) == null);

    }

    @Test
    public void testConvertCentToString() throws Exception {
        Long cent = null;
        Assert.assertTrue(MoneyUtil.convertCentToString(cent, 2) == null);

        cent = 0L;
        Assert.assertTrue(MoneyUtil.convertCentToString(cent, 2).equals("0.00"));

        cent = 123L;
        Assert.assertTrue(MoneyUtil.convertCentToString(cent, 2).equals("1.23"));

        cent = 123456L;
        Assert.assertTrue(MoneyUtil.convertCentToString(cent, 1).equals("1234.6"));
    }

    @Test
    public void testConvertCentToBigDecimal() throws Exception {
        Long cent = null;
        Assert.assertTrue(MoneyUtil.convertCentToBigDecimal(cent, 2) == null);

        cent = 0L;
        Assert.assertTrue(MoneyUtil.convertCentToBigDecimal(cent, 2).equals(BigDecimal.valueOf(0.00).setScale(2, BigDecimal.ROUND_HALF_UP)));

        cent = 123L;
        Assert.assertTrue(MoneyUtil.convertCentToBigDecimal(cent, 2).equals(BigDecimal.valueOf(1.23).setScale(2, BigDecimal.ROUND_HALF_UP)));

        cent = 123456L;
        Assert.assertTrue(MoneyUtil.convertCentToBigDecimal(cent, 2).equals(BigDecimal.valueOf(1234.56).setScale(2, BigDecimal.ROUND_HALF_UP)));
    }
}