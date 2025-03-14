package com.youshi.zebra.lesson.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.service.CourseService;
import com.youshi.zebra.course.service.CourseStatusService;
import com.youshi.zebra.exception.common.EntityNotNormalException;
import com.youshi.zebra.lesson.constants.LessonStatus;
import com.youshi.zebra.lesson.exception.LessonTimePeriodConflictException;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.model.LessonModel.LessonKeys;
import com.youshi.zebra.lesson.service.LessonService;
import com.youshi.zebra.lesson.service.UserScheduleService;
import com.youshi.zebra.lesson.utils.LessonTimeUtils;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.model.UserModel.UserKey;
import com.youshi.zebra.user.service.UserService;

/**
 * 
 * 排课service，这个service专门给运营后台使用
 * 
 * @author wangsch
 * @date 2017年2月17日
 */
@Service
public class LessonPlanService extends AbstractService<Integer, LessonModel>{
	private static final Logger logger = LoggerFactory.getLogger(LessonPlanService.class);
	
	@Autowired
	private UserScheduleService userScheduleService;
	
	@Autowired
	private LessonService lessonService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LessonDAO lessionDAO;
	
	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private CourseStatusService courseStatusService;
	
	public int addLesson(Integer userId, Integer courseId, Integer teacherId,
			String date, String start, String end, String label, Integer cnt) {
		CourseModel course = courseService.getById(courseId);
		int studentId = course.getStudentId();
		checkSchedules(teacherId, studentId, date, start, end);
		
		UserModel teacher = userService.getById(teacherId);
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(LessonKeys.label.name(), label);
		dataMap.put(LessonKeys.cnt.name(), cnt);
		dataMap.put(LessonKeys.course.name(), course.getCourse());
		dataMap.put(LessonKeys.teacher.name(), teacher.getName());
		dataMap.put(LessonKeys.student.name(), course.getStudent());
		
		
		int bookId = course.getBookId();
		ChapterModel chapter = chapterService.getChapter(bookId, cnt);
		if(chapter == null) {
			throw new EntityNotNormalException();
		}
		
		String time = LessonTimeUtils.concat(start, end);
		int id = lessonService.addLesson(courseId, chapter.getId(), teacherId, studentId, 
				DoradoMapperUtils.toJSON(dataMap), date, time);
		
		addUserSchedules(teacherId, studentId, date, time);
		
		// 计数
		courseStatusService.incrPlanedCnt(courseId);
		
		logger.info("Admin add lesson succ. "
				+ "id: {}, adminId: {}, courseId: {}, teacherId: {}, date: {}, time: {}, label: {}, cnt: {}", 
				id, userId, courseId, teacherId, date, time, label, cnt);
		
		return id;
	}

	public void modifyLesson(Integer userId, Integer courseId, Integer lessonId, Integer teacherId,
			String date, String start, String end, String label, Integer cnt) {
		CourseModel course = courseService.getById(courseId);
		int studentId = course.getStudentId();
		checkSchedules(teacherId, studentId, date, start, end);
		
		String time = LessonTimeUtils.concat(start, end);
		LessonModel lesson = lessonService.getOneLesson(courseId, lessonId);
		Map<String, Object> dataMap = lesson.resolvedData();
		dataMap.put(LessonKeys.date.name(), date);
		dataMap.put(LessonKeys.time.name(), time);
		
		lessonService.updateLesson(courseId, lessonId, teacherId, date, time);;
		
		removeUserSchedules(teacherId, studentId, lesson.getDate(), lesson.getTime());
		addUserSchedules(teacherId, studentId, date, time);
	
		logger.info("Admin modify lesson succ. adminId: {}, courseId: {}, lessonId: {}", 
				userId, courseId, lessonId);
	}
	
	public void removeLesson(Integer userId, Integer courseId, Integer lessonId) {
		lessonService.removeLesson(courseId, lessonId);
		
		// 计数
		courseStatusService.decrPlanedCnt(courseId);
		
		logger.info("Admin delete lesson succ. adminId: {}, courseId: {}, lessonId: {}", 
				userId, courseId, lessonId);
	}
	
	public PageView<LessonModel, HasUuid<Integer>> getPlanedLessons(Integer userId, Integer courseId, Integer cursor,
			Integer limit) {
		WhereClause params = WhereClause.create()
						.and().in(LessonKeys.status.name(), LessonStatus.Added.getValue(), LessonStatus.WAIT.getValue())
						.and().eq(LessonKeys.course_id.name(), courseId);
		PageView<LessonModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		
		return page;
	}
	
	// TODO 根据老师的时间安排，自动过滤掉有课程的老师
	public List<TeacherOption> getAvailableTeachers() {
		WhereClause params = WhereClause.create()
				.and().eq(UserKey.type, UserType.Teacher.getValue());
		PageView<UserModel, HasUuid<Integer>> result = userService.getByCursor(null, 10000, params);
		
		return result.getList()
				.stream()
				.map(user->new TeacherOption(user.getId(), user.getName()))
				.collect(Collectors.toList());
	}
	
	public class TeacherOption {
		private  int id;
		private  String name;
		public TeacherOption(int id, String name) {
			this.id = id;
			this.name = name;
		}
		public int getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		
	}

	@Override
	protected AbstractDAO<Integer, LessonModel> dao() {
		return lessionDAO;
	}
	
	// ---------------------------------------- private method ----------------------------------------
	private void checkSchedules(Integer teacherId,
			int studentId, String date, String start, String end) {
		List<String> times = userScheduleService.getTimes(teacherId, date);
		boolean timeConflict = LessonTimeUtils.isTimeConflict(start, end, times);;
		if(timeConflict) {
			throw new LessonTimePeriodConflictException(ZebraMetaCode.TeacherLessonTimePeriodConflict);
		}
		
		times = userScheduleService.getTimes(studentId, date);
		timeConflict = LessonTimeUtils.isTimeConflict(start, end, times);;
		if(timeConflict) {
			throw new LessonTimePeriodConflictException(ZebraMetaCode.StudentLessonTimePeriodConflict);
		}
	}

	private void addUserSchedules(Integer teacherId, int studentId, 
			String date, String time) {
		userScheduleService.addTime(studentId, date, time);
		userScheduleService.addTime(teacherId, date, time);
	}

	private void removeUserSchedules(int teacherId, int studentId, String date,
			String time) {
		userScheduleService.removeTime(studentId, date, time);
		userScheduleService.removeTime(teacherId, date, time);
	}
}
