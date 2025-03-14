package com.youshi.zebra.stats.model;

import java.util.List;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.youshi.zebra.stats.utils.StatsUtils;

/**
 * 
 * @author codegen
 */
public class UserRetByDayModel extends AbstractModel<Integer>{
	public enum UserRetByDayKeys {
		// db 字段
		normal_user_count,
		pay_user_count,
		normal_user_rets,
		pay_user_rets,
		day,
	
		// data字段
	}
	
	private Integer normalUserCount;
	private Integer payUserCount;
	private String normalUserRets;
	private String payUserRets;
	private String day;
	
	public UserRetByDayModel(
			int id, long createTime,
			Integer normalUserCount,
			Integer payUserCount,
			String normalUserRets,
			String payUserRets,
			String day
			) {
		this.id = id;
		this.createTime = createTime;
		this.normalUserCount = normalUserCount;
		this.payUserCount = payUserCount;
		this.normalUserRets = normalUserRets;
		this.payUserRets = payUserRets;
		this.day = day;
	}

	public Integer getNormalUserCount() {
		return normalUserCount;
	}
	public Integer getPayUserCount() {
		return payUserCount;
	}
	public String getNormalUserRets() {
		return normalUserRets;
	}
	public String getPayUserRets() {
		return payUserRets;
	}
	public String getDay() {
		return day;
	}
	
	public List<String> getNormalRates() {
		return StatsUtils.parseRetRates(getNormalUserRets());
	}
	
	public List<String> getPayRates() {
		return StatsUtils.parseRetRates(getPayUserRets());
	}
}
