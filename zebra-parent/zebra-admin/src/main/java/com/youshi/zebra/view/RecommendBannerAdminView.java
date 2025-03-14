package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.recommend.model.RecommendBannerModel;

public class RecommendBannerAdminView {
	private RecommendBannerModel model;
	private ZebraBuildContext context;
	
	public RecommendBannerAdminView(RecommendBannerModel model, 
			ZebraBuildContext context) {
		this.model = model;
		this.context = context;
	}
	
	public String getId() {
		return model.getUuid();
	}
	
	public Integer getGotoPageType() {
		return model.getGotoPageType();
	}
	
	public Integer getType() {
		return model.getType();
	}
	
	public Integer getStatus() {
		return model.getStatus();
	}
	
	public ImageView getImage() {
		return new ImageView(context.getImage(model.getImageId()));
	}
	
	public Long getStartTime() {
		return model.getStartTime();
	}
	
	public Long getEndTime() {
		return model.getEndTime();
	}
	
}
