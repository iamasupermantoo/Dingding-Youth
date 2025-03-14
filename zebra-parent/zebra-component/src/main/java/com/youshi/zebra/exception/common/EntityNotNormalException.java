package com.youshi.zebra.exception.common;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 实体状态异常
 * 
 * Date: Jun 2, 2016
 * 
 * @author wangsch
 *
 */
@SuppressWarnings("serial")
public class EntityNotNormalException extends DoradoMetaCodeException {

	public EntityNotNormalException() {
	}

	public EntityNotNormalException(String message) {
		super(message);
	}

	public EntityNotNormalException(Throwable cause) {
		super(cause);
	}

	public EntityNotNormalException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotNormalException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/* (non-Javadoc)
	 * @see com.dorado.exception.DoradoMetaCodeException#getMetaCode()
	 */
	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.EntityNotNormal;
	}

}
