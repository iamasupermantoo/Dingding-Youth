package com.youshi.zebra.exception.common;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 操作太频繁，可能带有攻击行为
* 
* Date: May 17, 2016
* 
 * @author wangsch
 *
 */
@SuppressWarnings("serial")
public class OpTooFastException extends DoradoMetaCodeException {

	public OpTooFastException() {
		super();
	}

	public OpTooFastException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public OpTooFastException(String message, Throwable cause) {
		super(message, cause);
	}

	public OpTooFastException(String message) {
		super(message);
	}

	public OpTooFastException(Throwable cause) {
		super(cause);
	}

	public MetaCode getMetaCode() {
		return ZebraMetaCode.OpTooFast;
	}
}
