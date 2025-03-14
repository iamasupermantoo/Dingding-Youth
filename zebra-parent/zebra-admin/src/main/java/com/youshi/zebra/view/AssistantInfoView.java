package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.user.model.UserModel;

/**
 * 助教信息view，之后所有地方的助教信息都统一用这个类即可
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
public class AssistantInfoView {
    private final UserModel model;

    private final ZebraBuildContext buildContext;
 
    public AssistantInfoView(UserModel model, ZebraBuildContext buildContext) {
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
