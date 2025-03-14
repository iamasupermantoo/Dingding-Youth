package com.youshi.zebra.pay.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 
 * @author wangsch
 * @date 2017年4月19日
 */
@SuppressWarnings("serial")
public class OrderStatusInvalidException extends DoradoMetaCodeException {
	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.OrderStatusInvalid;
	}

}
