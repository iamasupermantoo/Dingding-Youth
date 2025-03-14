package com.youshi.zebra.exception.base;

/**
 * 服务器内部异常，出现这个异常时，需要修复程序。包括：
 * 脏数据、实体不存在等等
* 
* Date: May 17, 2016
* 
 * @author wangsch
 *
 */
@SuppressWarnings("serial")
public class InternalException extends DoradoRuntimeException {

	public InternalException() {
	}

	public InternalException(String message) {
		super(message);
	}

	public InternalException(Throwable cause) {
		super(cause);
	}

	public InternalException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternalException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
