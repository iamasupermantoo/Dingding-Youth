package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.student.model.TeacherStudentModel;
import com.youshi.zebra.user.model.UserModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月21日
 */
public class TeacherStudentView {
	TeacherStudentModel delegate;
	ZebraBuildContext context;
	
	private UserModel student;
	
	public TeacherStudentView(TeacherStudentModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		
		this.student = context.getUser(delegate.getStudentId());
	}
	
	public String getSid() {
		return student.getUuid();
	}
	
	public String getName() {
		return student.getName();
	}
	
	public Integer getWaitCorrect() {
		return 100;
	}
	public Integer getCorrected() {
		return 10;
	}
	
	
}
