package com.lomoye.common.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SpringWebContextListener implements ServletContextListener {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SpringWebContextListener.class);

    public void contextInitialized(ServletContextEvent event) {

        long startTime = System.currentTimeMillis();
        LOGGER.info("database service  web initialize begin " + startTime);

        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(event
                .getServletContext());
        SpringContextHolder.setSpringContext(ctx);

        LOGGER.info("database service  web initialize success, cost time "
                + (System.currentTimeMillis() - startTime));
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }

}
