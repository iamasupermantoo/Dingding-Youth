package com.youshi.zebra.core.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 实体未找到
 * 
 * Date: Jun 2, 2016
 * 
 * @author wangsch
 *
 */
@SuppressWarnings("serial")
public class EntityNotFoundException extends DoradoMetaCodeException {

	public EntityNotFoundException() {
	}

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(Throwable cause) {
		super(cause);
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dorado.exception.DoradoAPIException#getMetaCode()
	 */
	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.EntityNotFound;
	}

}
