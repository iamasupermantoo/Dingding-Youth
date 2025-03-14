package com.youshi.zebra.conroller.media;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.conroller.LoginRequiredController;
import com.youshi.zebra.media.constants.MediaType;
import com.youshi.zebra.media.model.MediaModel;
import com.youshi.zebra.media.service.MediaService;
import com.youshi.zebra.view.MediaView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 
 * 媒体库管理
 * 
 * @author wangsch
 * @date 2017年3月7日
 */
@RequestMapping(value = "/media/admin")
@RestController
@Deprecated
public class MediaAdminController extends LoginRequiredController {
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "媒体列表查询", tags=SwaggerTags.MEDIA_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "type", required=false) MediaType type,
			@Uuid(value = "limit", type=MediaModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<MediaModel, HasUuid<Integer>> page = mediaService.getMedias(type, cursor, limit);
		Map<String, Object> resultMap = viewBuilder.buildToMap(page, "medias", 
				ExplicitViewMapper.getInstance().setViewClass(MediaView.class));
		
		ModelAndView mav = new ModelAndView("media/media_list");
		mav.addAllObjects(resultMap);
		
		return mav;
	}
	
//	@ApiOperation(value = "媒体添加", tags=SwaggerTags.MEDIA_ADMIN)
//	@ApiImplicitParams({
//		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
//	})
//	@RequestMapping(value = "/add", method=RequestMethod.POST)
//	public Object add(
//			@ApiIgnore @Visitor Integer loginAdminId,
//			@RequestParam(value = "name") String name,
//			@RequestParam(value = "desc", required=false) String desc,
//			@RequestParam(value = "type") MediaType type,
//			@RequestParam(value = "file" ) MultipartFile mediaFile
//			) {
//		
//		mediaService.createMedia(loginAdminId, name, desc, type, mediaFile);
//		
//		return JsonResultView.SUCCESS;
//	}
	
	@ApiOperation(value = "媒体更新", tags=SwaggerTags.MEDIA_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/update", method=RequestMethod.POST)
	public Object update(
			@ApiIgnore @Visitor Integer loginAdminId,
			@RequestParam(value = "mid") Integer mediaId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "desc", required=false) String desc,
			@RequestParam(value = "type") MediaType type,
			@RequestParam(value = "file" ) MultipartFile mediaFile
			) {
		mediaService.updateMedia(loginAdminId, mediaId, name ,desc, type, mediaFile);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "媒体删除", tags=SwaggerTags.MEDIA_ADMIN)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/delete", method=RequestMethod.POST)
	public Object delete(
			@RequestParam(value = "mid") Integer mediaId) {
		mediaService.deleteMedia(mediaId);
		
		return JsonResultView.SUCCESS;
	}
}
