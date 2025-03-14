package com.youshi.zebra.view;

import org.apache.commons.lang3.StringUtils;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.utils.LessonTimeUtils;
import com.youshi.zebra.user.model.UserModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
public class LessonPlanView {
	private LessonModel delegate;
	private String startTime;
	private String endTime;
	private ZebraBuildContext context;
	
	public LessonPlanView(LessonModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		if(StringUtils.isNotEmpty(delegate.getTime())) {
			String[] parts = LessonTimeUtils.split(delegate.getTime());
			startTime = parts[0];
			endTime = parts[1];
		}
	}
	
	public Integer getCid() {
//		return UuidUtils.getUuid(CourseModel.class, delegate.getCourseId());
		return delegate.getCourseId();
	}
	
	public Integer getLid() {
		return delegate.getId();
	}
	
	public String getCourse() {
		return delegate.getCourse();
	}
	
	public String getLabel() {
		return delegate.getLabel();
	}
	
	public Integer getCnt() {
		return delegate.getCnt();
	}
	
	public TeacherInfoView getTeacher() {
		UserModel teacher = context.getUser(delegate.getTeacherId());
		if(teacher != null) {
			return new TeacherInfoView(teacher, context);
		}
		
		return null;
	}
	
	public String getDate() {
		return delegate.getDate();
	}
	
	public String getTime() {
		return delegate.getTime();
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public Integer getStatus() {
		return delegate.getStatus();
	}
}
