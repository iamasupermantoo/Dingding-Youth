package com.youshi.zebra.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.youshi.zebra.homework.constants.HomeworkStatus;
import com.youshi.zebra.homework.model.HomeworkModel;
import com.youshi.zebra.homework.model.HomeworkModel.HomeworkKeys;

/**
 * 
 * 运营后台，作业管理
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@Service
public class HomeworkAdminService {
	
	@Autowired
	private HomeworkService homeworkService;
	
	/**
	 * 作业列表，分页查询，根据各种条件：如老师、学生、课程id
	 * 
	 * 
	 * 
	 */
	public PageView<HomeworkModel, HasUuid<Integer>> queryHomeworks(
			Integer courseId, Integer lessonId, Integer teacherId, Integer studentId, HomeworkStatus status, 
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create();
		if(courseId != null) {
			params.and().eq(HomeworkKeys.course_id, courseId);
		}
		if(lessonId != null) {
			params.and().eq(HomeworkKeys.lesson_id, lessonId);
		}
		if(teacherId != null) {
			params.and().eq(HomeworkKeys.teacher_id, teacherId);
		}
		if(studentId != null) {
			params.and().eq(HomeworkKeys.student_id, studentId);
		}
		if(status != null) {
			params.and().eq(HomeworkKeys.status, status.getValue());
		}
		
		PageView<HomeworkModel, HasUuid<Integer>> page = homeworkService
				.getByCursor(cursor, limit, params);
		
		return page;
	}

}
