	package com.youshi.zebra.controller.course;

import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.course.service.CourseService;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.service.LessonService;
import com.youshi.zebra.user.constant.UserType;
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
@RequestMapping("/lesson")
@LoginRequired
public class LessonController {
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseMetaService courseMetaService;
	
	@Autowired
	private LessonService lessonService;
	
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "课次列表", notes="某个课程下的具体课次列表，分页查询", 
			response=LessonView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public Object list(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@Uuid(value = "cursor", type=LessonModel.class, required = false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<LessonModel, HasUuid<Integer>> page = lessonService
				.getLessons(courseId, userId, UserType.Student, cursor, limit);
		
		JsonResultView result = viewBuilder.build(page, "lessons", 
				ExplicitViewMapper.getInstance().setViewClass(LessonView.class));
		
		// TODO
		CourseModel course = courseService.getById(courseId);
		CourseMetaModel cm = courseMetaService.getById(course.getCmId());
		result.addValue("allowShare", RandomUtils.nextInt(0, 10) % 2 ==0 ? true : false);
		
		return result;
	}
	
	@ApiOperation(value = "课次列表", notes="点击日历下方某一个lesson, 调用这个接口", 
			response=LessonView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/calendar/list", method=RequestMethod.GET)
	public Object calendarList(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class) Integer lessonId,
			@Uuid(value = "cursor", type=LessonModel.class, required = false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<LessonModel, HasUuid<Integer>> page = lessonService
				.getLessons(courseId, userId, UserType.Student, cursor, limit);
		
		Map<String, Object> dataMap = viewBuilder.buildToMap(page, "lessons", 
				ExplicitViewMapper.getInstance().setViewClass(LessonView.class));
		
		LessonModel lesson = lessonService.getOneLesson(courseId, lessonId);
		Map<String, Object> map2 = viewBuilder.buildSingleToMap(lesson, "lesson", 
				ExplicitViewMapper.getInstance().setViewClass(LessonView.class));
		
		JsonResultView result = new JsonResultView().addValues(dataMap)
				.addValues(map2);
		return result;
	}
}
