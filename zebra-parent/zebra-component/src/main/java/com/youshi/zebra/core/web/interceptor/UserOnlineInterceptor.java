package com.youshi.zebra.core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dorado.mvc.reqcontext.WebRequestContext;
import com.youshi.zebra.stats.service.UserStatsService;
import com.youshi.zebra.stats.utils.StatsUtils;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年8月9日
 */
@Component
public class UserOnlineInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private UserStatsService userStatsService;
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        Integer userId = WebRequestContext.getUserId();
        if (userId != null) {
			userStatsService.dealActUser(StatsUtils.today(), userId);
        }
        return true;
    }
}
