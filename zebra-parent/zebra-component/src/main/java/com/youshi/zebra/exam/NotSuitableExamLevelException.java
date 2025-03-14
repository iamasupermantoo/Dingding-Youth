package com.youshi.zebra.exam;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 
 * @author wangsch
 * @date 2017年8月24日
 */
public class NotSuitableExamLevelException extends DoradoMetaCodeException {

	
	public NotSuitableExamLevelException(String message) {
		super(message);
	}

	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.NoSuitableExam;
	}
	
}
