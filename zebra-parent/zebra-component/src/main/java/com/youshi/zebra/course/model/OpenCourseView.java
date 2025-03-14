package com.youshi.zebra.course.model;

import java.util.List;

import com.youshi.zebra.image.model.ImageView;

import io.swagger.annotations.ApiModelProperty;

public class OpenCourseView{
	
	private String lmId;
	private String name;
	@ApiModelProperty(value = "描述")
	private String desc;
	private ImageView image;

	@ApiModelProperty(value = "参与人数")
	private Integer joinCnt;

	@ApiModelProperty(value = "开课时间")
	private String openTime;
	@ApiModelProperty(value = "价格")
	private String price;
	@ApiModelProperty(value = "是否购买  0 是  1 否")
	private Integer status;
	
	@ApiModelProperty(value = "课程类型  1 付费  2 试听 ")
	private Integer type;

	@ApiModelProperty(value = "适宜人群")
	private String suitableCrowds;
	
	@ApiModelProperty(value = "订阅须知")
	public List<String> subNotes;
	
	

	public String getLmId() {
		return lmId;
	}

	public void setLmId(String lmId) {
		this.lmId = lmId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public ImageView getImage() {
		return image;
	}

	public void setImage(ImageView image) {
		this.image = image;
	}

	public Integer getJoinCnt() {
		return joinCnt;
	}

	public void setJoinCnt(Integer joinCnt) {
		this.joinCnt = joinCnt;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSuitableCrowds() {
		return suitableCrowds;
	}

	public void setSuitableCrowds(String suitableCrowds) {
		this.suitableCrowds = suitableCrowds;
	}

	public List<String> getSubNotes() {
		return subNotes;
	}


	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setSubNotes(List<String> subNotes) {
		this.subNotes = subNotes;
	}
	
}
