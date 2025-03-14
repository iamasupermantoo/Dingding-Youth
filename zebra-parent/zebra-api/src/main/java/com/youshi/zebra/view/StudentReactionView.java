package com.youshi.zebra.view;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.reaction.model.StudentReactionModel;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
public class StudentReactionView {
		private StudentReactionModel delegate;
		private CourseModel course;
		private ZebraBuildContext context;
		
		public StudentReactionView(StudentReactionModel delegate, ZebraBuildContext context) {
			this.delegate = delegate;
			this.context = context;
			course = context.getCourse(delegate.getCourseId());
		}
		
		public String getCid() {
			return UuidUtils.getUuid(CourseModel.class, delegate.getCourseId());
		}
		
		public String getLid() {
			return UuidUtils.getUuid(LessonModel.class, delegate.getLessonId());
		}
		
		public ImageView getImage() {
			return new ImageView(context.getImage(course.getImageId()));
		}
		
		public String getCourse() {
			return course.getCourse();
		}
		
		public Integer getCnt() {
			return delegate.getCnt();
		}
		
		public String getTeacher() {
			return delegate.getTeacher();
		}
		
		public String getDate() {
			return delegate.getDate();
		}
		
		public String getTime() {
			return delegate.getTime();
		}
	}