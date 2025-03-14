package com.youshi.zebra.live.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 检查房间的信息某字段，检查失败时抛出
 * 
 * @author wangsch
 * @date 2017年3月20日
 */
@SuppressWarnings("serial")
public class RoomInfoFieldInvalidException extends DoradoMetaCodeException {
	
	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.RoomInfoFieldInvalid;
	}
	
	
}