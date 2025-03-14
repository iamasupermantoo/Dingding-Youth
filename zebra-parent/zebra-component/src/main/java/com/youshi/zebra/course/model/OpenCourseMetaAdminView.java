package com.youshi.zebra.course.model;


import java.util.Date;
import java.util.List;

import com.youshi.zebra.core.utils.DateUtil;
import com.youshi.zebra.image.model.ImageView;
import com.youshi.zebra.user.model.UserModel;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
public class OpenCourseMetaAdminView {
	private LiveMetaModel delegate;
	
	private ImageView image;
	
	private UserModel user;
	
	public OpenCourseMetaAdminView(LiveMetaModel delegate, ImageView image) {
		super();
		this.delegate = delegate;
		this.image = image;
	}

	public void setImage(ImageView image) {
		this.image = image;
	}
	
	public Integer getCmId() {
		return delegate.getId();
	}
	public String getName() {
		return delegate.getName();
	}
	
	public String getDesc() {
		return delegate.getDesc();
	}
	
	public String getSubNotes() {
		List<String> subNotes = delegate.getSubNotes();
		StringBuffer sb = new StringBuffer();
		for (String string : subNotes) {
			sb.append(string);
		}
		return sb.toString();
	}
	
	public String getSuitableCrowds() {
		return delegate.getSuitableCrowds();
	}
	
	public Integer getStatus() {
		return delegate.getStatus();
	}
	
	public ImageView getImage() {	
		return image;
	}
	
	public String getPrice() {
		return delegate.getPrice();
	}
	
	public Integer getLevel() {
		return delegate.getLevel();
	}
	
	public Integer getType() {
		return delegate.getType();
	}
	
	public long getCreateTime() {
		return delegate.getCreateTime();
	}
	
	public String getOpenTime() {
		Date parse = DateUtil.parse(delegate.getOpenTime(), "yyyy-MM-dd hh:mm:ss.S");
		String format = DateUtil.format(parse, "yyyy-MM-dd hh:mm");
		return format;
	}
	
	public Integer getJoinCnt() {
		return delegate.getJoinCount();
	}
	
	public String getShareBrief() {
		return delegate.getShareBrief();
	}
	
	
	public String getShareJumpUrl() {
		return delegate.getShareJumpUrl();
	}


	public void setUser(UserModel user) {
		this.user = user;
	}
	
	public String getUserName() {
		return user.getName();
	}
	
	public String getMobile() {
		return user.getMobile();
	}
	
	public Integer getTeacherId() {
		return user.getId();
	}
	
}
