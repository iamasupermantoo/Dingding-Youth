package com.youshi.zebra.mobile.exception.code;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

public class MobileCodeNotVerifyedException extends DoradoMetaCodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5232858330860150408L;

	/* (non-Javadoc)
	 * @see com.dorado.exception.DoradoMetaCodeException#getMetaCode()
	 */
	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.MobileCodeNotVerifyed;
	}
}
