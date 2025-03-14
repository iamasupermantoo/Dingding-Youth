package com.youshi.zebra.pay.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

@SuppressWarnings("serial")
public class AlipayCallException extends DoradoMetaCodeException {
	private String reason;

	// other fields
	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.AlipayCallFail;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}