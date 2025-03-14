package com.youshi.zebra.controller.recommend;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.recommend.constants.BannerType;
import com.youshi.zebra.recommend.model.RecommendBannerModel;
import com.youshi.zebra.recommend.model.RecommendFeedModel;
import com.youshi.zebra.recommend.model.RecommendFeedView;
import com.youshi.zebra.recommend.service.RecommendFeedService;
import com.youshi.zebra.recommend.service.RecommendService;
import com.youshi.zebra.view.ImageView;
import com.youshi.zebra.view.RecommendBannerView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 首页推荐相关
 * 
 * @author wangsch
 * @date 2016-09-24
 */
@RequestMapping(value = "/recommend")
@RestController
public class RecommendController {
	@Autowired
	private RecommendService recommendService;
	
	
	@Autowired
	private RecommendFeedService recommendFeedService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
//	private OverrideViewMapper feedMapper;
//	
//	private OverrideViewMapper feedMapperV2;
//	
//	@PostConstruct
//	public void init() {
//		feedMapper = new OverrideViewMapper(viewBuilder.mapper())
//				.addMapper(RecommendFeedArticleModel.class, RecommendFeedArticleView::new)
//				.addMapper(RecommendFeedPictureModel.class, RecommendFeedPictureView::new);
//		
//		feedMapperV2 = new OverrideViewMapper(viewBuilder.mapper())
//				.addMapper(RecommendFeedArticleModel.class, RecommendFeedArticleViewV2::new)
//				.addMapper(RecommendFeedPictureModel.class, RecommendFeedPictureViewV2::new)
//				.addMapper(RecommendFeedPicturesModel.class, RecommendFeedPicturesViewV2::new);
//	}
	
	@ApiOperation(value = "首页焦点图", 
			tags={ZebraCommonApiTags.FINAL}, response=RecommendBannerView.class)
	@RequestMapping(value = "/banners", method=RequestMethod.GET)
	public Object banners() {
		List<RecommendBannerModel> bannerModels = recommendService.getOnlineBanners(BannerType.APP);
		JsonResultView banners = viewBuilder.build(bannerModels, "banners", 
				ExplicitViewMapper.getInstance().setViewClass(RecommendBannerView.class));
		
		return banners;
	}
	
	@ApiOperation(value = "首页feed", 
			tags={ZebraCommonApiTags.FINAL})
	@RequestMapping(value = "/feeds", method=RequestMethod.GET)
	public Object feeds() {
		
		PageView<RecommendFeedView, String> trials = recommendFeedService.getTrials(null, 2);
		
		PageView<RecommendFeedView, String> openCourse = recommendFeedService.getOpenCourse(null, 2);
		
		PageView<RecommendFeedView, String> information = recommendFeedService.getInformation(null, 2);
		
		JsonResultView result = new JsonResultView();
		
		result.addValue("trials",trials.getList() );
		result.addValue("opencourses",openCourse.getList());
		result.addValue("informations", information.getList());
		
		result.addValue("video", "http://vi.src.ddlad.com/ddlad-intro.mp4");
		
		return result;
	}
	
	
	@ApiOperation(value = "试听更多", 
			tags={ZebraCommonApiTags.FINAL})
	@ApiImplicitParams({
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", required=false, paramType="query"),
	})
	@RequestMapping(value = "/trials", method=RequestMethod.GET)
	public Object trials(
			@ApiIgnore @Uuid(value = "cursor", type = RecommendFeedModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<RecommendFeedView, String> trials = recommendFeedService.getTrials(cursor, limit);
		JsonResultView result = new JsonResultView();
		result.addValue("trials",trials);
		return result;
	}
	
	
	@ApiOperation(value = "公开课信息更多", 
			tags={ZebraCommonApiTags.FINAL})
	@ApiImplicitParams({
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", required=false, paramType="query"),
	})
	@RequestMapping(value = "/opencourses", method=RequestMethod.GET)
	public Object opencourses(
			@ApiIgnore @Uuid(value = "cursor", type = RecommendFeedModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<RecommendFeedView, String> openCourse = recommendFeedService.getOpenCourse(cursor, limit);
		JsonResultView result = new JsonResultView();
		result.addValue("opencourses", openCourse );
		return result;
	}
	
	
	@ApiOperation(value = "information资讯更多", 
			tags={ZebraCommonApiTags.FINAL})
	@ApiImplicitParams({
		@ApiImplicitParam(value = "cursor", name="cursor", dataType="String", required=false, paramType="query"),
	})
	@RequestMapping(value = "/informations", method=RequestMethod.GET)
	public Object information(
			@ApiIgnore @Uuid(value = "cursor", type = RecommendFeedModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<RecommendFeedView, String> information = recommendFeedService.getInformation(cursor, limit);
		JsonResultView result = new JsonResultView();
		result.addValue("informations", information);
		return result;
	}
	
	
	
	public class FeedView {
		
		private Integer id;
		private String title;
		private Integer count;
		private String openTime;
		private String desc;
		private String url;
		private String pubTime = "2017年7月5日";
		private ImageView image;

		public ImageView getImage() {
			return ImageView.imageView;
		}


		public FeedView(String title, Integer count, String openTime, String desc, String url) {
			super();
			this.title = title;
			this.count = count;
			this.openTime = openTime;
			this.desc = desc;
			this.url = url;
		}


		public String getTitle() {
			return title;
		}


		public void setTitle(String title) {
			this.title = title;
		}


		public Integer getJoinCnt() {
			return count;
		}


		public void setCount(Integer count) {
			this.count = count;
		}


		public String getOpenTime() {
			return openTime;
		}


		public void setOpenTime(String openTime) {
			this.openTime = openTime;
		}


		public String getDesc() {
			return desc;
		}


		public void setDesc(String desc) {
			this.desc = desc;
		}


		public String getUrl() {
			return url;
		}


		public void setUrl(String url) {
			this.url = url;
		}
		
		public String getPubTime() {
			return url==null?null : pubTime;
		}
		
		public Integer getDataType() {
			return 1;
		}
		
		public String getId() {
			return "asdfar-wef-gawsr";
		}

	}
	
}
