package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.reaction.model.TeacherReactionModel;
import com.youshi.zebra.user.model.UserModel;

public class TeacherReactionDetailsView {
	private TeacherReactionModel delegate;
	private UserModel student;
	private UserModel teacher;
	private ZebraBuildContext context;

	public TeacherReactionDetailsView(TeacherReactionModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		this.student = context.getUser(delegate.getStudentId());
		this.teacher = context.getUser(delegate.getTeacherId());
	}

	public String getDate() {
		return delegate.getDate();
	}

	public String getTime() {
		return delegate.getTime();
	}

	public String getStudent() {
		return student.getName();
	}
	
	public String getTeacher() {
		return teacher.getName();
	}

	public ImageView getStudentHeadImg() {
		return new ImageView(context.getImage(student.getHeadImageId()));
	}

	public String getCourse() {
		CourseModel course = context.getCourse(delegate.getCourseId());
		return course.getCourse();
	}

	public Integer getIdea() {
		return delegate.getIdea();
	}

	public Integer getBehavior() {
		return delegate.getBehavior();
	}

	public String getRemark() {
		return delegate.getRemark();
	}

	public Integer getStar() {
		return delegate.getStar();
	}

}