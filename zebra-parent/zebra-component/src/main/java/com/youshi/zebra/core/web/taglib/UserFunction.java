package com.youshi.zebra.core.web.taglib;

import com.youshi.zebra.user.constant.UserStatus;

/**
 * 用户相关的一些jstl函数
 * 
 * @author wangsch
 * @date 2016年11月04日
 */
public class UserFunction {
    /**
     * 用户是否被封禁
     * 
     * @param status	用户状态，int值
     * @return			true如果被封禁，否则false
     */
    public static final boolean isBlocked(Integer status) {
    	UserStatus userStatus = UserStatus.fromValue(status);
    	return userStatus == UserStatus.UserBlocked;
    }
}
