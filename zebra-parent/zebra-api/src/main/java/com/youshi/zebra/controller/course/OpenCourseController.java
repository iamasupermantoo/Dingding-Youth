package com.youshi.zebra.controller.course;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.core.constants.config.RawStringConfigKey;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.course.model.LiveModel;
import com.youshi.zebra.course.model.OpenCourseView;
import com.youshi.zebra.course.service.LiveMetaService;
import com.youshi.zebra.recommend.model.RecommendFeedView;
import com.youshi.zebra.teacher.model.TeacherModel;
import com.youshi.zebra.teacher.model.TeacherView;
import com.youshi.zebra.teacher.service.TeacherService;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.view.CourseMetaDetailsView;
import com.youshi.zebra.view.ImageView;
import com.youshi.zebra.view.TeacherDetailView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 公开课
 * @author guo
 *
 */
@RestController
@RequestMapping("/opencourse")
public class OpenCourseController {
	
	@Autowired
	private LiveMetaService liveMetaService;
	
	@Autowired
	private TeacherService teacherService;
	
	
	@ApiOperation(value = "公开课信息详情", response=CourseMetaDetailsView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "课程meta id", name="lmid", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/meta/details", method=RequestMethod.GET)
	public Object metaDetails(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "lmid", type=LiveMetaModel.class) Integer lmId
			) {
		OpenCourseView openCourseDetail = liveMetaService.getOpenCourseDetail(userId, lmId);
		JsonResultView result = new JsonResultView();
		result.addValue("course", openCourseDetail);
		return result;
	}
	
	@ApiOperation(value = "公开课列表", response=CourseMetaDetailsView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		//@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", required=false, paramType="query"),
	})
	@RequestMapping(value = "/meta/list", method=RequestMethod.GET)
	public Object metaList(
			//@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cursor", type = LiveMetaModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<RecommendFeedView, String> opencourses = liveMetaService.getOpenCourseList(cursor, limit);
		JsonResultView result = new JsonResultView();
		result.addValue("opencourses", opencourses );
		result.addValue("phone", RawStringConfigKey.CoursePhone.get());
		return result;
	}
	
	
	@ApiOperation(value = "已购公开课列表", response=CourseMetaDetailsView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", required=false, paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public Object list(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cursor", type = LiveModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<RecommendFeedView, String> opencourses = liveMetaService.getPaidOpenCourseList(userId, cursor, limit);
		JsonResultView result = new JsonResultView();
		
		result.addValue("opencourses", opencourses);
		result.addValue("phone", RawStringConfigKey.CoursePhone.get());
		
		return result;
	}
	
	
	
	@ApiOperation(value = "试听公开课", response=CourseMetaDetailsView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", dataType="String", required=true, paramType="query"),
		@ApiImplicitParam(value = "课程meta id", name="lmid", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/meta/freeRial", method=RequestMethod.GET)
	public Object freeRial(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "lmid", type=LiveMetaModel.class) Integer lmId
			) {
		liveMetaService.addLive(userId, lmId);
		return JsonResultView.SUCCESS;
	}
	
	
	
	
	
	
	
	
	
	
	@ApiOperation(value = "查询老师列表", response=TeacherDetailView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", required=false, paramType="query"),
	})
	@RequestMapping(value = "/teacher/list", method=RequestMethod.GET)
	public Object teacherList(
			@ApiIgnore @Uuid(value = "cursor", type = UserModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<TeacherView, String> teachers = teacherService.getTeacherList(cursor, limit);
		
		JsonResultView result = new JsonResultView();
		result.addValue("teachers",teachers);
		return result;
	}
	
	
	
}
