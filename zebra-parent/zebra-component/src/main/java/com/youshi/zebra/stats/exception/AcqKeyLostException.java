package com.youshi.zebra.stats.exception;

import com.youshi.zebra.exception.base.DoradoException;

/**
 * 
 * @author wangsch
 * @date 2017年8月10日
 */
public class AcqKeyLostException extends DoradoException {

	public AcqKeyLostException() {
		super();
	}

	public AcqKeyLostException(String message, Throwable cause) {
		super(message, cause);
	}

	public AcqKeyLostException(String message) {
		super(message);
	}

	public AcqKeyLostException(Throwable cause) {
		super(cause);
	}
}
