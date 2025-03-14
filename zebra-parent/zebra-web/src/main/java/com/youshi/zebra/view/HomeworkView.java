package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.homework.model.HomeworkModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.user.model.UserModel;

/**
 * 作业列表中，代表一条作业
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public class HomeworkView {
	private HomeworkModel delegate;
	private LessonModel lesson;
	private CourseModel course;
	
	private UserModel teacher;

	public HomeworkView(HomeworkModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.lesson = context.getLesson(delegate.getLessonId());
		this.course = context.getCourse(lesson.getCourseId());
		
		this.teacher = context.getUser(delegate.getTeacherId());
	}

	public String getHid() {
		return delegate.getUuid();
	}
	
	public String getCourse() {
		return course.getCourse();
	}
	
	public String getLabel() {
		return lesson.getLabel();
	}
	
	public String getTeacher() {
		return teacher.getName();
	}
	
	public String getTitle() {
		return delegate.getTitle();
	}
	
	public Integer getStatus() {
		return delegate.getStatus();
	}
	
	public String getDate() {
		return delegate.getDate();
	}
	
	public String getTime() {
		return delegate.getTime();
	}
	
	
}
