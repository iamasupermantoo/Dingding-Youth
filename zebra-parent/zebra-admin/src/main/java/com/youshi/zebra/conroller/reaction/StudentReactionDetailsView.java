package com.youshi.zebra.conroller.reaction;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.reaction.constants.Acceptance;
import com.youshi.zebra.reaction.constants.StudentReactionQuality;
import com.youshi.zebra.reaction.model.StudentReactionModel;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.view.ImageView;

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

	public String getQuality() {
		if (delegate.getQuality() == null) {
			return null;
		}

		return StudentReactionQuality.fromValue(delegate.getQuality()).getName();
	}

	public String getAcceptance() {
		if (delegate.getAcceptance() == null) {
			return null;
		}

		return Acceptance.fromValue(delegate.getAcceptance()).getName();
	}

	public String getRemark() {
		return delegate.getRemark();
	}

	public Integer getStar() {
		return delegate.getStar();
	}

	public Integer getScore() {
		Integer star = delegate.getStar();
		if (star == null) {
			return null;
		}
		double score = 100 * ((star + 0.0) / 5);
		return (int) score;
	}
}