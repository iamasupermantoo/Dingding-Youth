package com.youshi.zebra.homework.service;

import java.util.HashMap;
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
import com.youshi.zebra.book.model.ChapterModel;
import com.youshi.zebra.book.service.ChapterService;
import com.youshi.zebra.core.exception.EntityNotFoundException;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.homework.constants.HomeworkStatus;
import com.youshi.zebra.homework.dao.HomeworkDAO;
import com.youshi.zebra.homework.model.HomeworkModel;
import com.youshi.zebra.homework.model.HomeworkModel.HomeworkKeys;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.model.LessonModel.LessonKeys;
import com.youshi.zebra.lesson.service.LessonService;
import com.youshi.zebra.user.service.UserService;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@Service
public class HomeworkService extends AbstractService<Integer, HomeworkModel>{
	private Logger logger = LoggerFactory.getLogger(HomeworkService.class);
	
	@Autowired
	private LessonService lessonService;
	
	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HomeworkDAO homeworkDAO;
	
	@Override
	protected AbstractDAO<Integer, HomeworkModel> dao() {
		return homeworkDAO;
	}
	
	public HomeworkModel getHomework(Integer homeworkId, Integer courseId, Integer lessonId) {
		if(homeworkId == null) {
			return homeworkDAO.getHomework(courseId, lessonId);
		} else {
			return homeworkDAO.getById(homeworkId);
		}
	}
	
	/**
	 * 作业列表，分页查询。用于：学生查自己的作业，不区分课程的所有作业
	 * 
	 * 
	 * 
	 */
	public PageView<HomeworkModel, HasUuid<Integer>> getStudentHomeworks(Integer studentId, Integer status,
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create()
				.and().eq(HomeworkKeys.student_id, studentId);
		
		if(status != null ) {
			HomeworkStatus s = HomeworkStatus.fromValue(status);
			params.and().eq(HomeworkKeys.status, s.getValue());
		}
		
		PageView<HomeworkModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		
		return page;
	}
	
	public int getWaitCommit(Integer studentId) {
		int count = homeworkDAO.getWaitCommit(studentId);
		return count;
	}
	
	/**
	 * 作业列表，分页查询。用于：老师查询自己布置的作业，维度：根据学生、根据状态
	 * 
	 * @param teacherId		老师id
	 * @param studentId		学生id，可以为null
	 * @param status		作业状态，可以为null
	 * @param cursor		cursor
	 * @param limit			limit
	 * @return		{@link PageView}
	 */
	public PageView<HomeworkModel, HasUuid<Integer>> getTeacherHomeworks(
			Integer teacherId, Integer studentId, Integer status, 
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create()
				.and().eq(HomeworkKeys.teacher_id, teacherId);
		
		if(status != null) {
			HomeworkStatus hStatus = HomeworkStatus.fromValue(status);
			params.and().eq(HomeworkKeys.status, hStatus.getValue());
		}
		if(studentId != null) {
			params.and().eq(HomeworkKeys.student_id, studentId);
		}
		
		PageView<HomeworkModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		
		return page;
	}
	
	public void correct(Integer teacherId, Integer homeworkId, Boolean intime, Integer complete, Integer quality,
			String remark) {
		HomeworkModel homework = homeworkDAO.getById(homeworkId);
		if(homework == null) {
			throw new EntityNotFoundException();
		}
		
		Map<String, Object> dataMap = homework.resolvedData();
		dataMap.put(HomeworkKeys.intime.name(), intime);
		dataMap.put(HomeworkKeys.complete.name(), complete);
		dataMap.put(HomeworkKeys.quality.name(), quality);
		dataMap.put(HomeworkKeys.remark.name(), remark);
		
		int c = homeworkDAO.updateData(homeworkId, DoradoMapperUtils.toJSON(dataMap), homework.getData()
				);
		DAOUtils.checkAffectRows(c);
		
		c = homeworkDAO.setStatus(homeworkId, HomeworkStatus.CORRECTED);
		DAOUtils.checkAffectRows(c);
		
		lessonService.updateHomeworkStatus(homework.getCourseId(), homework.getLessonId(), 
				HomeworkStatus.CORRECTED);
		
		logger.info("Teacher correct homework succ. teacherId: {}, homeworkId: {}", teacherId, homeworkId);
	}
	
	public void setStatus(Integer userId, Integer homeworkId, HomeworkStatus status) {
		int c = homeworkDAO.setStatus(homeworkId, status);
		DAOUtils.checkAffectRows(c);
		
		logger.info("Homework status update succ. userId: {}, homeworkId: {}, toStatus: {}", 
				userId, homeworkId, status);
	}
	
	/**
	 * 课程结束后，创建作业
	 */
	public void createHomework(Integer courseId, Integer lessonId) {
		LessonModel lesson = lessonService.getOneLesson(courseId, lessonId);
		if(lesson == null) {
			throw new EntityNotFoundException();
		}
		int teacherId = lesson.getTeacherId();
		int studentId = lesson.getStudentId();
		Integer chapterId = lesson.getChapterId();
		
		ChapterModel chapter = chapterService.getById(chapterId);
		if(chapter == null) {
			throw new EntityNotFoundException();
		}
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(HomeworkKeys.title.name(), chapter.getHomeworkTitle());
		dataMap.put(HomeworkKeys.content.name(), chapter.getHomeworkContent());
		dataMap.put(HomeworkKeys.date.name(), lesson.getDate());
		dataMap.put(HomeworkKeys.time.name(), lesson.getTime());
		
		String data = DoradoMapperUtils.toJSON(dataMap);
		
		int homeworkId = homeworkDAO.insert(teacherId, studentId, courseId, lessonId, data, 
				HomeworkStatus.WAIT_COMMIT, System.currentTimeMillis());
		
		logger.info("Homework create succ. courseId: {}, lessonId: {}, homeworkId: {}",
				courseId, lessonId, homeworkId);
	}

}
