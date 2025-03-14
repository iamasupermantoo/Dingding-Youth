package com.youshi.zebra.conroller.recommend;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.model.PageView;
import com.dorado.gotopage.constant.GotoPage;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.utils.RequestUtils;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.core.utils.DateTimeUtils;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.recommend.constants.BannerStatus;
import com.youshi.zebra.recommend.constants.BannerType;
import com.youshi.zebra.recommend.constants.RecommendFeedType;
import com.youshi.zebra.recommend.model.RecommendBannerModel;
import com.youshi.zebra.recommend.model.RecommendFeedModel;
import com.youshi.zebra.recommend.model.RecommendFeedView;
import com.youshi.zebra.recommend.service.RecommendAdminService;
import com.youshi.zebra.recommend.service.RecommendFeedService;
import com.youshi.zebra.view.RecommendBannerAdminView;

import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 首页推荐管理
 * 
 * @author wangsch
 * @date 2016-10-10
 */
@RequestMapping("/recommend/admin")
@Controller
public class RecommendAdminController {
	
	@Autowired
	private RecommendAdminService recommendAdminService;
	
	@Autowired
	private RecommendFeedService recommendFeedService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	// -----------------------------------------------feed----------------------------------------------
	@RequestMapping(value = "/trials", method=RequestMethod.GET)
	public Object trials(
			@ApiIgnore @Uuid(value = "cursor", type = RecommendFeedModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<RecommendFeedView, String> trials = recommendFeedService.getTrials(cursor, limit);
		ModelAndView mav = new ModelAndView("recommend/recommend_feed_list");
		
		mav.addObject("feeds", trials);
		return mav;
	}
	
	@RequestMapping(value = "/opencourses", method=RequestMethod.GET)
	public Object opencourses(
			@ApiIgnore @Uuid(value = "cursor", type = RecommendFeedModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<RecommendFeedView, String> openCourse = recommendFeedService.getOpenCourse(cursor, limit);
		ModelAndView mav = new ModelAndView("recommend/recommend_feed_list");
		
		mav.addObject("feeds", openCourse);
		return mav;
	}
	
	
	@RequestMapping(value = "/informations", method=RequestMethod.GET)
	public Object information(
			@ApiIgnore @Uuid(value = "cursor", type = RecommendFeedModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<RecommendFeedView, String> informations = recommendFeedService.getInformation(cursor, limit);
		
		ModelAndView mav = new ModelAndView("recommend/recommend_feed_list");
		mav.addObject("feeds", informations);
		return mav;
	}
	
	@RequestMapping(value = "/addFeedUI", method=RequestMethod.GET)
	public String addFeedUI() {
		return "recommend/feed_add";
	}
	
	/**
	 * 推荐feed到首页
	 */
	@RequestMapping(value = "/addFeed", method=RequestMethod.POST)
	@ResponseBody
	public Object addFeed(
			@RequestParam(value = "title", required=false) String title,
			@RequestParam(value = "desc", required=false) String desc,
			@RequestParam(value = "trialId", required=false) String trialUuid,
			@RequestParam(value = "trialType", required=false) String trialType,
			@ApiIgnore @Uuid(value = "liveId", type=LiveMetaModel.class, required=false) Integer liveId,
			@RequestParam(value = "type") RecommendFeedType type,
			@RequestParam(value = "image", required = false) MultipartFile image,
			@RequestParam(value = "url", required = false) String url
			) {
		recommendAdminService.addFeed(title, desc, trialUuid, liveId, trialType, type,image, url);
		
		return JsonResultView.SUCCESS;
	}
	
	/**
	 * 移除一个首页推荐feed
	 */
	@RequestMapping(value = "/removeFeed", method=RequestMethod.POST)
	@ResponseBody
	public Object removeFeed(
			@Uuid(value = "feeds[]", type=RecommendFeedModel.class) List<Integer> feedIds
			) {
		recommendAdminService.removeFeeds(feedIds);
		
		return JsonResultView.SUCCESS;
	}
	
	// ----------------------------------------------- banner ----------------------------------------------
	/**
	 * 首页焦点图查询
	 */
	@RequestMapping(value = "/query/banner", method=RequestMethod.GET)
	public ModelAndView bannerList(
			@RequestParam(value = "status", required=false) BannerStatus status,
			@RequestParam(value = "type") BannerType type
			) {
		ModelAndView mav = new ModelAndView("recommend/banner_list");
		List<RecommendBannerModel> modelList = recommendAdminService.queryBanners(status, type);
		List<Object> banners = viewBuilder.buildToList(modelList, 
				ExplicitViewMapper.getInstance().setViewClass(RecommendBannerAdminView.class));
		
		return mav.addObject("banners", banners).addObject("status", status)
				.addObject("currTime", DateTimeUtils.getDateTime(System.currentTimeMillis(), TimeUnit.SECONDS))
				.addObject("type", type)
				;
	}
	
	@RequestMapping(value = "/addBannerUI", method=RequestMethod.GET)
	public ModelAndView addBannerUI() {
		return new ModelAndView("recommend/banner_add");
	}
	
	/**
	 * 添加一个首页焦点图
	 */
	@RequestMapping(value = "/addBanner", method=RequestMethod.POST)
	@ResponseBody
	public Object addBanner(
			@RequestParam(value = "dataId", required = false) String dataId,	// dataId，目标的id标识，如：帖子id、用户id
			@RequestParam(value = "gotoPage") GotoPage gotoPageType,
			@RequestParam(value = "type") BannerType type,
			@RequestParam(value = "startTime", required=false) String startTime,
			@RequestParam(value = "endTime", required=false) String endTime,
			
			@RequestParam(value = "desc", required=false) String desc,
			
			@RequestParam(value = "image") MultipartFile image,
			HttpServletRequest request
			) {
		Map<String, Object> paramMap = RequestUtils.paramArrayToSingle(request);
		recommendAdminService.addBanner(dataId, gotoPageType, paramMap, type,
				startTime, endTime, desc, image);
		
		return JsonResultView.SUCCESS;
	}
	
	/**
	 * 保存banner的顺序
	 */
	@RequestMapping(value = "/saveOrder")
	@ResponseBody
	public Object saveOrder(
			@Uuid(type=RecommendBannerModel.class, value="ids[]") List<Integer> bannerIds
			) {
		recommendAdminService.saveBannerOrder(bannerIds);
		
		return JsonResultView.SUCCESS;
	}
	
	/**
	 * 下线banner
	 */
	@RequestMapping(value = "/offlineBanner", method=RequestMethod.POST)
	@ResponseBody
	public Object offlineBanner(
			@Uuid(value = "id", type=RecommendBannerModel.class) Integer bannerId
			) {
		recommendAdminService.setBannerStatus(bannerId, BannerStatus.Offline);
		
		return JsonResultView.SUCCESS;
	}
	/**
	 * 上线banner
	 */
	@RequestMapping(value = "/onlineBanner", method=RequestMethod.POST)
	@ResponseBody
	public Object onlineBanner(
			@Uuid(value = "id", type=RecommendBannerModel.class) Integer bannerId
			) {
		recommendAdminService.setBannerStatus(bannerId, BannerStatus.Online);
		return JsonResultView.SUCCESS;
	}
	
	/**
	 * 删除banner
	 */
	@RequestMapping(value = "/delBanner", method=RequestMethod.POST)
	@ResponseBody
	public Object delBanner(
			@Uuid(value = "id", type=RecommendBannerModel.class) Integer bannerId
			) {
		recommendAdminService.setBannerStatus(bannerId, BannerStatus.Deleted);
		return JsonResultView.SUCCESS;
	}
}
