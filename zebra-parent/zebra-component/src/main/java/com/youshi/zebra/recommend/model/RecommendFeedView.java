package com.youshi.zebra.recommend.model;

import com.youshi.zebra.image.model.ImageView;

public class RecommendFeedView {
	
	private String id;
	private String title;
	private Integer count;
	private String openTime;
	private String desc;
	private String url;
	private String pubTime;
	private ImageView image;
	private Integer dataType;
	private Integer status;
	
	
	
	
	
	
	public RecommendFeedView() {
		super();
	}
	public RecommendFeedView(String title, Integer count, String openTime, String desc, String url, String pubTime,
			ImageView image) {
		super();
		this.title = title;
		this.count = count;
		this.openTime = openTime;
		this.desc = desc;
		this.url = url;
		this.pubTime = pubTime;
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getJoinCnt() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPubTime() {
		return pubTime;
	}
	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}
	public ImageView getImage() {
		return image;
	}
	public void setImage(ImageView image) {
		this.image = image;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

}
