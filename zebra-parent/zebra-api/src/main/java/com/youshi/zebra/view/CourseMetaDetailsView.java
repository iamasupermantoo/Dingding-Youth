package com.youshi.zebra.view;

import java.util.Arrays;
import java.util.List;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseMetaModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 
 * 课程详细信息View
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public class CourseMetaDetailsView {
	private CourseMetaModel delegate;
	
	private ZebraBuildContext context;
	
	public CourseMetaDetailsView(CourseMetaModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}
	
	public String getCmId() {
		return delegate.getUuid();
	}
	public String getName() {
		return delegate.getName();
	}
	
	public String getDesc() {
		return delegate.getDesc();
	}
	
	public ImageView getImage() {
		Integer bigImageId = delegate.getBigImageId();
		if(bigImageId != null) {
			return new ImageView(context.getImage(delegate.getBigImageId()));
		}
		
		return new ImageView(context.getImage(delegate.getImageId()));
	}
	
	public Integer getTotalCnt() {
		return delegate.getTotalCnt();
	}
	
	public String getPrice() {
		return delegate.getPrice();
	}
	
	public Integer getLevel() {
		return delegate.getLevel();
	}
	
	@ApiModelProperty(value = "订阅须知，字符串列表")
	public List<String> getSubNotes() {
		return delegate.getSubNotes();
	}

	@ApiModelProperty(value = "适宜人群")
	public String getSuitableCrowds() {
		return delegate.getSuitableCrowds();
	}
	
	@ApiModelProperty(value = "参与人数")
	public Integer getJoinCnt() {
		return delegate.getJoinCount() == null ? 0 : delegate.getJoinCount();
	}
	
	@ApiModelProperty(value = "试听：1：非试听，2：试听")
	public Integer getType() {
		return delegate.getType();
	}
	
}
