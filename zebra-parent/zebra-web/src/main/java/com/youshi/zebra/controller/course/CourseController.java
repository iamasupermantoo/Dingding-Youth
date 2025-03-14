package com.youshi.zebra.controller.course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.course.constants.CourseMetaStatus;
import com.youshi.zebra.course.constants.CourseType;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseMetaModel.CourseMetaKeys;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.course.service.CourseService;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.service.LessonService;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.view.CourseMetaDetailsView;
import com.youshi.zebra.view.CourseView;
import com.youshi.zebra.view.LessonView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年1月14日
 */
@RestController
@RequestMapping("/course")
public class CourseController {
	private static final int LESSON_SHOW_COUNT = 5;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseMetaService courseMetaService;
	
	@Autowired
	private LessonService lessonService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "个人中心，我的课程（一般只有一个）", response=CourseView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	@LoginRequired
	public Object list(
			@ApiIgnore @Visitor Integer studentId
			) {
		PageView<CourseModel, HasUuid<Integer>> page = courseService.getCourses(studentId, UserType.Student);
		
//		JsonResultView result = viewBuilder.build(page, "courses", 
//				ExplicitViewMapper.getInstance().setViewClass(CourseView.class)
//				);
		
		Map<String, Object> dataMap = viewBuilder.buildToMap(page, "courses", 
				ExplicitViewMapper.getInstance().setViewClass(CourseView.class));
		injectLessons(studentId, dataMap);
		
		return new JsonResultView().addValues(dataMap);
	}
	
	@ApiOperation(value = "公开课列表, 分页查询", response=CourseMetaDetailsView.class, tags=SwaggerTags.COURSE)
	@RequestMapping(value = "/meta/list", method=RequestMethod.GET)
	public Object metaList(
			@RequestParam(value = "cursor", required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		WhereClause params = WhereClause.create()
				.and().notEq(CourseMetaKeys.type, CourseType.TRY.getValue())
				.and().eq(CourseMetaKeys.status, CourseMetaStatus.Normal.getValue())
				;
		PageView<CourseMetaModel, HasUuid<Integer>> page = courseMetaService.getByCursor(cursor, limit, params);
		JsonResultView result = viewBuilder.build(page, "courses", 
				ExplicitViewMapper.getInstance().setViewClass(CourseMetaDetailsView.class));
		return result;
	}

	/**
	 * @param studentId
	 * @param page
	 * @param dataMap
	 */
	@SuppressWarnings("unchecked")
	private void injectLessons(Integer studentId, Map<String, Object> dataMap) {
		PageView<CourseView, String> courses = (PageView<CourseView, String>)dataMap.get("courses");
		if(courses == null || courses.isEmpty()) {
			return;
		}
		
		List<CourseView> list = (List<CourseView>)courses.getList();
		for (CourseView course : list) {
			Integer courseId = course.getId();
			PageView<LessonModel, HasUuid<Integer>> lessonPage = lessonService
					.getLessons(courseId, studentId, UserType.Student, null, LESSON_SHOW_COUNT);
			if(lessonPage.isEmpty()) {
				continue;
			}
			Map<String, Object> lessonMap = viewBuilder.buildToMap(lessonPage, "lessons", 
					ExplicitViewMapper.getInstance().setViewClass(LessonView.class));
			PageView<LessonView, String> lessons = (PageView<LessonView, String>)lessonMap.get("lessons");
			course.setLessons(lessons);
		}
	}
	
	
}
