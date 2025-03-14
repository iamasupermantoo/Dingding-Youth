package com.youshi.zebra.tech.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.dorado.mvc.interceptors.AccessLogInterceptor;
import com.dorado.mvc.interceptors.ForceNoCacheInterceptor;
import com.youshi.zebra.core.web.interceptor.TicketInterceptor;

/**
 * 
 * SpringMVC相关配置，如：拦截器、过滤器等<br />
 * 可以完全通过注解和代码的方式配置，但为了方便，还是通过{@link ImportResource}引入了一个xml，这个xml来自于之前传统的war工程，只是做了少量修改。
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
@Configuration
@ImportResource("classpath*:tech-servlet.xml")
public class SpringMvcConfiguration extends WebMvcConfigurerAdapter {
	
	@Autowired
	public TicketInterceptor ticketInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(ticketInterceptor);
		registry.addInterceptor(new AccessLogInterceptor());
		registry.addInterceptor(new ForceNoCacheInterceptor());
	}
}
