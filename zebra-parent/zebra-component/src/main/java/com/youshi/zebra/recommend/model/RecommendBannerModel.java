package com.youshi.zebra.recommend.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;
import com.dorado.gotopage.constant.GotoPage;

/**
 * 
 * 
 * 
 * @author wangsch
 * @date 2016-10-12
 */
public class RecommendBannerModel extends AbstractModel<Integer>{
	public enum RecommendBannerKeys {
		status,
		type,
		image_id, 
		start_time, 
		end_time,
		
		// timeline banner显示在底部的描述
		desc,
	}
	
	/**
	 * 焦点图gotoPage目标id，不需要的话，存0
	 */
	private int dataId;
	
	/**
	 * {@link GotoPage#getValue()}值
	 * 
	 * @see #hasGotoPage()
	 * 
	 */
	private int gotoPageType;
	
	private String gotoPageParams;
	
	private int type;

	public RecommendBannerModel() {
		super();
	}

	public RecommendBannerModel(int id, String data, long createTime, int status,
			int dataId, int gotoPageType, String gotoPageParams, int type) {
		super(id, data, createTime, status);
		this.dataId = dataId;
		this.gotoPageType = gotoPageType;
		this.gotoPageParams = gotoPageParams;
		this.type = type;
		
	}

	public int getDataId() {
		return dataId;
	}

	public int getGotoPageType() {
		return gotoPageType;
	}

	public String getGotoPageParams() {
		return gotoPageParams;
	}
	
	public int getType() {
		return type;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	public void setGotoPageType(int gotoPageType) {
		this.gotoPageType = gotoPageType;
	}

	public void setGotoPageParams(String gotoPageParams) {
		this.gotoPageParams = gotoPageParams;
	}
	
	public Integer getImageId() {
		return ModelUtils.getInt(this, RecommendBannerKeys.image_id);
	}
	
	public Long getStartTime() {
		return ModelUtils.getLong(this, RecommendBannerKeys.start_time);
	}
	
	public Long getEndTime() {
		return ModelUtils.getLong(this, RecommendBannerKeys.end_time);
	}
	
	public String getDesc() {
		return ModelUtils.getString(this, RecommendBannerKeys.desc);
	}
}
