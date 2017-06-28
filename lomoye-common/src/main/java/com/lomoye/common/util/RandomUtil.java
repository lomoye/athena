package com.lomoye.common.util;

import java.util.Random;

/**
 * Created by tommy on 2015/10/28.
 */
public class RandomUtil {

    private static final String STRING_BASE = "abcdefghijklmnopqrstuvwxyz0123456789"; //生成字符串从此序列中取

    private static final String NUMBER_BASE = "123456789"; //生成数字字符串从此序列中取

    public static String generateRandomString(int length) {
        Random random = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(STRING_BASE.length());
            sb.append(STRING_BASE.charAt(number));
        }
        return sb.toString();
    }

    public static String generateNumberRandomString(int length) {
        Random random = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(NUMBER_BASE.length());
            sb.append(NUMBER_BASE.charAt(number));
        }
        return sb.toString();
    }
}
