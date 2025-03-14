package com.youshi.zebra.conroller.course;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.connect.service.ConnectService;
import com.youshi.zebra.course.constants.CourseStatus;
import com.youshi.zebra.course.constants.CourseType;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.TryCourseModel;
import com.youshi.zebra.course.service.CourseAdminService;
import com.youshi.zebra.course.service.CourseService;
import com.youshi.zebra.course.service.TryCourseService;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.view.TryCourseView;

import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年6月27日
 */
@Controller
@RequestMapping("/try/course/admin")
public class TryCourseAdminController {
	
	@Autowired
	private TryCourseService tryCourseService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseAdminService courseAdminService;
	
	@Autowired
	@Qualifier("mobileConnectService")
	private ConnectService connectService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public ModelAndView list(
			@ApiIgnore @Visitor Integer userId,
			@RequestParam(value = "cid", required=false) Integer courseId,
			@RequestParam(value = "cmid", required=false) Integer cmId,
			@RequestParam(value = "sid", required=false) Integer studentId,
			@RequestParam(value = "status", required=false) CourseStatus status,
			@ApiIgnore @Uuid(value = "cursor", required = false, type=CourseModel.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<TryCourseModel, HasUuid<Integer>> page = tryCourseService.queryCourses(cursor, limit);
		
		Map<String, Object> resultMap = viewBuilder.buildToMap(page, "courses", 
				ExplicitViewMapper.getInstance().setViewClass(TryCourseView.class));
		ModelAndView mav = new ModelAndView("course/try_course_list");
		mav.addAllObjects(resultMap);
		
		mav.addObject("cid", courseId);
		mav.addObject("cmid", cmId);
		mav.addObject("sid", studentId);
		mav.addObject("status", status);
		
		mav.addObject("cursor", cursor);
		mav.addObject("limit", limit);
		
		return mav;
	}
	
	@RequestMapping(value = "/confirm", method=RequestMethod.POST)
	@ResponseBody
	public Object confirm(
			@RequestParam(value = "sid") Integer studentId,
			@ApiIgnore @Uuid(value = "cmId", type = CourseMetaModel.class) Integer cmId
			) {
		int courseId = tryCourseService.confirmCourse(studentId, cmId);
		return new JsonResultView()
				.addValue("cid", UuidUtils.getUuid(CourseModel.class, courseId));
	}
	
	@RequestMapping(value = "/chat/result", method=RequestMethod.POST)
	@ResponseBody
	public Object chatResult(
			@Visitor Integer userId,
			@RequestParam(value = "sid") Integer sid,
			@RequestParam(value = "result") Integer result,
			@RequestParam(value = "remark", required=false) String remark
			) {
		tryCourseService.chatResult(sid, result, remark);
		
		return JsonResultView.SUCCESS;
	}
}
