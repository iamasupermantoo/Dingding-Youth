package com.youshi.zebra.course.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasData;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.model.PageView;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.course.constants.CourseType;
import com.youshi.zebra.course.dao.TryCourseDAO;
import com.youshi.zebra.course.dao.TryCourseDAO.TryCourseStatus;
import com.youshi.zebra.course.model.TryCourseModel;
import com.youshi.zebra.order.constants.ChatResult;
import com.youshi.zebra.order.exception.TryCourseAlreadyExistException;
import com.youshi.zebra.order.model.OrderModel.OrderKeys;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class TryCourseService extends AbstractService<Integer, TryCourseModel>{
	private static final Logger logger = LoggerFactory.getLogger(TryCourseService.class);
	
	@Autowired
	private TryCourseDAO tryCourseDAO;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseAdminService courseAdminService;

	
	public PageView<TryCourseModel, HasUuid<Integer>> queryCourses(Integer cursor, int limit) {
		WhereClause params = WhereClause.create()
				;
		PageView<TryCourseModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		
		return page;
	}
	
	@Override
	public AbstractDAO<Integer, TryCourseModel> dao() {
		return tryCourseDAO;
	}
	
	public void create(int userId, int cmId) {
		TryCourseModel tryCourse = tryCourseDAO.getTryCourse(userId, cmId);
		if(tryCourse != null) {
			throw new TryCourseAlreadyExistException();
		}
		
		Integer courseId = null;
		boolean paid = false;
		boolean planned = false;
		Long updateTime = null;
		int id = tryCourseDAO.insert(cmId, courseId, userId, ChatResult.NO.getValue(), paid, planned, updateTime, 
				HasData.EMPTY_DATA, TryCourseStatus.Normal, System.currentTimeMillis());
		logger.info("Try course create succ. userId: {}, tryCountId: {}", userId, id);
	}
	
	public int confirmCourse(Integer studentId, Integer cmId) {
		int courseId = courseService.create(cmId, studentId, CourseType.TRY);
		
		TryCourseModel tryCourse = tryCourseDAO.getByStudentId(studentId);
		
		int c = tryCourseDAO.updateCourseId(studentId, courseId, 
				tryCourse.getChatResult(), tryCourse.getPaid(), tryCourse.getPlanned());
		DAOUtils.checkAffectRows(c);
		logger.info("Confirm course succ. studentId: {}, courseId: {}", studentId, courseId);
		return courseId;
	}
	
	public void chatResult(Integer studentId, Integer result, String remark) {
		TryCourseModel tryCourse = tryCourseDAO.getByStudentId(studentId);
		
		Map<String, Object> dataMap = tryCourse.resolvedData();
		dataMap.put(OrderKeys.remark.name(), remark);
		String data = DoradoMapperUtils.toJSON(dataMap);
		
		ChatResult res = ChatResult.fromValue(result);
		int c = tryCourseDAO.updateChatResult(studentId, res, data);
		DAOUtils.checkAffectRows(c);
		logger.info("Admin update chatResult succ. adminId: {}, orderId: {}, orderSn: {}");
	}
	
}