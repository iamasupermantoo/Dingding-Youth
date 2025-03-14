package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseMetaModel;

import io.swagger.annotations.ApiModel;

/**
 * 约课列表, 一节课的显示
 * 
 * @author wangsch
 * @date 2017年5月13日
 */
@ApiModel("约课列表中, 代表一个课程")
public class CourseMetaView {
	private CourseMetaModel delegate;
	
	private ZebraBuildContext context;
	
	public CourseMetaView(CourseMetaModel delegate, ZebraBuildContext context) {
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
		Integer bigImageId = delegate.getBigImageId();
		if(bigImageId != null) {
			return new ImageView(context.getImage(delegate.getBigImageId()));
		}
		
		return new ImageView(context.getImage(delegate.getImageId()));
	}
	
	public Integer getTotalCnt() {
		return delegate.getTotalCnt();
	}
	
	public Integer getLevel() {
		return delegate.getLevel();
	}
}
