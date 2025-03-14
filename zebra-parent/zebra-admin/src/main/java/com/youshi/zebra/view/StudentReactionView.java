package com.youshi.zebra.view;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.reaction.model.StudentReactionModel;
import com.youshi.zebra.user.model.UserModel;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
public class StudentReactionView {
		private StudentReactionModel delegate;
		private CourseModel course;
		private UserModel student;
		private UserModel teacher;
		private ZebraBuildContext context;
		
		public StudentReactionView(StudentReactionModel delegate, ZebraBuildContext context) {
			this.delegate = delegate;
			this.context = context;
			this.course = context.getCourse(delegate.getCourseId());
			this.student = context.getUser(delegate.getStudentId());
			this.teacher = context.getUser(delegate.getTeacherId());
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
		
		public Integer getStatus() {
			return delegate.getStatus();
		}
		
		public String getTeacher() {
			return teacher.getName();
		}
		
		public String getStudent() {
			return student.getName();
		}
		
		public String getDate() {
			return delegate.getDate();
		}
		
		public String getTime() {
			return delegate.getTime();
		}
	}