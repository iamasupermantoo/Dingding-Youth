package com.youshi.zebra.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.course.dao.CourseStatusDAO;
import com.youshi.zebra.course.model.CourseStatusModel;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年3月27日
 */
@Service
public class CourseStatusService extends AbstractService<Integer, CourseStatusModel>{
	@Autowired
	private CourseStatusDAO courseStatusDAO;
	
	private static final int INCR_1 = 1;
	private static final int DECR_1 = -1;
	
	public void incrPlanedCnt(Integer courseId) {
		int c = courseStatusDAO.incrPlanedMaxCnt(courseId, INCR_1, System.currentTimeMillis());
		DAOUtils.checkAffectRows(c);
	}
	
	public void decrPlanedCnt(Integer courseId) {
		int c = courseStatusDAO.incrPlanedMaxCnt(courseId, DECR_1, System.currentTimeMillis());
		DAOUtils.checkAffectRows(c);
	}
	
	public void incrFinishedCnt(Integer courseId) {
		int c = courseStatusDAO.incrFinishedMaxCnt(courseId, INCR_1, System.currentTimeMillis());
		DAOUtils.checkAffectRows(c);
	}
	
	@Override
	protected AbstractDAO<Integer, CourseStatusModel> dao() {
		return courseStatusDAO;
	}
}
