package com.youshi.zebra.core.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要登录
 * @author wangsch
 *
 * @date 2016-09-12
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {

    String loginUrl() default "";

    boolean apiMode() default true;

}
