package com.lomoye.common.web;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

public class SpringContextHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringContextHolder.class);

    private static ApplicationContext context;

    public static void setSpringContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static boolean isReady() {
        return context != null;
    }

    /**
     * 从spring content 获取bean 实例
     */
    public static Object getBean(String name) {
        if (Strings.isNullOrEmpty(name)) {
            LOGGER.warn("bean name is empty..");
            return null;
        }
        try {
            return context.getBean(name);
        } catch (Exception e) {
            LOGGER.warn("Get bean from baseContent by bean name {} ,but not exist..", name);
            return null;
        }
    }

    /**
     * 从spring content 获取bean 实例
     */
    public static <T> T getBean(String name, Class<T> type) {
        if (Strings.isNullOrEmpty(name)) {
            LOGGER.warn("bean name is empty..");
            return null;
        }
        try {
            return context.getBean(name, type);
        } catch (Exception e) {
            LOGGER.warn("Get bean from baseContent by bean name {} ,but not exist..", name);
            return null;
        }
    }

    public static String getMessage(String key) {
        return getMessage(key, new String[]{}, getRequestLocale());
    }

    public static String getMessage(String key, Locale locale) {
        return getMessage(key, new String[]{}, locale);
    }

    public static String getMessage(String msgKey, String[] values) {
        return getMessage(msgKey, values, getRequestLocale());
    }

    /**
     * 从spring message 获取中value
     */
    public static String getMessage(String key, String[] values, Locale locale) {
        if (Strings.isNullOrEmpty(key)) {
            LOGGER.warn("Get message from messageResource by key ,but key is empty..");
            return key;
        }
        try {
            return context.getMessage(key, values, locale);
        } catch (NoSuchMessageException e) {
            LOGGER.warn("Get message from messageResource by key {} ,but not exist..", key);
            return key;
        }
    }

    /**
     * 获取请求语言 用cookie维持会话语言
     */
    public static Locale getRequestLocale() {
        Locale locale = org.springframework.util.StringUtils.parseLocaleString("zh_CN");
        // get jvm default locale
        LOGGER.debug("request locale {}", locale.getLanguage() + "_" + locale.getCountry());
        return locale;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
