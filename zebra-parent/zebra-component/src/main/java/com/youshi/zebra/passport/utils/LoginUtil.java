package com.youshi.zebra.passport.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.tuple.TwoTuple;
import com.dorado.mvc.utils.CookieUtils;
import com.youshi.zebra.passport.constant.PassportConstants;
import com.youshi.zebra.passport.service.UserPassportService;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;

/**
 * 登录辅助工具
 * 
 * 当前使用Session保存用户信息，以后扩展web的时候再加入票的支持，到相应的Interceptor上加入兼容处理(
 * 这个实现保证如果不往session里存数据，取数据的时候不会激活session，所以放心取两次吧)
 * 
 * @author wangsch
 *
 * @date 2016-09-12
 */
@Service
public class LoginUtil {

    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LoginUtil.class);

    @Autowired
    private UserPassportService passportService;

    @Autowired
    private UserService userService;

    public static String getCurrentToken(HttpServletRequest request) {
	String cookieTicket = request.getParameter(PassportConstants.TICKET_NAME);
	if (StringUtils.isBlank(cookieTicket)) {
	    cookieTicket = CookieUtils.getCookie(request, PassportConstants.TICKET_NAME);
	    if (StringUtils.isBlank(cookieTicket)) {
		return null;
	    }
	}
	return StringUtils.trimToNull(cookieTicket);
    }

    /**
     * 获取ticket，验证ticket有效性、用户状态、用户类型，一切验证通过则返回用户id和用户的random值，否则返回null
     * 
	 * @param request
	 *            {@link HttpServletRequest}对象
	 * @param requiredUserType
	 *            {@link UserType}，要求的用户类型，不允许为null
	 * @return TwoTuple first: 用户id，second：random值
     */
    public TwoTuple<Integer, Integer> getLoginUserId(HttpServletRequest request, UserType requiredUserType) {
    	if(requiredUserType == null) {
    		throw new NullPointerException();
    	}
    	
		String ticket = getCurrentToken(request);
		if (ticket == null) {
		    return null;
		}
	
		TwoTuple<Integer, Integer> two = passportService.verifyTicketWithRand(ticket);
//		Integer resultUserId = two.first;
		if (two != null) {
			Integer resultUserId = two.first;
		    UserModel user = userService.getById(resultUserId);
			if (UserStatus.isNotNormal(user)) {
				logger.error("User is Not Normal, verify ticket FAIL. userId: {}", resultUserId);
				return null;
		    }
			UserType userType = UserType.fromValue(user.getType());
			if(userType != requiredUserType) {
				logger.error("User type MISMATCH, verify ticket FAIL. userId: {}, userType: {}, requiredUserType: {}", 
						resultUserId, userType, requiredUserType);
				return null;
			}
		}
		return two;
    }
    

    /**
     * 设置session当前登录用户的id
     * 
     * @param request
     * @param user_id
     */
	public void setTicket(HttpServletRequest request, HttpServletResponse response, Integer userId, int expireSecond) {
		setTicket(request, response, userId, expireSecond, true);
	}

	public void setTicket(HttpServletRequest request, HttpServletResponse response, Integer userId, int expireSecond,
			boolean isSecure) {
		String ticket = passportService.createTicketWithRand(userId);
		CookieUtils.saveCookie(response, PassportConstants.TICKET_NAME, ticket, expireSecond, request.getServerName(),
				"/", true, isSecure);
	}
}