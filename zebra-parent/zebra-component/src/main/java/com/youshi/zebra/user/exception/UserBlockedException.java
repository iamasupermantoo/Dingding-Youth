package com.youshi.zebra.user.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 用户被封禁了
 * Date: Jun 2, 2016
 * 
 * @author wangsch
 *
 */
@SuppressWarnings("serial")
public class UserBlockedException extends DoradoMetaCodeException {

	/* (non-Javadoc)
	 * @see com.dorado.exception.DoradoMetaCodeException#getMetaCode()
	 */
	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.UserBlocked;
	}

}
