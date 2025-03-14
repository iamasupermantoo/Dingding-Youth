package com.youshi.zebra.exception.common;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 操作不允许，如：操作了其他人数据/权限不够等
 * 
 * @author wangsch
 * @date 2016-09-12
 */
@SuppressWarnings("serial")
public class ForbiddenException extends DoradoMetaCodeException {

	public ForbiddenException() {
	}

	public ForbiddenException(String message) {
		super(message);
	}

	public ForbiddenException(Throwable cause) {
		super(cause);
	}

	public ForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}

	public ForbiddenException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/* (non-Javadoc)
	 * @see com.dorado.exception.DoradoMetaCodeException#getMetaCode()
	 */
	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.Forbidden;
	}

}
