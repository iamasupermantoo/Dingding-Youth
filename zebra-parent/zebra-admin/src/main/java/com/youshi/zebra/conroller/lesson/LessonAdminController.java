package com.youshi.zebra.conroller.lesson;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.lesson.constants.LessonStatus;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.lesson.model.LessonMonitorResult;
import com.youshi.zebra.lesson.service.LessonAdminService;
import com.youshi.zebra.view.LessonView;

/**
 * 
 * @author wangsch
 * @date 2017年2月15日
 */
@RequestMapping(value = "/lesson/admin")
@Controller
public class LessonAdminController {
	
	@Autowired
	private LessonAdminService lessonAdminService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public Object list(
			@Uuid(value = "cid", type=CourseModel.class, required=false) Integer courseId,
			@RequestParam(value = "status", required=false) LessonStatus status,
			@Uuid(value = "cursor", type=LessonModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<LessonModel, HasUuid<Integer>> page = lessonAdminService
				.getLessons(courseId, status, cursor, limit);
		Map<String, Object> resultMap = viewBuilder.buildToMap(page, "lessons", 
				ExplicitViewMapper.getInstance().setViewClass(LessonView.class));
		
		ModelAndView mav = new ModelAndView("lesson/lesson_list");
		return mav.addAllObjects(resultMap);
	}
	
	@RequestMapping(value = "/monitor/list", method=RequestMethod.GET)
	public Object monitorList(
			@RequestParam(value = "flag", defaultValue="next") String flag,			// next代表即将上课，pre代表已经上课
			@Uuid(value = "cursor", type=LessonModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		LessonMonitorResult result = lessonAdminService.getLessonMonitor(cursor, limit);
		
		List<Object> preLessons = viewBuilder.buildToList(result.getPreLessons(), 
				ExplicitViewMapper.getInstance().setViewClass(LessonView.class));
		List<Object> nextLessons = viewBuilder.buildToList(result.getNextLessons(), 
				ExplicitViewMapper.getInstance().setViewClass(LessonView.class));
		
		ModelAndView mav = new ModelAndView("lesson/lesson_monitor_list")
				.addObject("preLessons", preLessons)
				.addObject("nextLessons", nextLessons)
				.addObject("info", result.getInfo())
				
				.addObject("flag", flag)
				;
		return mav;
	}
}
