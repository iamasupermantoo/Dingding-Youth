package com.youshi.zebra.connect.model;

import com.youshi.zebra.connect.constant.ConnectType;
import com.youshi.zebra.register.constant.ConnectBindStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author wangsch
 * @date 2017年2月8日
 */
@ApiModel
public class ConnectBindInfo {
	private ConnectType type;
	
	private String account;
	
	private ConnectBindStatus status;
	
	public ConnectBindInfo(ConnectType type, String account, ConnectBindStatus status) {
		this.type = type;
		this.account = account;
		this.status = status;
	}
	@ApiModelProperty(value = "第三方账号类型，1:qq，2:微信", required=true)
	public String getType() {
		return type.name();
	}
	public void setType(ConnectType type) {
		this.type = type;
	}
	@ApiModelProperty(value = "第三方账号名", required=true)
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
	@ApiModelProperty(value = "第三方账号绑定状态。1:已绑定，0|2:未绑定", required=true)
	public int getStatus() {
		return status.getValue();
	}
	public void setStatus(ConnectBindStatus status) {
		this.status = status;
	}
}
