package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.user.model.UserModel;

/**
 * 老师信息view，之后所有地方的老师信息都统一用这个类即可
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
public class TeacherInfoView {
    private final UserModel model;

    private final ZebraBuildContext buildContext;
 
    public TeacherInfoView(UserModel model, ZebraBuildContext buildContext) {
        this.model = model;
        this.buildContext = buildContext;
    }
    
    
    public Integer getTid() {
    	return model.getId();
    }
    
    public ImageModel getHeadImage() {
		return buildContext.getImage(model.getHeadImageId());
	}

	public String getName() {
		return model.getName();
	}
	
	public String getMobile() {
		return model.getMobile();
	}
	
	public Integer getType() {
		return model.getType();
	}
	
	public Integer getGender() {
		return model.getGender();
	}
	
	public String getSignature() {
		return model.getSignature();
	}
	
	public long getCreateTime() {
		return model.getCreateTime();
	}
	
	public String getBirthday() {
		return model.getBirthday();
	}
	
	public String getDesc() {
		return model.getDesc();
	}
	
}
