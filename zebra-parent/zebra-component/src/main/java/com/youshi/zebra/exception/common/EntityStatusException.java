package com.youshi.zebra.exception.common;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年3月14日
 */
@SuppressWarnings("serial")
public class EntityStatusException extends DoradoMetaCodeException {

	public EntityStatusException() {
	}

	public EntityStatusException(String message) {
		super(message);
	}

	public EntityStatusException(Throwable cause) {
		super(cause);
	}

	public EntityStatusException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityStatusException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.EntityStatusInvalid;
	}

}
