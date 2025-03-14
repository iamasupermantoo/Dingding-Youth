package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.recommend.constants.RecommendFeedType;
import com.youshi.zebra.recommend.model.RecommendFeedModel;
import com.youshi.zebra.recommend.utils.RecommendHelper;

/**
 * 
 * 首页推荐FeedView，可以分为“单图”帖子、“文章”等多种类型。
 * 根据{@code type}区分
 * 
 * @author wangsch
 * @date 2016-09-26
 */
public abstract class RecommendFeedView {
	
	protected RecommendFeedModel delegate;
	
	protected ZebraBuildContext context;

	public RecommendFeedView(RecommendFeedModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}
	
	public String getId() {
		return RecommendHelper.getFeedUuid(
				RecommendFeedType.fromValue(delegate.getType()), delegate.getDataId());
	}
	
	public int getType() {
		return delegate.getType();
	}
	
	// 
	public ImageView getImage() {
		return ImageView.imageView;
	}
	
	public String getTitle() {
		return "";
	}
	
	
}
