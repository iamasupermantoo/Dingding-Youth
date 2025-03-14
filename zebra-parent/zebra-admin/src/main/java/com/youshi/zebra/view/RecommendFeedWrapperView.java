package com.youshi.zebra.view;

import java.util.List;

/**
 * 
 * 
 * 
 * @author wangsch
 * @date 2016-10-19
 */
public class RecommendFeedWrapperView {
	
	public RecommendFeedAdminView article;
	
	public List<RecommendFeedAdminView> twoPictures;
	
	public RecommendFeedWrapperView(RecommendFeedAdminView article) {
		this.article = article;
	}
	
	public RecommendFeedWrapperView(List<RecommendFeedAdminView> twoPictures) {
		this.twoPictures = twoPictures;
	}
	
	public RecommendFeedAdminView getArticle() {
		return article;
	}
	public List<RecommendFeedAdminView> getTwoPictures() {
		return twoPictures;
	}
}
