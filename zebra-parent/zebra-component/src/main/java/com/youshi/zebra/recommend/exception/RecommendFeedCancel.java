package com.youshi.zebra.recommend.exception;

import com.dorado.framework.crud.model.HasMetaCode.MetaCode;
import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 
 * @author wangsch
 * @date 2017年4月22日
 */
public class RecommendFeedCancel extends DoradoMetaCodeException {

	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.RecommendFeedCancel;
	}

}
