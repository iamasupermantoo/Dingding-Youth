package com.youshi.zebra.view;

import java.util.HashMap;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.model.PageView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 
 * 课程列表，每个课程用这个view展示
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@ApiModel(value = "一个课程，在列表中展示")
public class CourseView {
	private CourseModel delegate;
	
	private ZebraBuildContext context;
	
	public CourseView(CourseModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}
	
	@JsonIgnore
	@ApiIgnore
	public Integer getId() {
		return delegate.getId();
	}
	
	@ApiModelProperty(value = "课程id")
	public String getCid() {
		return delegate.getUuid();
	}
	
	@ApiModelProperty(value = "图")
	public ImageView getImage() {
		return new ImageView(context.getImage(delegate.getImageId()));
	}
	
	@ApiModelProperty(value = "课程名")
	public String getName() {
		return delegate.getCourse();
	}
	
	@ApiModelProperty(value = "总课时")
	public Integer getTotalCnt() {
		return delegate.getTotalCnt();
	}
	
	private PageView<LessonView, String> lessons;
	public void setLessons(PageView<LessonView, String> lessons) {
		this.lessons = lessons;
	}
	
	@ApiModelProperty(value = "前几条lesson，分页结构")
	public PageView<LessonView, String> getLessons() {
		return lessons;
	}
}
