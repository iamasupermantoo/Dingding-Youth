package com.youshi.zebra.view;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.live.service.LiveService;
import com.youshi.zebra.live.utils.LiveUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author wangsch
 * @date 2017年2月14日
 */
@ApiModel("一个lesson，在lesson列表中展示")
public class LessonView {
	private LessonModel delegate;
	private ZebraBuildContext context;
	public LessonView(LessonModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}
	
	public String getLid() {
		return delegate.getUuid();
	}
	public String getCid() {
		return UuidUtils.getUuid(CourseModel.class, delegate.getCourseId());
	}
	
	public String getLabel() {
		return delegate.getLabel();
	}
	
	@ApiModelProperty(value = "lesson状态：0: 待上课，1:正在上课，2:已上课")
	public Integer getStatus() {
		return delegate.getStatus();
	}
	
	public String getDate() {
		return delegate.getDate();
	}
	
	public String getTime() {
		return delegate.getTime();
	}
	
	@ApiModelProperty(value = "学生对老师评价，状态：0:待评价，1:已评价")
	public Integer getStudentReactionStatus() {
		return delegate.getStudentReactionStatus();
	}
	
	@ApiModelProperty(value = "老师对学生评价，状态：0:待评价，1:已评价")
	public Integer getTeacherReactionStatus() {
		return delegate.getTeacherReactionStatus();
	}
	
	@ApiModelProperty(value = "作业状态：0:待提交，1:待评分（等价于：已提交），2:已评分")
	public Integer getHomeworkStatus() {
		return delegate.getHomeworkStatus();
	}
	
	@ApiModelProperty(value = "视频回放地址。当status是“已上课”时，会有返回，但不保证一定返回")
	public String getVideoUrl() {
		String key = LiveService.videoOssKey(delegate.getCourseId(), delegate.getId());
		return LiveUtils.getReplayUrl(key);
	}
}
