package com.youshi.zebra.mobile.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class MobileNotRegisteredException extends DoradoMetaCodeException {

    private static final long serialVersionUID = -4572794581849879958L;
    
    /* (non-Javadoc)
     * @see com.dorado.exception.DoradoMetaCodeException#getMetaCode()
     */
    @Override
    public MetaCode getMetaCode() {
    	return ZebraMetaCode.MobileNotRegistered;
    }

}
