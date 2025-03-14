package com.youshi.zebra.course.service;

import java.util.HashMap;
import java.util.List;
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
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.counts.service.UserCountsService;
import com.youshi.zebra.course.constants.CourseStatus;
import com.youshi.zebra.course.constants.CourseType;
import com.youshi.zebra.course.dao.CourseDAO;
import com.youshi.zebra.course.dao.CourseStatusDAO;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.CourseModel.CourseKeys;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.service.LessonService;
import com.youshi.zebra.user.constant.UserType;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@Service
public class CourseService extends AbstractService<Integer, CourseModel>{
	private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
	
	private static final int LESSON_SHOW_COUNT = 5;
	
	@Autowired
	private CourseDAO courseDAO;
	
	@Autowired
	private CourseStatusDAO courseStatusDAO;
	
	@Autowired
	private CourseMetaService courseMetaService;
	
	@Autowired
	private LessonService lessonService;
	
	@Autowired
	private UserCountsService userCountsService;
	
	@Override
	protected AbstractDAO<Integer, CourseModel> dao() {
		return courseDAO;
	}
	
	public int create(Integer cmId, Integer studentId, CourseType type) {
		CourseMetaModel courseMeta = courseMetaService.getById(cmId);
		
		long ct = System.currentTimeMillis();
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(CourseKeys.total_cnt.name(), courseMeta.getTotalCnt());
		dataMap.put(CourseKeys.course.name(), courseMeta.getName());
		dataMap.put(CourseKeys.image_id.name(), courseMeta.getImageId());
		String data = DoradoMapperUtils.toJSON(dataMap);
		
		int bookId = courseMeta.getBookId();
		int courseId = courseDAO.insert(cmId, bookId, studentId, 
				data, CourseStatus.OnProgress, type, ct);
		
		int c = courseStatusDAO.insert(courseId, ct);
		DAOUtils.checkAffectRows(c);
		
		userCountsService.addCourseCount(studentId, 1);
		
		logger.info("Course create succ. courseId: {}, studentId: {}, cmId: {}", 
				courseId, studentId, cmId);
		
		return courseId;
	
	}
	
	
	public PageView<CourseModel, HasUuid<Integer>> getCourses(Integer userId, UserType userType) {
		WhereClause params = WhereClause.create();
		
		switch(userType) {
			case Student:
				params.and().eq(CourseKeys.student_id, userId);
				break;
			default:
				throw new IllegalArgumentException();
		}
		
		PageView<CourseModel, HasUuid<Integer>> courses = getByCursor(null, 666, params);
		return courses;
	}
	
	/**
	 * web端专用，需要在课程下边，挂几个lesson
	 */
	public PageView<CourseModel, HasUuid<Integer>> getWebCourses(Integer userId) {
		WhereClause params = WhereClause.create();
		params.and().eq(CourseKeys.student_id, userId);
		
		PageView<CourseModel, HasUuid<Integer>> courses = getByCursor(null, 666, params);
		if(courses.isEmpty()) {
			return courses;
		}
		
		List<CourseModel> list = courses.getList();
		
		for (CourseModel course : list) {
			Integer courseId = course.getId();
			PageView<LessonModel, HasUuid<Integer>> lessonPage = lessonService
					.getLessons(courseId, userId, UserType.Student, null, LESSON_SHOW_COUNT);
			
			
			
		}
		
		return courses;
	}
}
