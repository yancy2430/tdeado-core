package com.tdeado.core.annotations;

import java.lang.annotation.*;

/**
 * 不需要登录注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface NotLogin {
}
