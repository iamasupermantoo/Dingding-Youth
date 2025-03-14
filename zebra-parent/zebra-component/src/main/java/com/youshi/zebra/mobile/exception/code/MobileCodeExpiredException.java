package com.youshi.zebra.mobile.exception.code;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 验证码已过期
 * 
 * @author wangsch
 * @date 2016年12月24日
 */
public class MobileCodeExpiredException extends DoradoMetaCodeException {
	 
    private static final long serialVersionUID = -5913504866595571355L;
    
    /* (non-Javadoc)
     * @see com.dorado.exception.DoradoMetaCodeException#getMetaCode()
     */
    @Override
    public MetaCode getMetaCode() {
    	return ZebraMetaCode.MobileCodeExpired;
    }
}
