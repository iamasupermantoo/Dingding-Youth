package com.youshi.zebra.view;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.CourseStatusModel;
import com.youshi.zebra.user.model.UserModel;

/**
 * 运营后台查询课程详细信息
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
public class CourseDetailsView {
	private CourseModel delegate;
	
	private CourseStatusModel courseStatus;
	
	private ZebraBuildContext context;
	
	public CourseDetailsView(CourseModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		this.courseStatus = context.getCourseStatus(delegate.getId());
	}
	
	public Integer getCid() {
		return delegate.getId();
	}
	
	public String getCmId() {
		return UuidUtils.getUuid(CourseMetaModel.class, delegate.getCmId());
	}
	
	public ImageView getImage() {
		return new ImageView(context.getImage(delegate.getImageId()));
	}
	
	public String getName() {
		return delegate.getCourse();
	}
	
	public Integer getStatus() {
		return delegate.getStatus();
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
	
	public String getStudent() {
		UserModel student = context.getUser(delegate.getStudentId());
		return student.getName();
	}
	
	public long getCreateTime() {
		return delegate.getCreateTime();
	}
}
