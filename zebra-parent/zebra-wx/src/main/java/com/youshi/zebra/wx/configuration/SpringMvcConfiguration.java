package com.youshi.zebra.wx.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.dorado.mvc.interceptors.AccessLogInterceptor;
import com.dorado.mvc.interceptors.ForceNoCacheInterceptor;
import com.youshi.zebra.core.web.interceptor.TicketInterceptor;
import com.youshi.zebra.user.constant.UserType;

/**
 * 
 * SpringMVC相关配置，如：拦截器、过滤器等<br />
 * 可以完全通过注解和代码的方式配置，但为了方便，还是通过{@link ImportResource}引入了一个xml，这个xml来自于之前传统的war工程，只是做了少量修改。
 * 
 * @author wangsch
 * @date 2017年4月20日
 */
@Configuration
@ImportResource("classpath*:wx-servlet.xml")
public class SpringMvcConfiguration extends WebMvcConfigurerAdapter {
	@Autowired
	public TicketInterceptor ticketInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		ticketInterceptor.setUserType(UserType.Student);
		registry.addInterceptor(ticketInterceptor);
		registry.addInterceptor(new AccessLogInterceptor());
		registry.addInterceptor(new ForceNoCacheInterceptor());
	}

	@Bean
	public ErrorPageRegistrar errorPageRegistrar() {
		return new ErrorPageRegistrar() {
			@Override
			public void registerErrorPages(ErrorPageRegistry registry) {
				registry.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/400"));
				registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
				registry.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500"));
			}
		};
	}
}
