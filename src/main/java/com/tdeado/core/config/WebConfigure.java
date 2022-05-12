package com.tdeado.core.config;

import com.tdeado.core.interceptor.BaseHandlerInterceptor;
import com.tdeado.core.request.factory.RequestFieldToEnumConvertFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static cn.hutool.extra.spring.SpringUtil.getApplicationContext;

@Component
public class WebConfigure implements WebMvcConfigurer {
    Logger logger = Logger.getLogger("WebConfigure");
    MappingJackson2HttpMessageConverter converter;
    public WebConfigure(@Qualifier("jackson2HttpMessageConverter") MappingJackson2HttpMessageConverter converter) {
        this.converter = converter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("start registry Interceptor");
        Map<String, BaseHandlerInterceptor> interceptors = getApplicationContext().getBeansOfType(BaseHandlerInterceptor.class);
        //添加注册了的拦截器
        for (Map.Entry<String, BaseHandlerInterceptor> stringHandlerInterceptorEntry : interceptors.entrySet()) {
            logger.info("registered successfully "+stringHandlerInterceptorEntry.getKey());
            registry.addInterceptor(stringHandlerInterceptorEntry.getValue())
                    .addPathPatterns(stringHandlerInterceptorEntry.getValue().addPathPatterns())
                    .excludePathPatterns(stringHandlerInterceptorEntry.getValue().excludePathPatterns())
                    .pathMatcher(stringHandlerInterceptorEntry.getValue().pathMatcher())
                    .order(stringHandlerInterceptorEntry.getValue().getOrder())
            ;
        }
    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(converter);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new RequestFieldToEnumConvertFactory());
    }
}
