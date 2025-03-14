package com.youshi.zebra.pay.model;

import com.dorado.framework.crud.model.impl.AbstractModel;

/**
 * 
 * @author wangsch
 * @date 2017年4月18日
 */
public class PayNotifyModel extends AbstractModel<Integer>{
	private String orderSn;
	
	private int payChannel;
	
	public PayNotifyModel(Integer id, String data, long createTime, 
			String orderSn, int payChannel
			) {
		this.id = id;
		this.data = data;
		this.createTime = createTime;
		this.orderSn = orderSn;
		this.payChannel = payChannel;
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
	
	@Override
	public int getStatus() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setStatus(int status) {
		throw new UnsupportedOperationException();
	}
	
}
