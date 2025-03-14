package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.user.model.UserModel;

/**
 * 
 * @author wangsch
 * @date 2017年1月11日
 */
public class UserInfoView {
    private final UserModel model;

    private final ZebraBuildContext buildContext;
 
    public UserInfoView(UserModel model, ZebraBuildContext buildContext) {
        this.model = model;
        this.buildContext = buildContext;
    }
    
    public String getId() {
    	return model.getUuid();
    }
    
    public ImageView getHeadImage() {
		return new ImageView(buildContext.getImage(model.getHeadImageId()));
	}

	public String getName() {
		return model.getName();
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
}
