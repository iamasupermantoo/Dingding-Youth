package com.youshi.zebra.account.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author codegen
 */
public class AccountChargeModel extends AbstractModel<Integer>{
	public enum AccountChargeKeys {
		// db 字段
		user_id,
		charge_amount,
	
		// data字段
	}
	
	private int userId;
	private int chargeAmount;
	
	public AccountChargeModel(
			int id, String data, long createTime, int status,
			int userId,
			int chargeAmount
			) {
		super(id, data, createTime, status);
		this.userId = userId;
		this.chargeAmount = chargeAmount;
	}

	public int getUserId() {
		return userId;
	}
	public int getChargeAmount() {
		return chargeAmount;
	}
	
}
