package com.youshi.zebra.order.model;

import org.apache.commons.lang3.StringUtils;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;
import com.youshi.zebra.order.constants.OrderStatus;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.order.constants.ProductType;

/**
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
public class OrderModel extends AbstractModel<Integer>{
	public enum OrderKeys {
		user_id,
		order_sn,
		product_id,
		product_type,
		product_name,
		product_desc,
		
		/** 总价格，单位：分 */
		total_price,
		
		/** 充值数，单位：个，产品类型{@link ProductType#CHARGE } 时，不为0*/
		charge_amount,
		
		/** {@link OrderStatus} */
		order_status,
		
		/** {@link PayStatus} */
		pay_status,
		
		pay_channel,
		
		/** 更新时间 */
		update_time,
		
		// data 字段
		remark,
	}
	
	private int userId;
	
	private int productId;
	
	private int productType;
	
	private String orderSn;
	
	/**
	 * {@link PayStatus}
	 */
	private int payStatus;
	
	private int orderStatus;
	
	private Integer payChannel;
	
	private int chatResult;
	
	/**
	 * 订单价格
	 */
	private int totalPrice;
	
	private int chargeAmount;
	
	private Long updateTime;
	
	public OrderModel(Integer id, String data, long createTime, int orderStatus, 
			int userId, int productId, int productType, 
			String orderSn, int payStatus, String payChannel, int chatResult, int totalPrice, int chargeAmount, long updateTime) {
		this.id = id;
		this.status = orderStatus;
		this.data = data;
		this.createTime = createTime;
		this.userId = userId;
		this.productId = productId;
		this.productType = productType;
		this.orderSn = orderSn;
		this.orderStatus = orderStatus;
		this.payStatus = payStatus;
		if(StringUtils.isNotEmpty(payChannel)) {
			this.payChannel = Integer.parseInt(payChannel);
		}
		this.chatResult = chatResult;
		this.totalPrice = totalPrice;
		this.chargeAmount = chargeAmount;
		this.updateTime = updateTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public int getOrderStatus() {
		return orderStatus;
	}
	
	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getPayChannel() {
		return payChannel;
	}
	
	public void setPayChannel(Integer payChannel) {
		this.payChannel = payChannel;
	}
	
	public int getChatResult() {
		return chatResult;
	}
	
	public void setChatResult(int chatResult) {
		this.chatResult = chatResult;
	}
	
	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public int getChargeAmount() {
		return chargeAmount;
	}
	
	public void setChargeAmount(int chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	
	public int getProductId() {
		return productId;
	}
	
	public int getProductType() {
		return productType;
	}
	
	public Long getUpdateTime() {
		return updateTime;
	}
	
	public String getRemark() {
		return ModelUtils.getString(this, OrderKeys.remark);
	}
	
	public String getProductName() {
		return ModelUtils.getString(this, OrderKeys.product_name);
	}
	
	public String getProductDesc() {
		return ModelUtils.getString(this, OrderKeys.product_desc);
	}
}
