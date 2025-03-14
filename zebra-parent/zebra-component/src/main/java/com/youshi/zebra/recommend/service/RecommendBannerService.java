package com.youshi.zebra.recommend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.event.utils.PerfUtils;
import com.ecyrd.speed4j.StopWatch;
import com.youshi.zebra.recommend.constants.BannerType;
import com.youshi.zebra.recommend.dao.RecommendBannerDAO;
import com.youshi.zebra.recommend.model.RecommendBannerModel;

/**
 * 首页轮播图，推荐服务
 * 
 * @author wangsch
 * @date 2016-10-12
 */
@Service
public class RecommendBannerService extends AbstractService<Integer, RecommendBannerModel>{
	
	@Autowired
	private RecommendBannerDAO recommendBannerDAO;
	
	@Override
	protected AbstractDAO<Integer, RecommendBannerModel> dao() {
		return recommendBannerDAO;
	}
	
	/**
	 * 获取所有在线的
	 * 
	 * @param type {@link BannerType}
	 */
	public List<RecommendBannerModel> getAllOnline(BannerType type) {
		StopWatch watcher = PerfUtils.getWatcher("RecommendBannerService.getAllOnline");
		List<RecommendBannerModel> all = recommendBannerDAO.getAllOnline(type);
		watcher.stop();
		return all;
	}
	
	public List<RecommendBannerModel> getAll(BannerType type) {
		StopWatch watcher = PerfUtils.getWatcher("RecommendBannerService.getAll");
		List<RecommendBannerModel> all = recommendBannerDAO.getAll(type);
		watcher.stop();
		return all;
	}
	
}
