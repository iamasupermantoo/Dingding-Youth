package com.youshi.zebra.conroller.book;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.book.model.FrameItem;
import com.youshi.zebra.book.service.FrameConfigService;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.media.constants.MediaType;
import com.youshi.zebra.media.service.MediaService;
import com.youshi.zebra.view.FrameItemView;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
@RequestMapping(value = "/frame/config")
@Controller
public class FrameConfigController {
	@Autowired
	private ViewBuilder viewBuilder;
	
	@Autowired
	private FrameConfigService frameConfigService;
	
	@Autowired
	private MediaService mediaService;
	
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public Object list(
			@RequestParam(value = "cid") Integer chapterId
			) {
		List<FrameItem> frames = frameConfigService.getFrames(chapterId);
		List<Object> list = viewBuilder.buildToList(frames, 
				ExplicitViewMapper.getInstance().setViewClass(FrameItemView.class));
		
		ModelAndView mav = new ModelAndView("book/frame_config_list");
		mav.addObject("frames", list);
		
		return mav;
	}
	
	@ApiOperation(value = "保存顺序", tags=SwaggerTags.BOOK_ADMIN)
	@RequestMapping(value = "/saveIdx", method=RequestMethod.POST)
	@ResponseBody
	public Object saveIdx(
			@RequestParam(value = "cid") Integer chapterId,
			@RequestParam(value = "idxes") List<String> rawIdxes
			) {
		frameConfigService.saveIdx(chapterId, rawIdxes);
		
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/add", method=RequestMethod.POST)
	@ResponseBody
	public Object add(
			@Visitor Integer userId,
			@RequestParam(value = "cid") Integer chapterId,
			@RequestParam(value = "parentIdx", required=false) Integer parentIdx, 
			@RequestParam(value = "mediaFile") MultipartFile mediaFile
			) {
		Integer mediaId = mediaService.createMedia(userId, 
				StringUtils.EMPTY, StringUtils.EMPTY, mediaFile);
		
		frameConfigService.addFrame(chapterId, mediaId, parentIdx);
		
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/update", method=RequestMethod.POST)
	@ResponseBody
	public Object update(
			@Visitor Integer userId,
			@RequestParam(value = "cid") Integer chapterId,
			@RequestParam(value = "parentIdx") Integer parentIdx,
			@RequestParam(value = "childIdx", required=false) Integer childIdx,
			@RequestParam(value = "mediaFile") MultipartFile mediaFile
			) {
		Integer mediaId = mediaService.createMedia(userId, 
				StringUtils.EMPTY, StringUtils.EMPTY, mediaFile);
		frameConfigService.updateFrame(chapterId, mediaId, parentIdx, childIdx);
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/remove", method=RequestMethod.POST)
	@ResponseBody
	public Object remove(
			@RequestParam(value = "cid") Integer chapterId,
			@RequestParam(value = "parentIdx") Integer parentIdx,
			@RequestParam(value = "childIdx", required=false) Integer childIdx
			) {
		frameConfigService.removeFrame(chapterId, parentIdx, childIdx);
		return JsonResultView.SUCCESS;
	}
}
