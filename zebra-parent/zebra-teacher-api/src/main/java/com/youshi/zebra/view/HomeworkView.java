package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.homework.model.HomeworkModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.user.model.UserModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 作业列表中，代表一条作业
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@ApiModel(value = "作业在列表中view")
public class HomeworkView {
	private HomeworkModel delegate;
	private LessonModel lesson;
	private CourseModel course;
	private UserModel student;
	private UserModel teacher;
	private ZebraBuildContext context;

	public HomeworkView(HomeworkModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		
		this.lesson = context.getLesson(delegate.getLessonId());
		this.course = context.getCourse(lesson.getCourseId());
		this.student = context.getUser(delegate.getStudentId());
		this.teacher = context.getUser(delegate.getTeacherId());
	}
	
	@ApiModelProperty(value = "课程图片")
	public ImageView getCourseImage() {
		return new ImageView(context.getImage(course.getImageId()));
	}
	
	public int getCnt() {
		return lesson.getCnt();
	}
	
	public String getHid() {
		return delegate.getUuid();
	}
	
	public String getCourse() {
		return course.getCourse();
	}
	
	@ApiModelProperty(value = "学生name")
	public String getStudent() {
		return student.getName();
	}
	
	@ApiModelProperty(value = "老师name")
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
