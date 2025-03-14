package com.youshi.zebra.account.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author codegen
 */
public class AccountModel extends AbstractModel<Integer>{
	public enum AccountKeys {
		// db 字段
		user_id,
		total_amount,
		update_time,
	
		// data字段
	}
	
	private int userId;
	private int totalAmount;
	private long updateTime;
	
	public AccountModel(
			int id, String data, long createTime, int status,
			int userId,
			int totalAmount,
			long updateTime
			) {
		super(id, data, createTime, status);
		this.userId = userId;
		this.totalAmount = totalAmount;
		this.updateTime = updateTime;
	}

	public int getUserId() {
		return userId;
	}
	public int getTotalAmount() {
		return totalAmount;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	
}
