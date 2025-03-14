package com.youshi.zebra.reaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.youshi.zebra.reaction.constants.StudentReactionStatus;
import com.youshi.zebra.reaction.constants.TeacherReactionStatus;
import com.youshi.zebra.reaction.model.StudentReactionModel;
import com.youshi.zebra.reaction.model.StudentReactionModel.StudentReactionKeys;
import com.youshi.zebra.reaction.model.TeacherReactionModel;

/**
 * 
 * @author wangsch
 * @date 2017年8月22日
 */
@Service
public class ReactionAdminService {
	@Autowired
	private TeacherReactionService teacherReactionService;
	
	@Autowired
	private StudentReactionService studentReactionService;
	
	public PageView<TeacherReactionModel, HasUuid<Integer>> getTeacherReactions(
			Integer teacherId, Integer studentId, Integer courseId,
			TeacherReactionStatus status, Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create();
		if(teacherId != null) {
			params.and().eq(StudentReactionKeys.teacher_id, teacherId);
		}
		if(studentId != null) {
			params.and().eq(StudentReactionKeys.student_id, studentId);
		}
		if(courseId != null) {
			params.and().eq(StudentReactionKeys.course_id, courseId);
		}
		if(status != null) {
			params.and().eq(StudentReactionKeys.status, status.getValue());
		}
		
		PageView<TeacherReactionModel, HasUuid<Integer>> page = teacherReactionService
				.getByCursor(cursor, limit, params);
		
		return page;
	}
	
	public PageView<StudentReactionModel, HasUuid<Integer>> getStudentReactions(
			Integer teacherId, Integer studentId, Integer courseId,
			StudentReactionStatus status, Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create();
		if(teacherId != null) {
			params.and().eq(StudentReactionKeys.teacher_id, teacherId);
		}
		if(studentId != null) {
			params.and().eq(StudentReactionKeys.student_id, studentId);
		}
		if(courseId != null) {
			params.and().eq(StudentReactionKeys.course_id, courseId);
		}
		if(status != null) {
			params.and().eq(StudentReactionKeys.status, status.getValue());
		}
		
		PageView<StudentReactionModel, HasUuid<Integer>> page = studentReactionService
				.getByCursor(cursor, limit, params);
		
		return page;
	}
}
