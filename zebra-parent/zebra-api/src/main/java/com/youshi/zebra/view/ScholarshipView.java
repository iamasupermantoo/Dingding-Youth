package com.youshi.zebra.view;

import com.youshi.zebra.core.constants.config.IntConfigKey;
import com.youshi.zebra.core.constants.config.RawStringConfigKey;
import com.youshi.zebra.scholarship.model.ScholarshipModel;

import io.swagger.annotations.ApiModelProperty;

public class ScholarshipView {
	private ScholarshipModel delegate;

	public ScholarshipView(ScholarshipModel delegate) {
		this.delegate = delegate;
	}

	public String getTotalAmount() {
		if (delegate == null) {
			return "0元";
		}
		return delegate.getTotalAmount() / 100 + "元";
	}

	public String getRetainMinAmount() {
		return IntConfigKey.RetainMinAmount.get() / 100 + "元";
	}
	
	@ApiModelProperty(value = "每次分享加多少奖学金")
	public String getSharePlusAmount() {
		return IntConfigKey.SharePlusAmount.get() / 100 + "元";
	}

	public String getServicePhone() {
		return RawStringConfigKey.ServicePhone.get();
	}

	/**
	 * 是否填写了银行卡信息
	 */
	public boolean getHasBankInfo() {
		return getBankUser() != null;
	}

	public String getBankUser() {
		if (delegate == null) {
			return null;
		}
		return delegate.getBankUser();
	}

	public String getBankUserMobile() {
		if (delegate == null) {
			return null;
		}
		return delegate.getBankUserMobile();
	}

	public String getBankName() {
		if (delegate == null) {
			return null;
		}
		return delegate.getBankName();
	}

	public String getBankCardNum() {
		if (delegate == null) {
			return null;
		}
		return delegate.getBankCardNum();
	}
}