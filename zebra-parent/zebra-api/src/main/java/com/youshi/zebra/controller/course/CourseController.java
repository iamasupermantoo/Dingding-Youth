package com.youshi.zebra.controller.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.core.constants.config.RawStringConfigKey;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.course.constants.CourseMetaStatus;
import com.youshi.zebra.course.constants.CourseType;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.CourseMetaModel.CourseMetaKeys;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.course.service.CourseService;
import com.youshi.zebra.course.service.TryCourseService;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.view.CourseMetaDetailsView;
import com.youshi.zebra.view.CourseMetaView;
import com.youshi.zebra.view.CourseView;

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
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseMetaService courseMetaService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@Autowired
	private TryCourseService tryCourseService;
	
	@ApiOperation(value = "学生课程列表（一般只有一个）", response=CourseView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cmId", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/apply", method=RequestMethod.POST)
	public Object apply(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cmId", type = CourseMetaModel.class) Integer cmId
			) {
		tryCourseService.create(userId, cmId);
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "学生课程列表（一般只有一个）", response=CourseView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	@LoginRequired
	public Object list(
			@ApiIgnore @Visitor Integer studentId
			) {
		PageView<CourseModel, HasUuid<Integer>> page = courseService.getCourses(studentId, UserType.Student);
		
		JsonResultView result = viewBuilder.build(page, "courses", 
				ExplicitViewMapper.getInstance().setViewClass(CourseView.class)
				);
		if(page.isEmpty()) {
			result.addValue("phone", RawStringConfigKey.CoursePhone.defaultValue());
		}
		return result;
	}
	
	@ApiOperation(value = "约课列表, 分页查询", response=CourseView.class, tags=SwaggerTags.COURSE)
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
				ExplicitViewMapper.getInstance().setViewClass(CourseMetaView.class));
		return result;
	}
	
	
	@ApiOperation(value = "课程meta信息详情", response=CourseMetaDetailsView.class, tags=SwaggerTags.COURSE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "课程meta id", name="cmid", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/meta/details", method=RequestMethod.GET)
	public Object metaDetails(
			@ApiIgnore @Uuid(value = "cmid", type=CourseMetaModel.class) Integer cmId
			) {
		CourseMetaModel courseMeta = courseMetaService.getById(cmId);
		JsonResultView result = viewBuilder.buildSingle(courseMeta, "course", 
				ExplicitViewMapper.getInstance().setViewClass(CourseMetaDetailsView.class));
		
		return result;
	}
}
