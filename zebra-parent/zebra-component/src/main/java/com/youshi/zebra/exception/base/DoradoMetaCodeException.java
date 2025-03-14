package com.youshi.zebra.exception.base;

import com.dorado.framework.crud.model.HasMetaCode;


/**
 * 
 * 带有metacode的异常
 * 
 * @date: May 17, 2016
 * @author wangsch
 * @see DoradoException
 */
@SuppressWarnings("serial")
public abstract class DoradoMetaCodeException extends DoradoRuntimeException
	implements HasMetaCode {

	public DoradoMetaCodeException() {
		super();
	}

	public DoradoMetaCodeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DoradoMetaCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public DoradoMetaCodeException(String message) {
		super(message);
	}

	public DoradoMetaCodeException(Throwable cause) {
		super(cause);
	}

}
