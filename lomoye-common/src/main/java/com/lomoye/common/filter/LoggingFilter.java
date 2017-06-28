package com.lomoye.common.filter;

import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

public class LoggingFilter extends CommonsRequestLoggingFilter {

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {

    }

    /**
     * Writes a log message after the request is processed.
     */
    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.debug(request.getMethod() + " | " + message);
    }
}
