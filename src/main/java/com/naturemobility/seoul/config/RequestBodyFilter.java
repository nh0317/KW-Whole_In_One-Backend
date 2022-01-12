package com.naturemobility.seoul.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class RequestBodyFilter implements Filter {

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (servletRequest.getContentType() == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (servletRequest.getContentType().startsWith("multipart/form-data")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            RequestBodyHttpServletRequest wrappedRequest = new RequestBodyHttpServletRequest((HttpServletRequest) servletRequest);
            filterChain.doFilter(wrappedRequest, servletResponse);
        }
    }
}