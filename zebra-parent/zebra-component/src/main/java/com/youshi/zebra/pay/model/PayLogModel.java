package com.youshi.zebra.pay.model;

import com.dorado.framework.crud.model.impl.AbstractModel;

/**
 * 
 * 代表一条支付日志
 * 
 * @author wangsch
 * @date 2017年4月18日
 */
public class PayLogModel extends AbstractModel<Integer>{
	private String orderSn;
	
	private int payChannel;
	
	private int platform;
	
	private int payStatus;
	
	// TODO + 更新来源，是verify还是notify更新的payStatus
	
	public PayLogModel(int id, String data, long createTime,
			String orderSn, int payStatus, int payChannel, int platform, long updateTime) {
		this.id = id;
		this.data = data;
		this.createTime = createTime;
		this.orderSn = orderSn;
		this.payStatus = payStatus;
		this.payChannel = payChannel;
		this.platform = platform;
	}

	public String getOrderSn() {
		return orderSn;
	}
	
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	
	public int getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(int payChannel) {
		this.payChannel = payChannel;
	}

	public int getPlatform() {
		return platform;
	}
	
	public void setPlatform(int platform) {
		this.platform = platform;
	}
	
	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public int getStatus() {
		throw new UnsupportedOperationException();
	}

	public void setStatus(int status) {
		throw new UnsupportedOperationException();
	}
}
