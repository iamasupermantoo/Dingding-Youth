package com.youshi.zebra.recommend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.model.PageView;
import com.youshi.zebra.core.utils.DateUtil;
import com.youshi.zebra.course.dao.CourseMetaDAO;
import com.youshi.zebra.course.dao.LiveMetaDAO;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.image.model.ImageView;
import com.youshi.zebra.image.service.ImageService;
import com.youshi.zebra.recommend.constants.RecommendFeedType;
import com.youshi.zebra.recommend.dao.RecommendFeedDAO;
import com.youshi.zebra.recommend.model.RecommendFeedModel;
import com.youshi.zebra.recommend.model.RecommendFeedModel.RecommendFeedKeys;
import com.youshi.zebra.recommend.model.RecommendFeedView;

/**
 * 首页Feed推荐服务
 * 
 * @author wangsch
 * @date 2016-09-24
 */
@Service
public class RecommendFeedService extends AbstractService<Integer, RecommendFeedModel>{
	
	@Autowired
	private RecommendFeedDAO recommendFeedDAO;
	
	@Autowired
	private LiveMetaDAO liveMetaDAO;
	
	@Autowired
	private CourseMetaDAO courseMetaDAO;
	
	@Autowired
	private ImageService imageService;
		
	@Override
	protected AbstractDAO<Integer, RecommendFeedModel> dao() {
		return recommendFeedDAO;
	}
	
	// --------------------------------------for admin--------------------------------------
	
	
	public PageView<RecommendFeedView, String> getTrials( Integer cursor, int limit ){
		WhereClause params = WhereClause.create();
		params.and().eq(RecommendFeedKeys.type,RecommendFeedType.TRY_COURSE.getValue());
		PageView<RecommendFeedModel, HasUuid<Integer>> pages = getByCursor(cursor, limit, params);
		
		List<RecommendFeedView> parseFeed = parseFeed(pages.getList());
		String cursorStr = pages.getNextCursor()==null?null: pages.getNextCursor().getUuid();
		PageView<RecommendFeedView, String> result = new PageView<>(parseFeed,cursorStr);
		
		return result;
	}
	
	public PageView<RecommendFeedView, String> getOpenCourse( Integer cursor, int limit ){
		WhereClause params = WhereClause.create();
		params.and().eq(RecommendFeedKeys.type,RecommendFeedType.OPEN_COURSE.getValue());
		PageView<RecommendFeedModel, HasUuid<Integer>> pages = getByCursor(cursor, limit, params);
		
		List<RecommendFeedView> parseFeed = parseFeed(pages.getList());
		String cursorStr = pages.getNextCursor()==null?null: pages.getNextCursor().getUuid();
		PageView<RecommendFeedView, String> result = new PageView<>(parseFeed,cursorStr);
		
		return result;
	}
	
	public PageView<RecommendFeedView, String> getInformation( Integer cursor, int limit ){
		WhereClause params = WhereClause.create();
		params.and().eq(RecommendFeedKeys.type, RecommendFeedType.WEIXIN_ARTICLE.getValue());
		PageView<RecommendFeedModel, HasUuid<Integer>> pages = getByCursor(cursor, limit, params);
		
		List<RecommendFeedView> parseFeed = parseFeed(pages.getList());
		String cursorStr = pages.getNextCursor()==null?null: pages.getNextCursor().getUuid();
		PageView<RecommendFeedView, String> result = new PageView<>(parseFeed,cursorStr);
		
		return result;
	}
	
	
	
	private List<RecommendFeedView> parseFeed( List<RecommendFeedModel> list ) {
		
		List<RecommendFeedView> newList = new ArrayList<RecommendFeedView>();
		
		for (RecommendFeedModel model : list) {
			
			RecommendFeedView view = getView(model);
			newList.add(view);
		}
		
		return newList;
	}
	
	private RecommendFeedView getView(RecommendFeedModel model){
		RecommendFeedView view = new RecommendFeedView();
		
		switch (model.getDataType()) {
		case 0:
			CourseMetaModel course = courseMetaDAO.getById(model.getDataId());
			view.setId(course.getUuid());
			view.setTitle(course.getName());
			view.setCount(course.getJoinCount());
			view.setDesc(course.getDesc());
			view.setImage(new ImageView(imageService.getById(course.getImageId())));
			view.setDataType(0);
			break;
		case 1:
			LiveMetaModel live = liveMetaDAO.getById(model.getDataId());
			view.setId(live.getUuid());
			view.setTitle(live.getName());
			view.setCount(live.getJoinCount());
			view.setDesc(live.getDesc());
			Date parse = DateUtil.parse(live.getOpenTime(), "yyyy-MM-dd hh:mm:ss.S");
			String format = DateUtil.format(parse, "MM/dd hh:mm");
			view.setOpenTime(format);
			view.setImage(new ImageView(imageService.getById(live.getImageId())));
			view.setDataType(1);
			break;
		default:
			view.setId(model.getUuid());
			view.setTitle(model.getTitle());
			view.setUrl(model.getUrl());
			Date parsePub = DateUtil.parse(model.getPubTime(), "yyyy-MM-dd hh:mm:ss");
			String formatPub = DateUtil.format(parsePub, "yyyy-MM-dd hh:mm");
			view.setPubTime(formatPub);
			view.setImage(new ImageView(imageService.getById(model.getImageId())));
			view.setDataType(2);
			break;
		}
		return view;
	}
	
/*	public ThreeTuple<RecommendFeedModel, RecommendFeedModel, RecommendFeedModel> getFeeds(){
		//ThreeTuple<A, B, C> tuple = new ThreeTuple<A, B, C>(a, b, c);
		return null;
	}*/
	
	
}
