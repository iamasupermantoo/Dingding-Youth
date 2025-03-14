package com.youshi.zebra.view;

import org.apache.commons.lang3.StringUtils;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.utils.LessonTimeUtils;
import com.youshi.zebra.live.service.LiveService;
import com.youshi.zebra.live.utils.LiveUtils;
import com.youshi.zebra.user.model.UserModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月14日
 */
public class LessonView {
	private LessonModel delegate;
	private String startTime;
	private String endTime;
	private ZebraBuildContext context;
	
	public LessonView(LessonModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		if(StringUtils.isNotEmpty(delegate.getTime())) {
			String[] parts = LessonTimeUtils.split(delegate.getTime());
			startTime = parts[0];
			endTime = parts[1];
		}
	}
	
	public Integer getCid() {
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
	
	public StudentInfoView getStudent() {
		UserModel student = context.getUser(delegate.getStudentId());
		if(student != null) {
			return new StudentInfoView(student, context);
		}
		
		return null;
	}
	
	public TeacherInfoView getTeacher() {
		UserModel teacher = context.getUser(delegate.getTeacherId());
		if(teacher != null) {
			return new TeacherInfoView(teacher, context);
		}
		
		return null;
	}
	
	public Integer getTeacherOnlineStatus() {
		return delegate.getTeacherOnlineStatus();
	}
	
	public Integer getStudentOnlineStatus() {
		return delegate.getStudentOnlineStatus();
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
	
	public Boolean getVideoUploaded() {
		return delegate.getVideoUploaded();
	}
	
	public String getVideoUrl() {
		String key = LiveService.videoOssKey(delegate.getCourseId(), delegate.getId());
		return LiveUtils.getReplayUrl(key);
	}
}
