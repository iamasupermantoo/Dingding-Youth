package com.youshi.zebra.scholarship.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author codegen
 */
public class ScholarshipModel extends AbstractModel<Integer>{
	public enum ScholarshipKeys {
		// db 字段
		user_id,
		total_amount,
		
		// data字段
		bank_user,
		bank_user_mobile,
		bank_name,
		bank_card_num,
	}
	
	private int userId;
	private int totalAmount;
	
	public ScholarshipModel(
			int id, String data, long createTime, int status,
			int userId, int totalAmount
			) {
		super(id, data, createTime, status);
		this.userId = userId;
		this.totalAmount = totalAmount;
	}

	public int getUserId() {
		return userId;
	}
	
	public int getTotalAmount() {
		return totalAmount;
	}
	
	public String getBankUser() {
		return ModelUtils.getString(this, ScholarshipKeys.bank_user);
	}
	public String getBankUserMobile() {
		return ModelUtils.getString(this, ScholarshipKeys.bank_user_mobile);
	}
	public String getBankName() {
		return ModelUtils.getString(this, ScholarshipKeys.bank_name);
	}
	public String getBankCardNum() {
		return ModelUtils.getString(this, ScholarshipKeys.bank_card_num);
	}
}
