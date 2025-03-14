package com.youshi.zebra.teacher.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;
import com.youshi.zebra.user.model.UserModel;

/**
 * 代表一个老师，id来源于和{@link UserModel}的id
 * 
 * @author wangsch
 * @date 2017年2月13日
 */
public class TeacherModel extends AbstractModel<Integer>{
	
	public enum TeacherKeys {
		id,
		status,
		name,
		gender,
		desc,
		head_image_id,
		age,
		image_id
	}
	
	public TeacherModel(Integer id, String data, long createTime, int status) {
		super(id, data, createTime, status);
	}
	
	public String getName() {
		return ModelUtils.getString(this, TeacherKeys.name);
	}

	public Integer getHeadImageId() {
		return ModelUtils.getInt(this, TeacherKeys.head_image_id);
	}
	
	public Integer getGender() {
		return ModelUtils.getInt(this, TeacherKeys.gender);
	}

	public String getDesc() {
		return ModelUtils.getString(this, TeacherKeys.desc);
	}
	
	// FIXME 外部注入
	private int hours = 187;
	
	public int getHours() {
		return hours;
	}
	
	public void setHours(int hours) {
		this.hours = hours;
	}
	
	public String getAge() {
		return ModelUtils.getString(this, TeacherKeys.age);
	}
	
	public Integer getImageId() {
		return ModelUtils.getInt(this, TeacherKeys.image_id);
	}
	
}
