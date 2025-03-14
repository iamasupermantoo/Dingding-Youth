package com.youshi.zebra.view;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.TryCourseModel;
import com.youshi.zebra.user.model.UserModel;


public class TryCourseView {
	private TryCourseModel delegate;
	
	private UserModel user;
	
	private ZebraBuildContext context;
	
	public TryCourseView(TryCourseModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		this.user = context.getUser(delegate.getStudentId());
	}
	
	public String getCid() {
		int courseId = delegate.getCourseId();
		if(courseId == 0) {
			return null;
		}
		return UuidUtils.getUuid(CourseModel.class, courseId);
	}
	
	public String getCmId() {
		return UuidUtils.getUuid(CourseMetaModel.class, delegate.getCmId());
	}
	
	public StudentInfoView getStudent() {
		return new StudentInfoView(user, context);
	}
	
	public Integer getChatResult() {
		return delegate.getChatResult();
	}
	
	public long getCreateTime() {
		return delegate.getCreateTime();
	}
	
	public boolean getPaid() {
		return delegate.getPaid();
	}
	
	public boolean getPlanned() {
		return delegate.getPlanned();	
	}
	
}
