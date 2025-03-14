package com.youshi.zebra.core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.youshi.zebra.core.constants.config.BooleanConfigKey;

@Component
public class ServiceInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(!BooleanConfigKey.Service.get() && !request.getRequestURI().equals("/config/service")) {
			response.setStatus(500);
			return false;
		}
		
		return true;
	}
}