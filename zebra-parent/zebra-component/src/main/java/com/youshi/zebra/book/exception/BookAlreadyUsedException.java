package com.youshi.zebra.book.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 
 * @author wangsch
 * @date 2017年3月24日
 */
@SuppressWarnings("serial")
public class BookAlreadyUsedException extends DoradoMetaCodeException {

	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.BookAlreadyUsed;
	}

}
