package com.youshi.zebra.homework.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 
 * 
 * 作业状态不对，不允许编辑答案
 * 
 * 
 * @author wangsch
 * @date 2017年2月9日
 */
@SuppressWarnings("serial")
public class HomeworkAnswerModifyFailException extends DoradoMetaCodeException {

	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.HomeworkAnswerModifyFail;
	}

}
