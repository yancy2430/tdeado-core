package com.tdeado.core.interceptor;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 拦截器接口
 */
public interface BaseHandlerInterceptor extends HandlerInterceptor {
    public default Integer getOrder() {
        return Integer.MAX_VALUE;
    }

    public default String[] addPathPatterns() {
        return new String[]{"/**"};
    }

    public default String[] excludePathPatterns() {
        return new String[]{};
    }

    public default PathMatcher pathMatcher() {
        return new AntPathMatcher();
    }
}
