package com.lomoye.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by tommy on 2015/6/17. 通用的配置方案. Web和后台都可以用
 */
public class ConfigProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger("ConfigProperties");

    private static Map<String, ConfigProperties> props = new HashMap<>(); //alias map

    private Properties prop;

    private ConfigProperties(Properties prop) {
        this.prop = prop;
    }

    public static ConfigProperties getInstance(String alias) {
        return props.get(alias);
    }

    synchronized public static boolean initialize(Properties prop, String alias) {

        ConfigProperties existProp = props.get(alias);
        if (existProp != null) {
            LOGGER.warn("Config file initialized already: " + alias);
            return true;
        }
        ConfigProperties configProp = new ConfigProperties(prop);
        props.put(alias, configProp);
        LOGGER.warn("Config file " + alias + " | " + prop.toString());
        return true;
    }

    synchronized public static void initializeProperty(String propertyFile, String alias) {
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("classpath:" + propertyFile);

        InputStream in = null;
        Properties prop = null;
        try {
            in = resource.getInputStream();
            prop = new Properties();
            prop.load(in);
        } catch (Exception e) {
            LOGGER.error("initialize config error ....", e);
            System.exit(-1);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                LOGGER.error("close config file error ....", e);
                System.exit(-1);
            }
        }
        ConfigProperties.initialize(prop, alias);
    }


    public String getProperty(String key) {
        return prop.getProperty(key);
    }
}
