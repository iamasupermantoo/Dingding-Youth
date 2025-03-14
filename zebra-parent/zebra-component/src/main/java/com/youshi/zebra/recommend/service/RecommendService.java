package com.youshi.zebra.recommend.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.event.utils.PerfUtils;
import com.ecyrd.speed4j.StopWatch;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.recommend.constants.BannerType;
import com.youshi.zebra.recommend.constants.RecommendFeedType;
import com.youshi.zebra.recommend.dao.RecommendFeedDAO;
import com.youshi.zebra.recommend.model.RecommendBannerModel;


/**
 * 首页推荐服务
 * 
 * @author wangsch
 * @date 2016-09-26
 */
@Service
public class RecommendService {
	private static final Logger logger = LoggerFactory.getLogger(RecommendService.class);
	
	@Autowired
	private RecommendFeedService recommendFeedService;
	
	@Autowired
	private RecommendBannerService recommendBannerService;
	
	@Autowired
	private RecommendFeedDAO recommendFeedDAO;
	
	/**
	 * 头部轮播图
	 * 
	 * @return
	 */
	public List<RecommendBannerModel > getOnlineBanners(BannerType type) {
		StopWatch watcher = PerfUtils.getWatcher("RecommendService.getOnlineBanners");
		List<RecommendBannerModel> result = recommendBannerService.getAllOnline(type);
		
		watcher.stop();
		return result;
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
}
