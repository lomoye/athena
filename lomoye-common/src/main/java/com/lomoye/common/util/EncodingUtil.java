package com.lomoye.common.util;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by tommy on 2015/6/21.
 */
public class EncodingUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncodingUtil.class);

    public static String getUrlDecodedString(String str, String encoding) {
        if (Strings.isNullOrEmpty(str)) {
            return str;
        }
        try {
            return URLDecoder.decode(str, encoding);
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("decode string |" + str + " | " + encoding + " | failed");
            throw new RuntimeException("getUrlDecodedString error|" + str);
        }
    }

    public static String getUrlEncodedString(String str, String encoding){
        if (Strings.isNullOrEmpty(str)) {
            return str;
        }
        try {
            return URLEncoder.encode(str, encoding);
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("decode string |" + str + " | " + encoding + " | failed");
            throw new RuntimeException("getUrlEncodedString error|" + str);
        }
    }
}
