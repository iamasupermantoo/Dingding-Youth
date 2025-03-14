package com.youshi.zebra.lesson.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 
 * lesson的时间范围，格式不正确或不符合要求
 * 
 * @author wangsch
 * @date 2017年2月17日
 */
@SuppressWarnings("serial")
public class LessonTimePeriodException extends DoradoMetaCodeException {
	
	public LessonTimePeriodException() {
	}
	
	public LessonTimePeriodException(String message) {
		super(message);
	}
	
	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.LessonTimePeriodInvalid;
	}

}
