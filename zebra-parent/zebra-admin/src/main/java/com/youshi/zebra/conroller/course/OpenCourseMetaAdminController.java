package com.youshi.zebra.conroller.course;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.course.model.OpenCourseMetaAdminView;
import com.youshi.zebra.course.service.LiveMetaService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 公开课
 * @author wangsch
 * @date 2017年2月18日
 */
@RestController
@RequestMapping("/opencourse/meta/admin")
public class OpenCourseMetaAdminController {
	
	@Autowired
	private LiveMetaService liveMetaService;
	
	@ApiOperation(value = "公开课meta列表查询", tags=SwaggerTags.COURSE_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "list", method=RequestMethod.GET)
	public ModelAndView list(
			
			@ApiIgnore @Uuid(value = "cursor", required = false, type=CourseModel.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit) {
		PageView<OpenCourseMetaAdminView, String> pageByCorsor = liveMetaService.getPageByCorsor(cursor, limit);
		ModelAndView mav = new ModelAndView("course/opencourse_meta_list");
		mav.addObject("pages",pageByCorsor);
		return mav;
	}
	
	
	
	@RequestMapping(value = "/add", method=RequestMethod.GET)
	public Object toAdd(){
		ModelAndView mav = new ModelAndView("course/opencourse_meta_add");
		return mav;
	}
	
	@ApiOperation(value = "添加公开课meta信息", notes="添加一个公开课", 
			tags=SwaggerTags.COURSE_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/add", method=RequestMethod.POST)
	@ResponseBody
	public Object add(
			@ApiIgnore @Visitor Integer userId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "desc", required=false) String desc,
			@RequestParam(value = "image", required=false) MultipartFile imageFile,
			@RequestParam(value = "bigImage", required=false) MultipartFile bigImageFile,
			@RequestParam(value = "shareImage", required=false) MultipartFile shareImage,
			@RequestParam(value = "price") String price,
			@RequestParam(value = "teacher") Integer teacher,
			@RequestParam(value = "level" , required=false) Integer level,
			@RequestParam(value = "type") Integer type,
			@RequestParam(value = "subNotes") String subNotesStr,
			@RequestParam(value = "suitableCrowds") String suitableCrowds,
			@RequestParam(value = "openTime", required=false) String openTime,
			@RequestParam(value = "joinCnt", required=false) Integer joinCnt,
			@RequestParam(value = "shareDesc", required=false) String shareDesc,
			@RequestParam(value = "shareUrl", required=false) String shareUrl
			) {
		List<String> subNotes = splitSubNotes(subNotesStr);
		liveMetaService.addLiveMeta(userId,teacher, name, desc, imageFile, bigImageFile,
				shareImage, level, type, openTime, joinCnt, shareDesc,
				shareUrl, price, subNotes, suitableCrowds);
		
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/edit", method=RequestMethod.GET)

	public Object toEdit(
			@RequestParam(value = "cmId", required=false) Integer cmId
			){
		OpenCourseMetaAdminView view = liveMetaService.getAdminOpenCourseDetail(cmId);
		ModelAndView mav = new ModelAndView("course/opencourse_meta_edit");
		mav.addObject("x", view);
		return mav;
	}
	
	@ApiOperation(value = "编辑公开课meta信息", notes="编辑公开课", 
			tags=SwaggerTags.COURSE_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/edit", method=RequestMethod.POST)
	@ResponseBody
	public Object edit(
			@ApiIgnore @Visitor Integer userId,
			@RequestParam(value = "cmId", required=false) Integer cmId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "desc", required=false) String desc,
			@RequestParam(value = "image", required=false) MultipartFile imageFile,
			@RequestParam(value = "bigImage", required=false) MultipartFile bigImageFile,
			@RequestParam(value = "shareImage", required=false) MultipartFile shareImage,
			@RequestParam(value = "price") String price,
			@RequestParam(value = "level", required=false) Integer level,
			@RequestParam(value = "type") Integer type,
			@RequestParam(value = "subNotes") String subNotesStr,
			@RequestParam(value = "suitableCrowds") String suitableCrowds,
			@RequestParam(value = "openTime", required=false) String openTime,
			@RequestParam(value = "joinCnt", required=false) Integer joinCnt,
			@RequestParam(value = "shareDesc", required=false) String shareDesc,
			@RequestParam(value = "shareUrl", required=false) String shareUrl
			) {
		List<String> subNotes = splitSubNotes(subNotesStr);
		liveMetaService.editLiveMeta(cmId, userId, name, desc, imageFile, bigImageFile, 
				shareImage, level, type, openTime, joinCnt, shareDesc, shareUrl, 
				price, subNotes, suitableCrowds);
		
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
			@RequestParam(value = "cmId", required=false) Integer cmId
			) {
		liveMetaService.unshelve(adminId, cmId); 
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "上架", tags=SwaggerTags.COURSE_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/shelve", method=RequestMethod.POST)
	@ResponseBody
	public Object shelve(
			@ApiIgnore @Visitor Integer adminId,
			@RequestParam(value = "cmId", required=false) Integer cmId
			) {
		liveMetaService.shelve(adminId, cmId); 
		
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
	
	
	
	
	@ApiOperation(value = "已购公开课列表查询", tags=SwaggerTags.COURSE_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "paidList", method=RequestMethod.GET)
	public ModelAndView paidList(
			
			@ApiIgnore @Uuid(value = "cursor", required = false, type=CourseModel.class) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit) {
		PageView<OpenCourseMetaAdminView, String> pageByCorsor = liveMetaService.getPaidByCorsor(cursor, limit);
		ModelAndView mav = new ModelAndView("course/opencourse_paid_list");
		mav.addObject("pages",pageByCorsor);
		return mav;
	}
	
	
}
