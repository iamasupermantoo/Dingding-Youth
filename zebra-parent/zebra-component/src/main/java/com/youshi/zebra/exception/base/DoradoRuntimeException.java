package com.youshi.zebra.exception.base;


/**
 * 
* 运行时异常，一般不需要catch处理，直接抛到最上层的异常处理程序。
* Date: May 17, 2016
* 
 * @author wangsch
 *
 */
@SuppressWarnings("serial")
public class DoradoRuntimeException extends RuntimeException {
	
	public DoradoRuntimeException() {
	}

	public DoradoRuntimeException(String message) {
		super(message);
	}

	public DoradoRuntimeException(Throwable cause) {
		super(cause);
	}

	public DoradoRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public DoradoRuntimeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
