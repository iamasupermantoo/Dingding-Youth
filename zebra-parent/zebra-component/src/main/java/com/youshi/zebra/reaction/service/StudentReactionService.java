package com.youshi.zebra.reaction.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.model.PageView;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.exception.base.DoradoRuntimeException;
import com.youshi.zebra.lesson.constants.LessonStatus;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.service.LessonService;
import com.youshi.zebra.reaction.constants.Acceptance;
import com.youshi.zebra.reaction.constants.StudentReactionQuality;
import com.youshi.zebra.reaction.constants.StudentReactionStatus;
import com.youshi.zebra.reaction.dao.StudentReactionDAO;
import com.youshi.zebra.reaction.model.StudentReactionModel;
import com.youshi.zebra.reaction.model.StudentReactionModel.StudentReactionKeys;

/**
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
@Service
public class StudentReactionService extends AbstractService<Integer, StudentReactionModel>{
	private Logger logger = LoggerFactory.getLogger(StudentReactionService.class);
	
	@Autowired
	private StudentReactionDAO studentReactionDAO;
	
	@Autowired
	private LessonService lessonService;
	
	public void commit(Integer studentId, Integer courseId, Integer lessonId, 
			Integer quality, Integer acceptance, String remark, Integer star) {
		StudentReactionModel reaction = getOneReaction(studentId, courseId, lessonId);
		
		Map<String, Object> dataMap = reaction.resolvedData();
		dataMap.put(StudentReactionKeys.quality.name(), StudentReactionQuality.fromValue(quality).getValue());
		dataMap.put(StudentReactionKeys.acceptance.name(), Acceptance.fromValue(acceptance).getValue());
		dataMap.put(StudentReactionKeys.remark.name(), remark);
		dataMap.put(StudentReactionKeys.star.name(), star);
		String newData = DoradoMapperUtils.toJSON(dataMap);
		
		int c = studentReactionDAO.update(courseId, lessonId, newData, StudentReactionStatus.Committed);
		DAOUtils.checkAffectRows(c);
		
		lessonService.updateStudentReactionStatus(courseId, lessonId, StudentReactionStatus.Committed);
		
		logger.info("Student commit reaction succ. studentId: {}, courseId: {}, lessonId: {}", 
				studentId, courseId, lessonId);
	}
	
	public StudentReactionModel getOneReaction(Integer studentId, Integer courseId, Integer lessonId) {
		StudentReactionModel reaction = studentReactionDAO.getOne(courseId, lessonId);
		
		return reaction;
	}
	
	
	public PageView<StudentReactionModel, HasUuid<Integer>> getReactions(
			Integer studentId, Integer status,
			Integer cursor, Integer limit) {
		StudentReactionStatus srStatus = StudentReactionStatus.fromValue(status);
		WhereClause params = WhereClause.create()
				.and().eq(StudentReactionKeys.student_id, studentId)
				.and().eq(StudentReactionKeys.status, srStatus.getValue())
				;
		
		PageView<StudentReactionModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		return page;
	}
	
	
	
	/**
	 * 
	 * 这个方法，lesson成功结束后由老师调用，将会添加一个学生给老师的待反馈记录。
	 * 
	 * @param loginTeacherId
	 * @param courseId
	 * @param lessonId
	 */
	public void createReaction(Integer loginTeacherId, Integer courseId, Integer lessonId
			) {
		LessonModel lesson = lessonService.getOneLesson(courseId, lessonId);
		if(lesson.getStatus() != LessonStatus.FINISHED.getValue()) {
			throw new DoradoRuntimeException();
		}
		
		int studentId = lesson.getStudentId();
		int teacherId = lesson.getTeacherId();
		
		Map<String, Object> dataMap = lesson.resolvedData();
		dataMap.put(StudentReactionKeys.date.name(), lesson.getDate());
		dataMap.put(StudentReactionKeys.time.name(), lesson.getTime());
		dataMap.put(StudentReactionKeys.cnt.name(), lesson.getCnt());
		
		Integer id = studentReactionDAO.insert(courseId, lessonId, studentId, teacherId, 
				ObjectMapperUtils.toJSON(dataMap), StudentReactionStatus.WaitCommit, System.currentTimeMillis());
		
		logger.info("Add one reaction succ. login tid: {}, cid: {}, lid: {}, srid: {}", 
				loginTeacherId, courseId, lessonId, id);
	}
	
	@Override
	protected AbstractDAO<Integer, StudentReactionModel> dao() {
		return studentReactionDAO;
	}
	
}
