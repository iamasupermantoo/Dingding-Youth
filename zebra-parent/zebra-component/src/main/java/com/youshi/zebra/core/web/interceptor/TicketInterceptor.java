package com.youshi.zebra.core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dorado.framework.tuple.TwoTuple;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.dorado.mvc.utils.ResponseUtils;
import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.passport.utils.LoginUtil;
import com.youshi.zebra.user.constant.UserType;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
@Component
public class TicketInterceptor extends HandlerInterceptorAdapter {
	private static final String DEFAULT_WWW_LOGIN_URL = "/login";
	
	@Autowired
	private LoginUtil loginUtil;

	private UserType userType;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		LoginRequired loginRequired = MethodInterceptorUtils.getAnnotaion(handler, LoginRequired.class);
		// 取用户id
		TwoTuple<Integer, Integer> two = loginUtil.getLoginUserId(request, userType);
		if (two != null) {
			WebRequestContext.setUserId(two.first);
			WebRequestContext.setTicketRandom(two.second);
		}
		
		// 需要登录要验票
		if (loginRequired != null && two == null) {
			if (loginRequired.apiMode()) {
				ResponseUtils.output(response, new JsonResultView(ZebraMetaCode.Unauthorized));
				return false;
			}
			String loginUrl = loginRequired.loginUrl();
			if (StringUtils.isBlank(loginUrl)) {
				loginUrl = DEFAULT_WWW_LOGIN_URL;
			}
			response.sendRedirect(request.getContextPath() + loginUrl);
			return false;
		}
		return true;
	}
	
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
}