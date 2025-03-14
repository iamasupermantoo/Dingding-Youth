package com.youshi.zebra.stats.exception;

import com.youshi.zebra.exception.base.DoradoException;

/**
 * 
 * @author wangsch
 * @date 2017年8月10日
 */
public class ActKeyLostException extends DoradoException {

	public ActKeyLostException() {
		super();
	}

	public ActKeyLostException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActKeyLostException(String message) {
		super(message);
	}

	public ActKeyLostException(Throwable cause) {
		super(cause);
	}
}
