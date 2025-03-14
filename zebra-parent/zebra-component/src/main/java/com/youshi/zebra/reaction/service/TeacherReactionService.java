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
import com.youshi.zebra.exception.common.EntityNotNormalException;
import com.youshi.zebra.lesson.constants.LessonStatus;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.service.LessonService;
import com.youshi.zebra.reaction.constants.TeacherReactionBehavior;
import com.youshi.zebra.reaction.constants.TeacherReactionIdea;
import com.youshi.zebra.reaction.constants.TeacherReactionStatus;
import com.youshi.zebra.reaction.dao.TeacherReactionDAO;
import com.youshi.zebra.reaction.model.TeacherReactionModel;
import com.youshi.zebra.reaction.model.TeacherReactionModel.StudentReactionKeys;

/**
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
@Service
public class TeacherReactionService extends AbstractService<Integer, TeacherReactionModel>{
	private Logger logger = LoggerFactory.getLogger(TeacherReactionService.class);
	
	@Autowired
	private TeacherReactionDAO teacherReactionDAO;
	
	@Autowired
	private LessonService lessonService;
	
	public void commit(Integer teacherId, Integer courseId, Integer lessonId, 
			Integer idea, Integer behavior, String remark, Integer star) {
		TeacherReactionModel reaction = getOneReaction(teacherId, courseId, lessonId);
		
		Map<String, Object> dataMap = reaction.resolvedData();
		dataMap.put(StudentReactionKeys.idea.name(), TeacherReactionIdea.fromValue(idea).getValue());
		dataMap.put(StudentReactionKeys.behavior.name(), TeacherReactionBehavior.fromValue(behavior).getValue());
		dataMap.put(StudentReactionKeys.remark.name(), remark);
		dataMap.put(StudentReactionKeys.star.name(), star);
		String newData = DoradoMapperUtils.toJSON(dataMap);
		
		int c = teacherReactionDAO.update(courseId, lessonId, newData, TeacherReactionStatus.Committed);
		DAOUtils.checkAffectRows(c);
		
		lessonService.updateTeacherReactionStatus(courseId, lessonId, TeacherReactionStatus.Committed);
		
		logger.info("Teacher commit reaction succ. teacherId: {}, courseId: {}, lessonId: {}",
				teacherId, courseId, lessonId);
		
	}
	
	public TeacherReactionModel getOneReaction(Integer teacherId, Integer courseId, Integer lessonId) {
		TeacherReactionModel reaction = teacherReactionDAO.getOne(courseId, lessonId);
		if(reaction == null) {
			logger.error("Teacher reaction NOT FOUND. tid:{}, cid: {}, lid: {}", 
					teacherId, courseId, lessonId);
			throw new EntityNotNormalException();
		}
		return reaction;
	}
	
	
	public PageView<TeacherReactionModel, HasUuid<Integer>> getReactions(
			Integer teacherId, Integer status,
			Integer cursor, Integer limit) {
		TeacherReactionStatus srStatus = TeacherReactionStatus.fromValue(status);
		WhereClause params = WhereClause.create()
				.and().eq(StudentReactionKeys.teacher_id, teacherId)
				.and().eq(StudentReactionKeys.status, srStatus.getValue())
				;
		
		PageView<TeacherReactionModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		return page;
	}
	
	
	
	/**
	 * 
	 * 这个方法，lesson成功结束后由老师调用，将会添加一个老师给学生的待反馈记录。
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
		
		Integer id = teacherReactionDAO.insert(courseId, lessonId, studentId, teacherId, 
				ObjectMapperUtils.toJSON(dataMap), TeacherReactionStatus.WaitCommit, System.currentTimeMillis());
		
		logger.info("Add one reaction succ. login tid: {}, cid: {}, lid: {}, srid: {}", 
				loginTeacherId, courseId, lessonId, id);
	}
	
	@Override
	protected AbstractDAO<Integer, TeacherReactionModel> dao() {
		return teacherReactionDAO;
	}
}
