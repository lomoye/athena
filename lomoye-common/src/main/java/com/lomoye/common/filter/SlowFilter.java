package com.lomoye.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SlowFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlowFilter.class);

    private static final long SLOW_REQUEST_THRESHOLD = 200;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("SlowFilter just supports HTTP requests");
        }
        long start = System.currentTimeMillis();
        HttpServletRequest req = (HttpServletRequest) request;
        try {
            chain.doFilter(request, response);
        } finally {
            long time = System.currentTimeMillis() - start;
            if (time >= SLOW_REQUEST_THRESHOLD) {
                LOGGER.warn("Slow request logged:" + time + "ms|" + req.getMethod() + "|" + req.getRequestURL() + "|" + req.getQueryString());
            }
        }
    }

    @Override
    public void destroy() {

    }
}
