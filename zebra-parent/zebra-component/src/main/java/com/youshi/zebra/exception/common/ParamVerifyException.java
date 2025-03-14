package com.youshi.zebra.exception.common;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 参数校验失败
* 
 * @author wangsch
 * @date 2016-09-13
 */
@SuppressWarnings("serial")
public class ParamVerifyException extends DoradoMetaCodeException {

	public ParamVerifyException() {
	}

	public ParamVerifyException(String message) {
		super(message);
	}

	public ParamVerifyException(Throwable cause) {
		super(cause);
	}

	public ParamVerifyException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParamVerifyException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.ParmVerifyFail;
	}

}
