package com.youshi.zebra.teacher.model;

import com.youshi.zebra.image.model.ImageView;
import com.youshi.zebra.user.model.UserModel;

public class TeacherView{
	
	private UserModel delegate;
	private ImageView imageView;

	public TeacherView(UserModel delegate, ImageView imageView) {
		super();
		this.delegate = delegate;
		this.imageView = imageView;
	}

	public String getTid() {
		return delegate.getUuid();
	}
	
	public String getName() {
		return delegate.getName();
	}
	
	public ImageView getImage() {
		return imageView;
	}
	
	public Integer getGender() {
		return delegate.getGender();
	}
	
	
	public String getDesc() {
		return delegate.getDesc();
	}
	
	public Integer getAge() {
		return delegate.getAge();
	}
	
}