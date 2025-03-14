package com.youshi.zebra.user.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;
import com.youshi.zebra.counts.model.UserCountsModel;

/**
 * 用户Model
 * 
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class UserModel extends AbstractModel<Integer> {
	public enum UserKey {
		id, 
		data,
		status, 
		create_time,
		name,
		signature,
		type,
//		verified,
		
		
		// data字段
		head_image_id,
		gender,
		mobile, 
		province,
		region, 
		exam_answers,
		exam_level,
		age,
		birthday,
		desc,
		image_id
		
	}
	
	public enum StudentKey {
		grade
	}

	
	private String name;
	
	private int type;
	
	private String signature;
	
	public UserModel() {
	}

	public UserModel(int id, String data, long createTime, int status,
			String name, int type, String signature, boolean verified) {
		super(id, data, createTime, status);
		this.name = name;
		this.type = type;
		this.signature = signature;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public String getSignature() {
		return signature;
	}

	public Integer getHeadImageId() {
		return ModelUtils.getInt(this, UserKey.head_image_id);
	}
	
	public Integer getGender() {
		return ModelUtils.getInt(this, UserKey.gender);
	}
	
	public String getMobile() {
		return ModelUtils.getString(this, UserKey.mobile);
	}
	
	public String getProvince() {
		return ModelUtils.getString(this, UserKey.province);
	}
	
	public String getRegion() {
		return ModelUtils.getString(this, UserKey.region);
	}
	
	public String getBirthday() {
		return ModelUtils.getString(this, UserKey.birthday);
	}
	

	
	private UserCountsModel counts;
	
	public UserCountsModel getCounts() {
		return counts;
	}
	
	public void setCounts(UserCountsModel counts) {
		this.counts = counts;
	}
	
	public Integer getImageId() {
		Integer imageId = ModelUtils.getInt(this, UserKey.image_id);
		return imageId!=null?imageId : getHeadImageId();
	}
	
	public Integer getAge() {
		Integer age = ModelUtils.getInt(this, UserKey.age);
		return age==null?0:age;
	}
	
	public String getDesc() {
		return ModelUtils.getString(this, UserKey.desc);
	}
	
}
