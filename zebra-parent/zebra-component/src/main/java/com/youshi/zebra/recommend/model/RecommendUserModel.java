package com.youshi.zebra.recommend.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 首页推荐用户
 * 
 * @author wangsch
 * @date 2016-09-26
 */
public class RecommendUserModel extends AbstractModel<Integer>{
	public enum RecommendUserKeys {
		user_id,
		school,
		status
	}
	
	
	private int userId;
	
	public RecommendUserModel() {
		super();
	}

	public RecommendUserModel(Integer id, String data, long createTime, int status,
			int userId) {
		super(id, data, createTime, status);
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}
	
	/**
	 * 这个name是推荐的时候，运营录入的
	 */
	public String getSchoolName() {
		return ModelUtils.getString(this, RecommendUserKeys.school);
	}
}
