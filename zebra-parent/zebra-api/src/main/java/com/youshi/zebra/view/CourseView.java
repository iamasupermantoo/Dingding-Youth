package com.youshi.zebra.view;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;

/**
 * 
 * 
 * 课程列表，每个课程用这个view展示
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public class CourseView {
	private CourseModel delegate;
	
	private CourseMetaModel courseMeta;
	
	private ZebraBuildContext context;
	
	public CourseView(CourseModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		
		this.courseMeta = context.getCourseMeta(delegate.getCmId());
	}
	
	public String getCid() {
		return delegate.getUuid();
	}
	
	public String getCmId() {
		return UuidUtils.getUuid(CourseMetaModel.class, delegate.getCmId());
	}
	
	
	public ImageView getImage() {
		Integer bigImageId = courseMeta.getBigImageId();
		if(bigImageId != null) {
			return new ImageView(context.getImage(courseMeta.getBigImageId()));
		}
		
		return new ImageView(context.getImage(delegate.getImageId()));
	}
	
	public String getName() {
		return delegate.getCourse();
	}
	
	public Integer getTotalCnt() {
		return delegate.getTotalCnt();
	}
	
	
}
