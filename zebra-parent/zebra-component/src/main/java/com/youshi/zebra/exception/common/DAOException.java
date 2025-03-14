package com.youshi.zebra.exception.common;

import com.youshi.zebra.exception.base.InternalException;

/**
 * dao操作异常，内部程序错误
 * 
 * 
 * @author wangsch
 * @date 2016-09-12
 */
@SuppressWarnings("serial")
public class DAOException extends InternalException {

	public DAOException() {
		super();
	}

	public DAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(String message) {
		super(message);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}
	
}
