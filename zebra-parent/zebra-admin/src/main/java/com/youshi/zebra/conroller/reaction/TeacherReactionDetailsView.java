package com.youshi.zebra.conroller.reaction;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.reaction.constants.TeacherReactionBehavior;
import com.youshi.zebra.reaction.constants.TeacherReactionIdea;
import com.youshi.zebra.reaction.model.TeacherReactionModel;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.view.ImageView;

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

	public String getIdea() {
		Integer idea = delegate.getIdea();
		if(idea == null) {
			return null;
		}
		
		return TeacherReactionIdea.fromValue(idea).getName();
	}

	public String getBehavior() {
		Integer behavior = delegate.getBehavior();
		if(behavior == null) {
			return null;
		}
		return TeacherReactionBehavior.fromValue(behavior).getName();
	}

	public String getRemark() {
		return delegate.getRemark();
	}

	public Integer getStar() {
		return delegate.getStar();
	}
	
	public Integer getScore() {
		Integer star = delegate.getStar();
		if(star == null) {
			return null;
		}
		double score = 100 * ((star + 0.0) / 5);
		return (int)score;
	}

}