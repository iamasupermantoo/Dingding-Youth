package com.youshi.zebra.scholarship.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author codegen
 */
public class ScholarshipRetainRecordModel extends AbstractModel<Integer>{
	public enum ScholarshipRetainRecordKeys {
		// db 字段
		user_id,
		status,
	
		// data字段
		apply_amount,
	}
	
	private int userId;
	
	public ScholarshipRetainRecordModel(
			int id, String data, long createTime, int status,
			int userId
			) {
		super(id, data, createTime, status);
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}
	
	public Integer getApplyAmount() {
		return ModelUtils.getInt(this, ScholarshipRetainRecordKeys.apply_amount);
	}
	
	private ScholarshipModel scholarship;
	public void setScholarship(ScholarshipModel scholarship) {
		this.scholarship = scholarship;
	}
	
	public ScholarshipModel getScholarship() {
		return scholarship;
	}
	
}
