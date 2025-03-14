package com.youshi.zebra.admin.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.youshi.zebra.admin.log.constants.AdminLogType;

/**
 * 
 * 用在Controller方法上，表示需要记录管理员操作日志
 * 
 * @author wangsch
 *
 * @date 2016-09-12
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AdminLog {
	
	/**
	 * 操作日志类型
	 * @return	{@link AdminLogType}
	 */
    AdminLogType value();
    
    /**
     * 是否记录访问的uri
     * 
     * @return	默认是false不记录
     */
    boolean recordUri() default false;

}
