package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.homework.model.HomeworkModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.user.model.UserModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 作业详情，头部信息
 * 
 * @author wangsch
 * @date 2017年2月6日
 */
public class HomeworkDetailsView {
	private HomeworkModel delegate;
	private LessonModel lesson;
	private UserModel student;
	private UserModel teacher;
	private CourseModel course;

	public HomeworkDetailsView(HomeworkModel delegate, ZebraBuildContext context) {
		if(delegate == null) {
			return;
		}
		this.delegate = delegate;
		this.lesson = context.getLesson(delegate.getLessonId());
		this.student = context.getUser(delegate.getStudentId());
		this.teacher = context.getUser(delegate.getTeacherId());
		this.course = context.getCourse(lesson.getCourseId());
	}

	public String getHid() {
		return delegate.getUuid();
	}
	
	public String getCourse() {
		return course.getCourse();
	}
	
	public String getTeacher() {
		return teacher.getName();
	}
	
	public String getDate() {
		return delegate.getDate();
	}
	
	public String getTime() {
		return delegate.getTime();
	}
	
	// TODO
	@ApiModelProperty("批改时间")
	public String getCorrectDate() {
		return "2016-12-11";
	}
	
	public String getTitle() {
		return delegate.getTitle();
	}
	
	public String getContent() {
		return delegate.getContent();
	}
	
	public Integer getStatus() {
		return delegate.getStatus();
	}
	
	public Boolean getIntime() {
		return delegate.getIntime();
	}
	
	public Integer getQuality() {
		return delegate.getQuality();
	}
	
	public Integer getComplete() {
		return delegate.getComplete();
	}
	
	public String getRemark() {
		return delegate.getRemark();
	}
	
}
