package com.youshi.zebra.mobile.exception.code;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;


/**
 * 验证码输入错误
 * 
 * @author wangsch
 * @date 2016年12月24日
 */
public class MobileCodeWrongException extends DoradoMetaCodeException {
 
    private static final long serialVersionUID = -7518991370698278319L;
    
    /* (non-Javadoc)
     * @see com.dorado.exception.DoradoMetaCodeException#getMetaCode()
     */
    @Override
    public MetaCode getMetaCode() {
    	return ZebraMetaCode.MobileCodeWrong;
    }
 
}
