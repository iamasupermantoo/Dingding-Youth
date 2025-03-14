package com.youshi.zebra.passport;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 
 * 修改密码失败
 * 
 * @author wangsch
 * @date 2017年2月9日
 */
public class OldPasswordWrongException extends DoradoMetaCodeException {

	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.OldPasswordWrong;
	}

}
