package com.youshi.zebra.controller.course;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.service.LessonService;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.view.CalendarLessonView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年1月14日
 */

@RestController
@RequestMapping("/calendar")
public class CalendarController {
	@Autowired
	private ViewBuilder viewBuilder;
	
	@Autowired
	private LessonService lessonService;
	
	@ApiOperation(value = "日历，获取小红点", tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/points", method=RequestMethod.GET)
	public Object points(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class, required = false) Integer courseId,
			@ApiParam(value = "月份。格式：2017-01", required=true)
			@RequestParam(value = "month") String month
			) {
		Set<Integer> days = lessonService.getPoints(courseId, userId, UserType.Teacher, month);
		
		return new JsonResultView().addValue("days", days);
	}
	
	/**
	 * 日历，获取某天的课程提醒
	 * 
	 */
	@ApiOperation(value = "日历，获取某天的课程提醒", tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/reminders", method=RequestMethod.GET)
	public Object reminders(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class, required = false) Integer courseId,
			@ApiParam(value = "日期。格式：2017-01-01", required=true)
			@RequestParam(value = "day") String day
			) {
		List<LessonModel > lessonList = lessonService.getDayLessons(courseId, userId, UserType.Teacher, day);
		List<Object> lessons = viewBuilder.buildToList(lessonList, 
				ExplicitViewMapper.getInstance().setViewClass(CalendarLessonView.class));
		
		return new JsonResultView().addValue("lessons", lessons);
	}
}
