package com.youshi.zebra.admin.adminuser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.youshi.zebra.admin.adminuser.constant.Privilege;

/**
 * 
 * 用在Controller类或者方法上，代表这个类或方法需要有相应的权限才能访问。
 * 
 * @author wangsch
 * @date 2016-09-12
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PrivilegeRequired {
	
	/**
	 * 需要的权限集合
	 * 
	 * @see Privilege
	 */
    Privilege[] value() default {};
}
