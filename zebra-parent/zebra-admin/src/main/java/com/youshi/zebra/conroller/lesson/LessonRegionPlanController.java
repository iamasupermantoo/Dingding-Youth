package com.youshi.zebra.conroller.lesson;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.service.CourseService;
import com.youshi.zebra.lesson.dao.LessonPlanService;
import com.youshi.zebra.lesson.dao.LessonPlanService.TeacherOption;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.service.UserScheduleService;
import com.youshi.zebra.view.CourseDetailsView;
import com.youshi.zebra.view.LessonPlanView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 排课Controller
 * 
 * 
 * @author wangsch
 * @date 2017年2月17日
 */
@RestController
@RequestMapping(value = "/lesson/region/plan")
public class LessonRegionPlanController {
	@Autowired
	private LessonPlanService lessonPlanService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private UserScheduleService userScheduleService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "添加一个lesson", tags=SwaggerTags.LESSON_PLAN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/add", method=RequestMethod.POST)
	public Object add(
			@ApiIgnore @Visitor Integer userId,
			@RequestParam(value = "cid") Integer courseId,
			@RequestParam(value = "tid") Integer teacherId,
			@RequestParam(value = "date") String date,
			@RequestParam(value = "start") String start,
			@RequestParam(value = "end") String end,
			@RequestParam(value = "label") String label,
			@RequestParam(value = "cnt") Integer cnt
			) {
		
		int lessonId = lessonPlanService.addLesson(userId, courseId, teacherId, 
				date, start, end, label, cnt);
		
		return new JsonResultView().addValue("lid", lessonId);
	}
	
	@ApiOperation(value = "修改一个lesson", tags=SwaggerTags.LESSON_PLAN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/modify", method=RequestMethod.POST)
	public Object modify(
			@ApiIgnore @Visitor Integer userId,
			@RequestParam(value = "cid") Integer courseId,
			@RequestParam(value = "lid") Integer lessonId,
			@RequestParam(value = "tid", required=false) Integer teacherId,
			@RequestParam(value = "date") String date,
			@RequestParam(value = "start") String start,
			@RequestParam(value = "end") String end,
			@RequestParam(value = "label") String label,
			@RequestParam(value = "cnt") Integer cnt
			) {
		lessonPlanService.modifyLesson(userId, courseId, lessonId, teacherId, 
				date, start, end, label, cnt);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "删除一个lesson", tags=SwaggerTags.LESSON_PLAN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/remove", method=RequestMethod.POST)
	public Object remove(
			@ApiIgnore @Visitor Integer userId,
			@RequestParam(value = "cid") Integer courseId,
			@RequestParam(value = "lid") Integer lessonId
			) {
		lessonPlanService.removeLesson(userId, courseId, lessonId);
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "查询某个课程下的lesson，只查询上课的lesson", tags=SwaggerTags.LESSON_PLAN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public ModelAndView list(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class) Integer courseId,
			@RequestParam(value = "cursor", required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		CourseModel course = courseService.getById(courseId);
		Map<String, Object> courseMap = viewBuilder.buildSingleToMap(course, "course", 
				ExplicitViewMapper.getInstance().setViewClass(CourseDetailsView.class));
		
		PageView<LessonModel, HasUuid<Integer>> page = lessonPlanService
				.getPlanedLessons(userId, courseId, cursor, limit);
		Map<String, Object> lessonsMap = viewBuilder.buildToMap(page, "lessons", 
				ExplicitViewMapper.getInstance().setViewClass(LessonPlanView.class));
		
		List<TeacherOption> teachers = lessonPlanService.getAvailableTeachers();
		ModelAndView mav = new ModelAndView("lesson/lesson_plan_list");
		mav.addAllObjects(courseMap)
			.addAllObjects(lessonsMap)
			.addObject("teachers", teachers)
			;
		
		return mav;
	}
	
	@RequestMapping(value = "/times", method=RequestMethod.GET)
	@ResponseBody
	public Object teacherTimes(
			@RequestParam(value = "date") String date,
			@RequestParam(value = "tid") Integer teacherId
			) {
		List<String> times = userScheduleService.getTimes(teacherId, date);
		return new JsonResultView().addValue("times", times);
	}
	
}
