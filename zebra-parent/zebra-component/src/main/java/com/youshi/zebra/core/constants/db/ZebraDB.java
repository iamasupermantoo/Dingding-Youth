package com.youshi.zebra.core.constants.db;

/**
 * 
 * @author wangsch
 * @date 2017年1月5日
 */
public enum ZebraDB {
	mobile("mobile"),
	connect("connect"),
	
	/**
	 * 教学，包括：上课、作业、反馈等与教学相关的业务
	 */
	teaching("teaching"),
	
	order("order"),
	
	passport("passport"),
	image("image"),
	user("user"),
	
	/** 运营后台相关 */
	admin("admin"),
	
	misc("misc"),
	;
	private String name;
	
	ZebraDB(String name) {
		this.name= name;
	}
	
	public String getZKName() {
		return name;
	}
}
