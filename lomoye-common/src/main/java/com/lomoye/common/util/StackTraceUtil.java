package com.lomoye.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by tommy on 2015/9/5.
 */
public final class StackTraceUtil {

    public static String getStackTrace(Throwable aThrowable) {
        if (aThrowable == null) {
            return "";
        }
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * Defines a custom format for the stack trace as String.
     */
    public static String getCustomStackTrace(Throwable aThrowable) {
        if (aThrowable == null) {
            return "";
        }
        //add the class name and any message passed to constructor
        StringBuilder result = new StringBuilder("error-stack: ");
        result.append(aThrowable.toString());
        //add each element of the stack trace
        for (StackTraceElement element : aThrowable.getStackTrace()) {
            result.append(element);
            result.append("\n");
        }
        return result.toString();
    }

    public static String getCustomStackTrace(Throwable aThrowable, int lineCnt) {
        if (aThrowable == null) {
            return "";
        }
        //add the class name and any message passed to constructor
        StringBuilder result = new StringBuilder("error-stack: ");
        result.append(aThrowable.toString());
        //add each element of the stack trace
        int cnt = 0;
        for (StackTraceElement element : aThrowable.getStackTrace()) {
            result.append(element);
            result.append("\n");
            cnt++;
            if (cnt == lineCnt) {
                break;
            }
        }
        return result.toString();
    }
}
