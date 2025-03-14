package com.youshi.zebra.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.model.PageView;
import com.youshi.zebra.student.TeacherStudentDAO;
import com.youshi.zebra.student.model.TeacherStudentModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月21日
 */
@Service
public class TeacherStudentService extends AbstractService<Integer, TeacherStudentModel>{
	
	@Autowired
	private TeacherStudentDAO teacherStudentDAO;

	
	public PageView<TeacherStudentModel, HasUuid<Integer>> getStudents(Integer teacherId, 
			Integer cursor, Integer limit) {
		
		WhereClause params = WhereClause.create().and().eq("teacher_id", teacherId);
		PageView<TeacherStudentModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		
		return page;
	}
	
	@Override
	protected AbstractDAO<Integer, TeacherStudentModel> dao() {
		return teacherStudentDAO;
	}
}
