package com.youshi.zebra.view;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.CourseStatusModel;
import com.youshi.zebra.image.model.ImageModel;

/**
 * 运营后台查询课程列表时，每个课程的展示
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
public class CourseView {
	private CourseModel delegate;
	
	private CourseStatusModel courseStatus;
	
	private ZebraBuildContext context;
	
	public CourseView(CourseModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		this.courseStatus = context.getCourseStatus(delegate.getId());
	}
	
	public String getCid() {
		return delegate.getUuid();
	}
	
	public String getCmId() {
		return UuidUtils.getUuid(CourseMetaModel.class, delegate.getCmId());
	}
	
	public ImageModel getImage() {
		return context.getImage(delegate.getImageId());
	}
	
	public String getName() {
		return delegate.getCourse();
	}
	
	public Integer getTotalCnt() {
		return delegate.getTotalCnt();
	}
	
	public int getPlanedMaxCnt() {
		return courseStatus.getPlanedMaxCnt();
	}
	
	public int getFinishedMaxCnt() {
		return courseStatus.getFinishedMaxCnt();
	}
	
	public StudentInfoView getStudent() {
		return new StudentInfoView(context.getUser(delegate.getStudentId()), context);
	}
	
	public long getCreateTime() {
		return delegate.getCreateTime();
	}
	
	
}
