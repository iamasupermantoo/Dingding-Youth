package com.youshi.zebra.passport.model;

/**
 * 
 * @author wangsch
 * @date 2017年2月20日
 */
public class TicketModel {
	private int id;
	private int userId;
	private byte[] secret;
	private String randoms;
	private long createTime;
	
	public TicketModel(int id, int userId, byte[] secret, String randoms, long createTime) {
		this.id = id;
		this.userId = userId;
		this.secret = secret;
		this.randoms = randoms;
		this.createTime = createTime;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public byte[] getSecret() {
		return secret;
	}
	public void setSecret(byte[] secret) {
		this.secret = secret;
	}
	public String getRandoms() {
		return randoms;
	}
	public void setRands(String randoms) {
		this.randoms = randoms;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
