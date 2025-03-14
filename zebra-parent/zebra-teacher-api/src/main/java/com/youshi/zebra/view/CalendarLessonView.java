package com.youshi.zebra.view;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.user.model.UserModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月14日
 */
public class CalendarLessonView {
	private LessonModel delegate;
	private ZebraBuildContext context;
	private CourseModel course;
	private UserModel student;
	
	public CalendarLessonView (LessonModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		this.course = context.getCourse(delegate.getCourseId());
		
		this.student = context.getUser(delegate.getStudentId());
		
	}
	
	public String getLid() {
		return delegate.getUuid();
	}
	
	public String getCid() {
		return UuidUtils.getUuid(CourseModel.class, delegate.getCourseId());
	}
	
	public String getCourse() {
		CourseModel course = context.getCourse(delegate.getCourseId());
		return course.getCourse();
	}
	
	public Integer getCnt() {
		return delegate.getCnt();
	}
	
	public ImageView getCourseImage() {
		return new ImageView(context.getImage(course.getImageId()));
	}
	
	public Integer getTotalCnt() {
		return course.getTotalCnt();
	}
	
	public Integer getStatus() {
		return delegate.getStatus();
	}
	
	public String getStudent() {
		return student.getName();
	}
	
	public String getTime() {
		return delegate.getTime();
	}
	
	public Integer getDuration() {
		return 25;
	}
}
