package com.lomoye.common.util;

import com.google.common.base.Strings;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Parse 淘宝商品链接,从中取出商品id.
 */
public class LinkParserUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkParserUtil.class);

    public static Long getTaobaoItemId(String link) {
        if (Strings.isNullOrEmpty(link)) {
            return null;
        }

        try {
            if (NumberUtils.isDigits(link)) {
                return Long.valueOf(link.trim());
            }

            link = LinkParserUtil.getValidUrl(link);
            Map<String, String> maps = splitQuery(new URL(link));

            String itemId = maps.get("id");
            if (Strings.isNullOrEmpty(itemId)) {
                itemId = maps.get("itemid");
            }
            if (Strings.isNullOrEmpty(itemId)) {
                itemId = maps.get("itemId");
            }
            if (Strings.isNullOrEmpty(itemId)) {
                return null;
            }

            if (NumberUtils.isDigits(itemId)) {
                return Long.valueOf(itemId);   //taobao Id
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("parse taobao url error : " + link, e);
            return null;
        }
    }

    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String query = url.getQuery();
        if (Strings.isNullOrEmpty(query)) {
            return query_pairs;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx <= 0) {
                continue;
            }
            String name = pair.substring(0, idx);
            String value = pair.substring(idx + 1);
            if (!Strings.isNullOrEmpty(name) && !Strings.isNullOrEmpty(value)) {
                query_pairs.put(URLDecoder.decode(name, "UTF-8"), URLDecoder.decode(value, "UTF-8"));
            }
        }
        if (url.getHost().contains("detail.ju.taobao.com")) {
            query_pairs.remove("id"); //聚划算的不适用id,使用的是itemId
        }
        return query_pairs;
    }

    public static String getValidUrl(String link) {
        if (Strings.isNullOrEmpty(link)) {
            return "";
        }
        if (link.endsWith("_.webp")) {
            link = link.substring(0, link.length() - "_.webp".length());
        }

        if (link.startsWith("http")) {
            return link;
        }
        if (link.startsWith("//")) {
            return "http:" + link;
        }

        return "http://" + link;
    }


}