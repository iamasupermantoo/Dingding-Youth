package com.youshi.zebra.view;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.lesson.model.LessonModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author wangsch
 * @date 2017年2月14日
 */
@ApiModel(value = "点击某一天，返回当天要上的课。这个model，代表其中一节课")
public class CalendarLessonView {
	private LessonModel delegate;
	
	private ZebraBuildContext context;
	
	private CourseModel course;
	
	public CalendarLessonView (LessonModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		this.course = context.getCourse(delegate.getCourseId());
	}
	
	@ApiModelProperty(value = "lesson id")
	public String getLid() {
		return delegate.getUuid();
	}
	
	@ApiModelProperty(value = "课程id")
	public String getCid() {
		return UuidUtils.getUuid(CourseModel.class, delegate.getCourseId());
	}
	
	@ApiModelProperty(value = "课程名")
	public String getCourse() {
		return course.getCourse();
	}
	
	@ApiModelProperty(value = "课次")
	public Integer getCnt() {
		return delegate.getCnt();
	}
	
	@ApiModelProperty(value = "lesson状态：0: 待上课，1:正在上课，2:已上课")
	public Integer getStatus() {
		return delegate.getStatus();
	}
	
	@ApiModelProperty(value = "图")
	public ImageView getCourseImage() {
		return new ImageView(context.getImage(course.getImageId()));
	}
	
	@ApiModelProperty(value = "老师")
	public String getTeacher() {
		return delegate.getTeacher();
	}
	
	@ApiModelProperty(value = "时间")
	public String getTime() {
		return delegate.getTime();
	}
}
