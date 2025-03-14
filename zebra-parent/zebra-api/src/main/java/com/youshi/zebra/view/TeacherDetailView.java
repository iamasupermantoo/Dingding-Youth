package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.teacher.model.TeacherModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月13日
 */
public class TeacherDetailView {
	private TeacherModel delegate;
	
	private ZebraBuildContext context;
	
	public TeacherDetailView(TeacherModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}
	
	public String getTid() {
		return delegate.getUuid();
	}
	
	public String getName() {
		return delegate.getName();
	}
	
	public ImageView getImage() {
		Integer imageId = delegate.getImageId();
		if(imageId==null) {
			return null;
		}
		ImageView imageView = new ImageView(context.getImage(imageId));
		return imageView;
	}
	
	public Integer getGender() {
		return delegate.getGender();
	}
	
	public Integer getHours() {
		return delegate.getHours();
	}
	
	public String getDesc() {
		return delegate.getDesc();
	}
	
	public String getAge() {
		return delegate.getAge();
	}
	
	
}
