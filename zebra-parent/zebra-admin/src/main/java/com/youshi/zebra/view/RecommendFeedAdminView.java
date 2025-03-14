package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.recommend.constants.RecommendFeedType;
import com.youshi.zebra.recommend.model.RecommendFeedModel;

/**
 * 
 * 
 * 
 * @author wangsch
 * @date 2016-10-19
 */
public class RecommendFeedAdminView {
	private RecommendFeedModel delegate;
	
	private ZebraBuildContext context;
	
	public RecommendFeedAdminView(RecommendFeedModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		RecommendFeedType type = RecommendFeedType.fromValue(delegate.getType());
		
	}
	
	public String getId() {
		return delegate.getUuid();
	}
	
	public int getType() {
		return delegate.getType();
	}
	
//	public String getTitle() {
//		return 
//	}
	
	
	// TODO
	// ...
	
	
}
