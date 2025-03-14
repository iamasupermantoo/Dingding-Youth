package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.homework.model.HomeworkModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.user.model.UserModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 作业详情，头部信息
 * 
 * @author wangsch
 * @date 2017年2月6日
 */
@ApiModel(value = "作业详情")
public class HomeworkDetailsView {
	private HomeworkModel delegate;
	private LessonModel lesson;
	private CourseModel course;
	private UserModel teacher;
	private UserModel student;
	
	public HomeworkDetailsView(HomeworkModel delegate, ZebraBuildContext context) {
		if(delegate == null) {
			return;
		}
		this.delegate = delegate;
		
		this.lesson = context.getLesson(delegate.getLessonId());
		this.course = context.getCourse(lesson.getCourseId());
		this.teacher = context.getUser(delegate.getTeacherId());
		this.student = context.getUser(delegate.getStudentId());
	}

	@ApiModelProperty(value = "作业id")
	public String getHid() {
		return delegate.getUuid();
	}
	
	public String getCourse() {
		return course.getCourse();
	}
	
	@ApiModelProperty(value = "老师name")
	public String getTeacher() {
		return teacher.getName();
	}
	
	@ApiModelProperty(value = "学生name")
	public String getStudent() {
		return student.getName();
	}
	
	public String getDate() {
		return delegate.getDate();
	}
	
	public String getTime() {
		return delegate.getTime();
	}
	
	public String getTitle() {
		return delegate.getTitle();
	}
	
	@ApiModelProperty(value = "作业内容")
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
