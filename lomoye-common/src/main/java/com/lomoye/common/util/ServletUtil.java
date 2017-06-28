package com.lomoye.common.util;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by tommy on 2015/10/23.
 */
public class ServletUtil {
    public static boolean sendRedirect(HttpServletResponse response, String url){
        if(Strings.isNullOrEmpty(url)){
            return false;
        }
        try {
            response.sendRedirect(url);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
