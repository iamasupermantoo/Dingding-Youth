package com.youshi.zebra.register.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public class ConnectBindedByOtherException extends DoradoMetaCodeException {

    private static final long serialVersionUID = -4572794581849879958L;
    
	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.ConnectBindedByOther;
	}

}
