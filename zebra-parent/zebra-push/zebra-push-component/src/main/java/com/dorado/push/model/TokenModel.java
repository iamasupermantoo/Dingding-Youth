package com.dorado.push.model;

import java.util.Date;

import com.dorado.framework.crud.model.HasId;


public class TokenModel implements HasId<Integer> {
	private int id;

	private String token;

	private Integer userId;

	private Date createTime;

	public TokenModel(int id, Integer userId, String token, Date creteTime) {
		this.id = id;
		this.token = token;
		this.userId = userId;
		this.createTime = creteTime;
	}

	public String getToken() {
		return token;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
