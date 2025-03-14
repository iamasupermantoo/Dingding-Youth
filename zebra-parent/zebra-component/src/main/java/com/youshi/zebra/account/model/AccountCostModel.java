package com.youshi.zebra.account.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author codegen
 */
public class AccountCostModel extends AbstractModel<Integer>{
	public enum AccountCostKeys {
		// db 字段
		user_id,
		cost_amount,
	
		// data字段
	}
	
	private int userId;
	private int costAmount;
	
	public AccountCostModel(
			int id, String data, long createTime, int status,
			int userId,
			int costAmount
			) {
		super(id, data, createTime, status);
		this.userId = userId;
		this.costAmount = costAmount;
	}

	public int getUserId() {
		return userId;
	}
	public int getCostAmount() {
		return costAmount;
	}
	
}
