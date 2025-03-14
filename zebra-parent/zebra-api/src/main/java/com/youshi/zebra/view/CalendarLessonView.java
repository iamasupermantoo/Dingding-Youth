package com.youshi.zebra.view;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.lesson.model.LessonModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月14日
 */
public class CalendarLessonView {
	private LessonModel delegate;
	
	private ZebraBuildContext context;
	
	private CourseModel course;
	
	public CalendarLessonView (LessonModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		this.course = context.getCourse(delegate.getCourseId());
	}
	
	public String getLid() {
		return delegate.getUuid();
	}
	
	public String getCid() {
		return UuidUtils.getUuid(CourseModel.class, delegate.getCourseId());
	}
	
	public String getCourse() {
		return course.getCourse();
	}
	
	public Integer getCnt() {
		return delegate.getCnt();
	}
	
	public Integer getStatus() {
		return delegate.getStatus();
	}
	
	public ImageView getCourseImage() {
		return new ImageView(context.getImage(course.getImageId()));
	}
	
	public Integer getTotalCnt() {
		return course.getTotalCnt();
	}
	
	public String getTeacher() {
		return delegate.getTeacher();
	}
	
	public String getTime() {
		return delegate.getTime();
	}
}
