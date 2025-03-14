package com.youshi.zebra.view;

import com.youshi.zebra.core.constants.config.IntConfigKey;
import com.youshi.zebra.core.constants.config.RawStringConfigKey;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "常量信息")
public class ConstantsView {
	@ApiModelProperty(value = "最小提现额度")
	public String getRetainMinAmount() {
		return IntConfigKey.RetainMinAmount.get() / 100 + "元";
	}
	
	@ApiModelProperty(value = "每次分享加多少奖学金")
	public String getSharePlusAmount() {
		return IntConfigKey.SharePlusAmount.get() / 100 + "";
	}
	
	@ApiModelProperty(value = "提现服务电话")
	public String getServicePhone() {
		return RawStringConfigKey.ServicePhone.get();
	}
}