package com.lomoye.common.file;

/**
 * Created by tommy on 2016/2/3.
 */
public class ReflectionHelper {
    public static String getAttrNameFromMethod(String methodName) {
        if (methodName.length() < 4) {
            return null;
        }

        return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
    }
}
