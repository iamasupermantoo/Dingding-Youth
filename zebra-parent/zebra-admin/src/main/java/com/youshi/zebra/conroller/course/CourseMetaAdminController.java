package com.youshi.zebra.conroller.course;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.book.model.BookOption;
import com.youshi.zebra.book.service.BookService;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.course.constants.CourseMetaStatus;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.view.CourseMetaAdminView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 
 * 课程meta管理
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
@RestController
@RequestMapping("/course/meta/admin")
public class CourseMetaAdminController {
	@Autowired
	private CourseMetaService courseMetaService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "课程meta列表查询", tags=SwaggerTags.COURSE_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "list", method=RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "cmid", required=false) Integer cmId,
			@RequestParam(value = "status", defaultValue="Normal") CourseMetaStatus status,
			@ApiIgnore @Uuid(value = "cursor", required = false, type=CourseModel.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit) {
		PageView<CourseMetaModel, HasUuid<Integer>> page = courseMetaService.getCourseMetas(cmId, status, cursor, limit);
		
		Map<String, Object> resultMap = viewBuilder.buildToMap(page, "courseMetas", 
				ExplicitViewMapper.getInstance().setViewClass(CourseMetaAdminView.class));
		ModelAndView mav = new ModelAndView("course/course_meta_list");
		mav.addAllObjects(resultMap);
		
		List<BookOption> books = bookService.getBookOptions();
		mav.addObject("books", books);
		return mav;
	}
	
	@ApiOperation(value = "课程meta详细信息", tags=SwaggerTags.COURSE_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/details", method=RequestMethod.GET)
	public Object details(
			@RequestParam(value = "cmid") Integer cmId
			) {
		CourseMetaModel courseMeta = courseMetaService.getById(cmId);
		JsonResultView result = viewBuilder.buildSingle(courseMeta, "details", 
				ExplicitViewMapper.getInstance().setViewClass(CourseMetaAdminView.class));
		
		return result;
	}
	
	@ApiOperation(value = "添加课程meta信息", notes="添加一个课程meta信息（相当于电商中添加一个商品），供学生选课，预定课程", 
			tags=SwaggerTags.COURSE_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/add", method=RequestMethod.POST)
	@ResponseBody
	public Object add(
			@ApiIgnore @Visitor Integer userId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "bid") Integer bookId,
			@RequestParam(value = "desc", required=false) String desc,
			@RequestParam(value = "image", required=false) MultipartFile imageFile,
			@RequestParam(value = "bigImage", required=false) MultipartFile bigImageFile,
			@RequestParam(value = "price") String price,
			@RequestParam(value = "level") Integer level,
			@RequestParam(value = "type") Integer type,
			@RequestParam(value = "subNotes") String subNotesStr,
			@RequestParam(value = "suitableCrowds") String suitableCrowds,
			@RequestParam(value = "shareJumpUrl", required=false) String shareJumpUrl,
			@RequestParam(value = "shareImage", required=false) MultipartFile shareImage,
			@RequestParam(value = "shareBrief", required=false) String shareBrief
			) {
		List<String> subNotes = splitSubNotes(subNotesStr);
		courseMetaService.add(userId, bookId, name, desc, imageFile, bigImageFile, level, type, price, subNotes, suitableCrowds,
				shareJumpUrl, shareImage, shareBrief
				);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "修改课程meta信息", tags=SwaggerTags.COURSE_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/modify", method=RequestMethod.POST)
	@ResponseBody
	public Object modify(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cmId", type=CourseMetaModel.class) Integer cmId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "desc") String desc,
			@RequestParam(value = "image", required=false) MultipartFile imageFile,
			@RequestParam(value = "bigImage", required=false) MultipartFile bigImageFile,
			@RequestParam(value = "price") String price,
			@RequestParam(value = "level") Integer level,
			@RequestParam(value = "type") Integer type,
			@RequestParam(value = "subNotes") String subNotesStr,
			@RequestParam(value = "suitableCrowds") String suitableCrowds,
			@RequestParam(value = "joinCnt", required=false) Integer joinCnt,
			@RequestParam(value = "shareJumpUrl", required=false) String shareJumpUrl,
			@RequestParam(value = "shareImage", required=false) MultipartFile shareImage,
			@RequestParam(value = "shareBrief", required=false) String shareBrief
			) {
		List<String> subNotes = splitSubNotes(subNotesStr);
		courseMetaService.modify(userId, cmId, name, desc, imageFile, bigImageFile, level, type, price, 
				subNotes, suitableCrowds , joinCnt, shareJumpUrl, shareImage, shareBrief);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "上架", tags=SwaggerTags.COURSE_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/shelve", method=RequestMethod.GET)
	@ResponseBody
	public Object shelve(
			
			@ApiIgnore @Visitor Integer adminId,
			@Uuid(value = "cmId", type=CourseMetaModel.class) Integer cmId) {
		
		courseMetaService.shelve(adminId, cmId);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "下架", tags=SwaggerTags.COURSE_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/unshelve", method=RequestMethod.POST)
	@ResponseBody
	public Object unshelve(
			@ApiIgnore @Visitor Integer adminId,
			@Uuid(value = "cmId", type=CourseMetaModel.class) Integer cmId
			) {
		courseMetaService.unshelve(adminId, cmId);
		
		return JsonResultView.SUCCESS;
	}
	
	private List<String> splitSubNotes(String subNotesStr) {
		List<String> result = new ArrayList<>(10);
		String[] parts = subNotesStr.split("(\r\n)+");
		for (String part : parts) {
			part = part.trim();
			if(StringUtils.isNotEmpty(part)) {
				result.add(part);
			}
		}
		
		return result;
	}
	
}
