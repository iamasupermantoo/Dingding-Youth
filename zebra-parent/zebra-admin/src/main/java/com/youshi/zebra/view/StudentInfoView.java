package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.user.model.UserModel;

/**
 * 学生信息view，之后所有地方的学生信息都统一用这个类即可
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
public class StudentInfoView {
    private final UserModel model;

    private final ZebraBuildContext buildContext;
 
    public StudentInfoView(UserModel model, ZebraBuildContext buildContext) {
        this.model = model;
        this.buildContext = buildContext;
    }
    
    public Integer getSid() {
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
}
