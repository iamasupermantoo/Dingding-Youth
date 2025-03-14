package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.reaction.model.StudentReactionModel;
import com.youshi.zebra.user.model.UserModel;

public class StudentReactionDetailsView {
		private StudentReactionModel delegate;
		private UserModel teacher;
		private UserModel student;
		private ZebraBuildContext context;
		
		public StudentReactionDetailsView(StudentReactionModel delegate, ZebraBuildContext context) {
			this.delegate = delegate;
			this.context = context;
			this.teacher = context.getUser(delegate.getTeacherId());
			this.student = context.getUser(delegate.getStudentId());
		}
		
		public String getDate() {
			return delegate.getDate();
		}
		
		public String getTime() {
			return delegate.getTime();
		}
		
		public String getTeacher() {
			return teacher.getName();
		}
		
		public String getStudent() {
			return student.getName();
		}
		
		public ImageView getTeacherHeadImg() {
			return new ImageView(context.getImage(teacher.getHeadImageId()));
		}
		
		public String getCourse() {
			CourseModel course = context.getCourse(delegate.getCourseId());
			return course.getCourse();
		}
		
		public Integer getQuality() {
			return delegate.getQuality();
		}
		
		public Integer getAcceptance() {
			return delegate.getAcceptance();
		}
		
		public String getRemark() {
			return delegate.getRemark();
		}
		
		public Integer getStar() {
			return delegate.getStar();
		}
		
		
	}