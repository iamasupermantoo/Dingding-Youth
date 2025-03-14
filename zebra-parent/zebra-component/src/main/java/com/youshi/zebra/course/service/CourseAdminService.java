package com.youshi.zebra.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.youshi.zebra.course.constants.CourseStatus;
import com.youshi.zebra.course.constants.CourseType;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.CourseMetaModel.CourseMetaKeys;
import com.youshi.zebra.course.model.CourseModel.CourseKeys;

/**
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
@Service
public class CourseAdminService {
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseMetaService courseMetaService;
	
	public PageView<CourseModel, HasUuid<Integer>> query(Integer userId, Integer courseId, Integer cmId, 
			Integer studentId, CourseStatus status, 
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create();
		if(courseId != null) {
			params.and().eq(CourseKeys.id, courseId);
		}
		if(cmId != null) {
			params.and().eq(CourseKeys.cm_id, cmId);
		}
		if(studentId != null) {
			params.and().eq(CourseKeys.student_id, studentId);
		}
		if(status != null) {
			params.and().eq(CourseKeys.status, status);
		}
		
		PageView<CourseModel, HasUuid<Integer>> page = courseService.getByCursor(cursor, limit, params);
		return page;
	}
	
	public PageView<CourseModel, HasUuid<Integer>> queryTryCourse(Integer userId, Integer courseId,
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create()
				.and().eq(CourseKeys.type, CourseType.TRY.getValue())
				;
		if(userId != null) {
			params.and().eq(CourseKeys.student_id, userId);
		}
		
		PageView<CourseModel, HasUuid<Integer>> page = courseService.getByCursor(cursor, limit, params);
		return page;
	}
	
	// OPTI lc缓存
	public List<CourseMetaModel> getCms() {
		WhereClause params = WhereClause.create()
				.and().eq(CourseMetaKeys.type, CourseType.TRY.getValue());
		int limit = 20;
		PageView<CourseMetaModel, HasUuid<Integer>> all = courseMetaService.getByCursor(null, limit, params);
		return all.getList();
	}

}
