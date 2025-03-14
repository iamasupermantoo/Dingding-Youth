package com.youshi.zebra.admin.adminuser.exception;

import com.youshi.zebra.exception.base.DoradoException;

/**
 * 
 * @author wangsch
 * @date 2017年2月27日
 */
@SuppressWarnings("serial")
public class AdminUserLoginException extends DoradoException {

	public AdminUserLoginException(String message) {
		super(message);
	}
}
