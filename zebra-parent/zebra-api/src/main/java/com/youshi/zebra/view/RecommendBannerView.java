package com.youshi.zebra.view;

import java.util.HashMap;

import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.recommend.model.RecommendBannerModel;

/**
 * 推荐头部轮播图
 * 
 * @author wangsch
 * @date 2016-10-12
 */
public class RecommendBannerView {
	private RecommendBannerModel delegate;
	
	private ZebraBuildContext context;
	
	public RecommendBannerView(RecommendBannerModel delegate, 
			ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}
	
	public ImageView getImage() {
		return new ImageView(context.getImage(delegate.getImageId()));
	}
	
	public GotoPageView getGotoPage() {
		return new GotoPageView(delegate.getGotoPageType(), 
				ObjectMapperUtils.fromJSON(delegate.getGotoPageParams(), HashMap.class, String.class, Object.class));
	}
	
	public String getDesc() {
		return delegate.getDesc();
	}
}
