package com.youshi.zebra.stats.model;

import com.dorado.framework.crud.model.impl.AbstractModel;

/**
 * 
 * @author codegen
 */
public class UserActByDayModel extends AbstractModel<Integer>{
	public enum UserActByDayKeys {
		// db 字段
		normal_user_count,
		pay_user_count,
		day,
	
		// data字段
	}
	
	private Integer normalUserCount;
	private Integer payUserCount;
	private String day;
	
	public UserActByDayModel(
			int id, long createTime,
			Integer normalUserCount,
			Integer payUserCount,
			String day
			) {
		this.id = id;
		this.createTime = createTime;
		this.normalUserCount = normalUserCount;
		this.payUserCount = payUserCount;
		this.day = day;
	}

	public Integer getNormalUserCount() {
		return normalUserCount;
	}
	public Integer getPayUserCount() {
		return payUserCount;
	}
	public String getDay() {
		return day;
	}
}
