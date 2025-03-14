package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseMetaModel;

/**
 * 老师开设的课程，简略信息展示
 * 
 * @author wangsch
 * @date 2017年2月13日
 */
public class CourseMetaLiteView {
	private CourseMetaModel delegate;
	
	private ZebraBuildContext context;
	
	public CourseMetaLiteView(CourseMetaModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}
	
	public String getName() {
		return delegate.getName();
	}
	
	public String getBrief() {
		return delegate.getName();
	}
	
	public ImageView getImage() {
		return new ImageView(context.getImage(delegate.getImageId()));
	}
	
	
	
}
