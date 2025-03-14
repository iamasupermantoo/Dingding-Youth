package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseMetaModel;

/**
 * 
 * 
 * 课程详细信息View
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public class CourseMetaDetailsView {
	private CourseMetaModel delegate;
	
	private ZebraBuildContext context;
	
	public CourseMetaDetailsView(CourseMetaModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}
	
	public String getCmId() {
		return delegate.getUuid();
	}
	public String getName() {
		return delegate.getName();
	}
	
	public String getDesc() {
		return delegate.getDesc();
	}
	
	public ImageView getImage() {
		return new ImageView(context.getImage(delegate.getImageId()));
	}
	
	public Integer getTotalCnt() {
		return delegate.getTotalCnt();
	}
	
	public String getPrice() {
		return delegate.getPrice();
	}
	
	public Integer getLevel() {
		return delegate.getLevel();
	}
	
	
}
