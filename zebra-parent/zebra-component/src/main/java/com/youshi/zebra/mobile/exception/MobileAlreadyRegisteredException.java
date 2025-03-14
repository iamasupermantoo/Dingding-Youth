/**
 * 
 */
package com.youshi.zebra.mobile.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;


/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class MobileAlreadyRegisteredException extends DoradoMetaCodeException {
 
    private static final long serialVersionUID = -7518991370698278319L;

	/* (non-Javadoc)
	 * @see com.dorado.exception.DoradoMetaCodeException#getMetaCode()
	 */
	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.MobileAlreadyRegistered;
	}
    
 
}
