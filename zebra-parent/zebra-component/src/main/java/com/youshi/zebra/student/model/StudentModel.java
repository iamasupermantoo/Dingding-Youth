package com.youshi.zebra.student.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;
import com.youshi.zebra.user.model.UserModel;

/**
 * 代表一个学生，id来源于和{@link UserModel}的id
 * 
 * @author wangsch
 * @date 2017年2月13日
 */
public class StudentModel extends AbstractModel<Integer>{
	
	public enum StudentKeys {
		id,
		name,
		status,
		gender,
		head_image_id,
	}
	
	public StudentModel(Integer id, String data, long createTime, int status) {
		super(id, data, createTime, status);
	}
	
	public String getName() {
		return ModelUtils.getString(this, StudentKeys.name);
	}

	public Integer getHeadImageId() {
		return ModelUtils.getInt(this, StudentKeys.head_image_id);
	}
	
	public Integer getGender() {
		return ModelUtils.getInt(this, StudentKeys.gender);
	}
}
