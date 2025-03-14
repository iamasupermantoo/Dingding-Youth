package com.youshi.zebra.config.model;

import java.util.List;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 用户配置
 * 
 * @author wangsch
 * @date 2016-10-20
 */
public class UserConfigModel extends AbstractModel<Integer> {
	public enum UserConfigKeys {
		kv
	}
	
	private int userId;
	
	public UserConfigModel() {
	}

	public UserConfigModel(Integer id, String data, long createTime, int status, int userId) {
		super(id, data, createTime, status);
		this.userId = userId;
	}
	
	public int getUserId() {
		return userId;
	}
}
