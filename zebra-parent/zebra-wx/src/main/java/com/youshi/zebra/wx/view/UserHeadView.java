package com.youshi.zebra.wx.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.user.model.UserModel;

import io.swagger.annotations.ApiModel;

/**
 * 
 * 用户头像view，通常在帖子、评论等地方，表示一个用户。
 * 
 * 包含头像、名字等最基本信息
 * 
 * @author wangsch
 * @date 2016-09-24
 */
@ApiModel("用户头像view")
public class UserHeadView {
	private UserModel model;
	private ZebraBuildContext buildContext;
	
	public UserHeadView (UserModel user, ZebraBuildContext buildContext) {
		this.model = user;
		this.buildContext = buildContext;
	}
	
    public ImageView getHeadImage() {
    	return new ImageView(buildContext.getImageMap().get(model.getHeadImageId()));
    }
    
    public Integer getType() {
		return model.getType();
	}
    
    public String getName() {
        return model.getName();
    }
    
    public String getId() {
        return model.getUuid();
    }
    
    public Integer getGender() {
		return model.getGender();
	}
	
}
