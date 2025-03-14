package com.youshi.zebra.recommend.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.dorado.gotopage.GotoPageHelper;
import com.dorado.gotopage.GotoPageHelper.ParsedGotoPage;
import com.dorado.gotopage.constant.GotoPage;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.ecyrd.speed4j.StopWatch;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.course.service.LiveMetaService;
import com.youshi.zebra.image.exception.ImageUploadException;
import com.youshi.zebra.image.service.ImageService;
import com.youshi.zebra.recommend.constants.BannerStatus;
import com.youshi.zebra.recommend.constants.BannerType;
import com.youshi.zebra.recommend.constants.RecommendFeedType;
import com.youshi.zebra.recommend.dao.RecommendBannerDAO;
import com.youshi.zebra.recommend.dao.RecommendFeedDAO;
import com.youshi.zebra.recommend.dao.RecommendFeedDAO.RecommendFeedStatus;
import com.youshi.zebra.recommend.exception.RecommendFeedAlreadyExistException;
import com.youshi.zebra.recommend.model.RecommendBannerModel;
import com.youshi.zebra.recommend.model.RecommendBannerModel.RecommendBannerKeys;
import com.youshi.zebra.recommend.model.RecommendFeedModel;

/**
 * 首页推荐管理服务
 * 
 * @author wangsch
 * @date 2016-10-12
 */
@Service
public class RecommendAdminService {
	private static final Logger logger = LoggerFactory.getLogger(RecommendAdminService.class);
	
	@Autowired
	private RecommendBannerService recommendBannerService;
	
	@Autowired
	private GotoPageHelper gotoPageHelper;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private RecommendBannerDAO recommendBannerDAO;
	
	@Autowired
	private RecommendFeedDAO recommendFeedDAO;
	
	@Autowired
	private CourseMetaService courseMetaService;
	
	@Autowired
	private LiveMetaService liveMetaService;
	
	public void addFeed(String title, String desc,
			String trialUuid, Integer liveId, String trialType, RecommendFeedType type, MultipartFile image, String url) {
		StopWatch watcher = PerfUtils.getWatcher("RecommendAdminService.addFeed");
		
		FeedParsed parsed = parseFeed(title, desc, trialUuid, liveId, trialType, type, url, image);
		RecommendFeedModel feed = recommendFeedDAO.getFeed(parsed.dataIntId, parsed.type);
		if(type != RecommendFeedType.WEIXIN_ARTICLE && feed != null) {
			logger.error("Feed already recommended, ignore. parsedFeed: {}", parsed);
			throw new RecommendFeedAlreadyExistException();
		}
		
		recommendFeedDAO.insert(parsed.dataIntId, parsed.authorId, parsed.type, RecommendFeedStatus.Normal, 
				parsed.data, System.currentTimeMillis());
		
		watcher.stop();
	}
	
	private static final List<Integer> DELETE_FEED_AFFECT_ROWS = Arrays.asList(0, 1);
	public void removeFeedByDataId(Integer dataId, RecommendFeedType type) {
		StopWatch watcher = PerfUtils.getWatcher("RecommendAdminService.removeFeedByDataId");
		
		int row = recommendFeedDAO.delete(dataId, type);
		DAOUtils.checkAffectRows(row, DELETE_FEED_AFFECT_ROWS);
		
		watcher.stop();
		logger.info("Remove feed by data id succ. dataId: {}, type: {}", dataId, type);
	}
	
	
	public void removeFeeds(List<Integer> feedIds) {
		for (Integer feedId : feedIds) {
			int row = recommendFeedDAO.delete(feedId);
			DAOUtils.checkAffectRows(row);
		}
		logger.info("Succ remove removeFeeds . feedIds: {}", feedIds);
	}
	
	/**
	 * 分页查询banner列表
	 * 
	 * @param status		状态
	 * @return				
	 */
	public List<RecommendBannerModel> queryBanners(BannerStatus status, BannerType type) {
		StopWatch watcher = PerfUtils.getWatcher("RecommendAdminService.queryBanners");
		
		WhereClause params = WhereClause.create()
				.and().eq(RecommendBannerKeys.type, type.getValue());
		if(status != null) {
			params.and().eq(RecommendBannerKeys.status, status.getValue());
		}
		List<RecommendBannerModel> result = recommendBannerService.getAll(type);
		
		watcher.stop();
		return result;
	}

	/**
	 * 添加一个首页顶部焦点图
	 * 
	 * @param dataId					目标id，字符串
	 * @param gotoPageType		gotoPage类型
	 * @param requestParams		gotoPage参数
	 * @param startTime				上线时间
	 * @param endTime				下线时间
	 * @param image					轮播图片
	 */
	public void addBanner(String dataId, GotoPage gotoPageType, Map<String, Object> requestParams, 
			BannerType type, String startTime, String endTime, String desc, MultipartFile image) {
		StopWatch watcher = PerfUtils.getWatcher("RecommendAdminService.addBanner");
		Map<RecommendBannerKeys, Object> dataMap = new HashMap<>();
		ParsedGotoPage gotoPage = gotoPageHelper.parseGotoPage(dataId, gotoPageType, requestParams);
		Integer imageId = mayCreateImage(image);
		
		dataMap.put(RecommendBannerKeys.image_id, imageId);
		dataMap.put(RecommendBannerKeys.desc, desc);
		
		String data = ObjectMapperUtils.toJSON(dataMap);
		String gotoPageParams = ObjectMapperUtils.toJSON(gotoPage.gotoPageParams);
		recommendBannerDAO.insert(gotoPage.dataIntId, gotoPage.gotoPageType, gotoPageParams, type, 0, BannerStatus.Online, 
				data, System.currentTimeMillis());
		
		watcher.stop();
	}
	
	public void setBannerStatus(Integer bannerId, BannerStatus status) {
		recommendBannerDAO.setStatus(bannerId, status);
	}
	
	public void saveBannerOrder(List<Integer> bannerIds) {
		StopWatch watcher = PerfUtils.getWatcher("RecommendAdminService.saveBannerOrder");
		
		int rowAffect = recommendBannerDAO.saveOrders(bannerIds);
		DAOUtils.checkAffectRows(rowAffect, bannerIds.size());
		
		logger.info("Save banner order succ. bannerIds: {}", bannerIds);
		watcher.stop();
	}
	
	/**
	 * 
	 * 推荐feed，把输入的原始入参，根据{@link RecommendFeedType}解析为{@link FeedParsed}。
	 * 
	 * @return
	 */
	private FeedParsed parseFeed(String title, String desc, 
			String trialUuid, Integer liveId, String trialType, RecommendFeedType type, String url, MultipartFile image) {
		Integer dataIntId = null;	// 占位
		Integer authorId = 1;	// 写死，这个字段暂不使用
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("title", title);
		dataMap.put("desc", desc);
		
		Integer cmId = null;
		Integer openCourseId = null;
		switch(type)  {
		case TRY_COURSE:
			if("opencourse".equals(trialType)) {
				dataIntId = UuidUtils.toIntId(LiveMetaModel.class, trialUuid);
				dataMap.put("data_type", 1);
				
				openCourseId = dataIntId;
			} else if("course".equals(trialType)) {
				dataIntId = UuidUtils.toIntId(CourseMetaModel.class, trialUuid);
				dataMap.put("data_type", 0);
				
				cmId = dataIntId;
			}
			break;
			
		case OPEN_COURSE:
			dataIntId = liveId;
			dataMap.put("data_type", 1);
			
			openCourseId = dataIntId;
			break;
			
		case WEIXIN_ARTICLE:
			dataMap.put("url", url);
			dataMap.put("data_type", 2);
			break;
			
		default:
			throw new UnsupportedOperationException("Invalid recommend feed type: "+ type);
		}
		
		Integer imageId = getOrCreateImageId(image, cmId, openCourseId);
		dataMap.put("image_id", imageId);
		String data = DoradoMapperUtils.toJSON(dataMap);
		
		return new FeedParsed(dataIntId, authorId, data, type);
	}
	
	/**
	 * @param image
	 * @param dataMap
	 */
	private Integer getOrCreateImageId(MultipartFile image, Integer cmId, Integer openCourseId) {
		if(cmId != null) {
			CourseMetaModel cm = courseMetaService.getById(cmId);
			if(cm != null) {
				return cm.getImageId();
			}
		}
		
		if(openCourseId != null) {
			LiveMetaModel openCourse = liveMetaService.getById(openCourseId);
			if(openCourse != null) {
				return openCourse.getImageId();
			}
		}
		
		try {
			int imageId = imageService.createImage(WebRequestContext.getUserId(), image.getBytes());
			return imageId;
		} catch (IOException e) {
			return null;
		}
	}
	
	public class FeedParsed {
		public final Integer dataIntId;
		public final Integer authorId;
		public final String data;
		public final RecommendFeedType type;
		public FeedParsed(Integer dataIntId, Integer authorId, String data, RecommendFeedType type) {
			this.dataIntId = dataIntId;
			this.authorId = authorId;
			this.data = data;
			this.type = type;
		}
		
		public boolean isValid() {
			return dataIntId != null && authorId != null;
		}
		
		@Override
		public String toString() {
			return ObjectMapperUtils.toJSON(this);
		}
		
	}

	private Integer mayCreateImage(MultipartFile image) {
		try{
			Integer userId = WebRequestContext.getUserId();
			if(image != null && ArrayUtils.isNotEmpty(image.getBytes())) {
				return imageService.createImage(userId, image.getBytes());
			}
		} catch(Exception e) {
			logger.error("Ops when upload image.", e);
		}
		throw new ImageUploadException();
	}
	
	public class BannerParsed {
		public final Integer dataIntId;
		public final Integer order;
		public final GotoPage gotoPageType;
		public final Map<String, Object> gotoPageParams;
		public final String data;
		
		public BannerParsed(Integer dataIntId, Integer order, 
				GotoPage gotoPageType, Map<String, Object> gotoPageParams,
				String data) {
			this.dataIntId = dataIntId;
			this.order = order;
			this.gotoPageType = gotoPageType;
			this.gotoPageParams = gotoPageParams;
			this.data = data;
		}
		
		public boolean isValid() {
			return dataIntId != null && gotoPageType != null && MapUtils.isNotEmpty(gotoPageParams);
		}
		
	}
	
	
}
