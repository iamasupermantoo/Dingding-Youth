package com.youshi.zebra.passport.model;

/**
 * @author wangsch
 * @date 2016年12月24日
 */
public class UserPassportModel {
	private int id;
	
	private String mobile;
	
	private String salt;
	
	private String password;
	
	public UserPassportModel(int id, String mobile, String salt, String password) {
		this.id = id;
		this.mobile = mobile;
		this.salt = salt;
		this.password = password;
	}

	public int getId() {
		return id;
	}
	
	public String getMobile() {
		return mobile;
	}
	
	public String getSalt() {
		return salt;
	}
	
	public String getPassword() {
		return password;
	}
}
