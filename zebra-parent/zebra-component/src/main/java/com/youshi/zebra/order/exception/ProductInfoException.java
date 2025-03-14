package com.youshi.zebra.order.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年6月26日
 */
@SuppressWarnings("serial")
public class ProductInfoException extends DoradoMetaCodeException {
	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.ProductInfoInvalid;
	}
}
