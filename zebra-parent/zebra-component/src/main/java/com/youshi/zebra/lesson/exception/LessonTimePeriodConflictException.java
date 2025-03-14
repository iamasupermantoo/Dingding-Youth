package com.youshi.zebra.lesson.exception;

import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 
 * @author wangsch
 * @date 2017年2月17日
 */
@SuppressWarnings("serial")
public class LessonTimePeriodConflictException extends DoradoMetaCodeException {
	private MetaCode metaCode;
	
	public LessonTimePeriodConflictException(MetaCode metaCode) {
		this.metaCode = metaCode;
	}
	
	@Override
	public MetaCode getMetaCode() {
		return metaCode;
	}

}
