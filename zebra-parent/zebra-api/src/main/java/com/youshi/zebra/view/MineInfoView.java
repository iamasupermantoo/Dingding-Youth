package com.youshi.zebra.view;

import com.dorado.framework.crud.model.util.ModelUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.model.UserModel.StudentKey;

import io.swagger.annotations.ApiModelProperty;

/**
 * 我的个人资料
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public class MineInfoView {
	private UserModel delegate;
	
	private ZebraBuildContext context;
	
	public MineInfoView(UserModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}
	
	public String getName() {
		return delegate.getName();
	}
	
	public ImageView getHeadImage() {
		return new ImageView(context.getImage(delegate.getHeadImageId()));
	}
	
	@ApiModelProperty(value = "年级", required=true)
	public String getGrade() {
		return ModelUtils.getString(delegate, StudentKey.grade);
	}
	
	public Integer getGender() {
		return delegate.getGender();
	}
	
	public String getProvince() {
		return delegate.getProvince();
	}
	
	public String getRegion() {
		return delegate.getRegion();
	}
	
	public String getSignature() {
		return delegate.getSignature();
	}
	public String getBirthday() {
		return delegate.getBirthday();
	}
}
