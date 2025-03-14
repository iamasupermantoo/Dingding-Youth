package com.youshi.zebra.lesson.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.model.PageView;
import com.dorado.framework.tuple.TwoTuple;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.course.dao.CourseStatusDAO;
import com.youshi.zebra.exception.common.ForbiddenException;
import com.youshi.zebra.homework.constants.HomeworkStatus;
import com.youshi.zebra.lesson.constants.LessonStatus;
import com.youshi.zebra.lesson.dao.LessonDAO;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.model.LessonModel.LessonKeys;
import com.youshi.zebra.lesson.utils.LessonTimeUtils;
import com.youshi.zebra.reaction.constants.StudentReactionStatus;
import com.youshi.zebra.reaction.constants.TeacherReactionStatus;
import com.youshi.zebra.user.constant.UserType;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月14日
 */
@Service
public class LessonService extends AbstractService<Integer, LessonModel>{
	private static Logger logger = LoggerFactory.getLogger(LessonService.class);
	
	@Autowired
	private LessonDAO lessonDAO;
	
	@Autowired
	private CourseStatusDAO courseCurrentStatusDAO;
	
	@Override
	protected AbstractDAO<Integer, LessonModel> dao() {
		return lessonDAO;
	}
	
	public int addLesson(Integer courseId, Integer chapterId, Integer teacherId, Integer studentId, 
			String data, String date, String time) {
		int id = lessonDAO.insert(courseId, chapterId, teacherId, studentId, 
				data, LessonStatus.WAIT, date, time, System.currentTimeMillis());
		
		return id;
	}
	
	public void updateLesson(Integer courseId, Integer lessonId, Integer teacherId,
			String date, String time) {
		
		// TODO random teacherId where null
		
		
		int c = lessonDAO.update(courseId, lessonId, teacherId, date, time);
		DAOUtils.checkAffectRows(c);
		
	}
	
	public LessonModel getOneLesson(Integer courseId, Integer lessonId) {
		return lessonDAO.getOneLesson(courseId, lessonId);
	}
	
	// ----------------------------------------- 学生 -----------------------------------------
	// END 学生
	
	// ----------------------------------------- 老师 -----------------------------------------
	public void updateData(Integer courseId, Integer lessonId, String data) {
		int c = lessonDAO.updateData(courseId, lessonId, data);
		DAOUtils.checkAffectRows(c);
		
	}
	
	
	// END 老师
	
	// ----------------------------------------- 状态 -----------------------------------------
	public void updateStatus(Integer teacherId, Integer courseId, Integer lessonId, LessonStatus status) {
		LessonModel lesson = getOneLesson(courseId, lessonId);
		if(lesson.getTeacherId() != lesson.getTeacherId()) {
			throw new ForbiddenException();
		}
		
		int c = lessonDAO.updateStatus(courseId, lessonId, status);
		DAOUtils.checkAffectRows(c);
		logger.info("Update lesson status succ. courseId: {}, lessonId: {}, to status: {}", courseId, lessonId, status);
	}
	
	public void updateHomeworkStatus(Integer courseId, Integer lessonId, HomeworkStatus status) {
		int c = lessonDAO.updateHomeworkStatus(courseId, lessonId, status);
		DAOUtils.checkAffectRows(c);
		logger.info("Update lesson homework status succ. courseId: {}, lessonId: {}, to status: {}", 
				courseId, lessonId, status);
	}
	
	public void updateTeacherReactionStatus(Integer courseId, Integer lessonId, TeacherReactionStatus status) {
		int c = lessonDAO.updateTeacherReactionStatus(courseId, lessonId, status);
		DAOUtils.checkAffectRows(c);
		logger.info("Update lesson teacher reaction status succ. courseId: {}, lessonId: {}, to status: {}", 
				courseId, lessonId, status);
	}
	
	public void updateStudentReactionStatus(Integer courseId, Integer lessonId, StudentReactionStatus status) {
		int c = lessonDAO.updateStudentReactionStatus(courseId, lessonId, status);
		DAOUtils.checkAffectRows(c);
		logger.info("Update lesson student reaction status succ. courseId: {}, lessonId: {}, to status: {}", 
				courseId, lessonId, status);
	}
	// END 状态
	
	
	
	
	// ----------------------------------------- 公共 -----------------------------------------
	public PageView<LessonModel, HasUuid<Integer>> getLessons(Integer courseId, Integer userId, UserType userType,
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create();
		if(courseId != null) {
			params.and().eq(LessonKeys.course_id, courseId);
		}
		switch(userType) {
			case Student:
				params.and().eq(LessonKeys.student_id, userId);
				break;
			case Teacher:
				params.and().eq(LessonKeys.teacher_id, userId);
				break;
			default:
				throw new IllegalArgumentException("Unsupported userType: " + userType);
		}
		PageView<LessonModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		
		return page;
	}
	
	public Set<Integer> getPoints(Integer courseId, Integer userId, UserType userType, String month) {
		TwoTuple<String, String> parsed = LessonTimeUtils.parsePointsMonth(month);
		
		List<String> days = lessonDAO.getPoints(courseId, userId, userType, parsed.first, parsed.second);
		Set<Integer> result = new HashSet<>();
		for (String day : days) {
			result.add(Integer.parseInt(day.split("-")[2]));
		}
		
		return result;
	}
	
	public List<LessonModel> getDayLessons(Integer courseId, Integer userId, UserType userType, String day) {
		List<LessonModel> result = lessonDAO.getDayLessons(courseId, userId, userType, day);
		
		return result;
	}
	// END 公共
	
	
	
	
	
	// ----------------------------------------- Admin -----------------------------------------
	/**
	 * 
	 * TODO应该控制只能删除最新的一条
	 * 
	 * 删除一个lesson。lesson的状态必须是{@link LessonStatus#WAIT}，可以执行真正的delete操作，不必留存，注意记下日志就行了。
	 * 
	 * @param courseId
	 * @param lessonId
	 * @return
	 */
	public void removeLesson(Integer courseId, Integer lessonId) {
		int c = lessonDAO.deleteLesson(courseId, lessonId, LessonStatus.WAIT);
		DAOUtils.checkAffectRows(c);
		logger.info("Delete wait plan succ. ");
	}
	// END Admin

	
	
	// ----------------------------------------- private methods -----------------------------------------
	

}
