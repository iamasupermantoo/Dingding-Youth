package com.youshi.zebra.live.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 直播课堂，lesson状态不正常时抛出
 * 
 * @author wangsch
 * @date 2017年3月20日
 */
@SuppressWarnings("serial")
public class LiveLessonStatusInvalidException extends DoradoMetaCodeException {

	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.LiveLessonStatusInvalid;
	}
}