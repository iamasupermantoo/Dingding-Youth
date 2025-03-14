package com.youshi.zebra.core.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.youshi.zebra.core.web.binder.URIStatsBinder;

/**
 * 这个玩意，用在接口上，通过{@link URIStatsBinder}执行一些统计操作。<br />
 * 需要注意的是，统计操作要控制耗时，不要太大的影响接口的响应时间。
 * 
 * @author wangsch
 * @date 2017年1月3日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface URIStats {
	
	StatsType value();
	
	StatsWhen when() default StatsWhen.SUCC;
	
	public enum StatsType {
		register,
	}
	
	public enum StatsWhen {
		ANY,
		SUCC,
	}
}



