package com.youshi.zebra.view;

import java.util.List;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseMetaModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 
 * 课程详细信息View
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@ApiModel(value = "公开课列表，一个课程详情")
public class CourseMetaDetailsView {
	private CourseMetaModel delegate;
	
	private ZebraBuildContext context;
	
	public CourseMetaDetailsView(CourseMetaModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}
	
	@ApiModelProperty(value = "课程详情id，下单时的“商品id”")
	public String getCmId() {
		return delegate.getUuid();
	}
	
	@ApiModelProperty(value = "课程名")
	public String getName() {
		return delegate.getName();
	}
	
	@ApiModelProperty(value = "描述")
	public String getDesc() {
		return delegate.getDesc();
	}
	
	@ApiModelProperty(value = "图")
	public ImageView getImage() {
		return new ImageView(context.getImage(delegate.getImageId()));
	}
	
	@ApiModelProperty(value = "总课次")
	public Integer getTotalCnt() {
		return delegate.getTotalCnt();
	}
	
	@ApiModelProperty(value = "级别")
	public Integer getLevel() {
		return delegate.getLevel();
	}
	
	@ApiModelProperty(value = "订阅须知，字符串数组")
	public List<String> getSubNotes() {
		return delegate.getSubNotes();
	}

	@ApiModelProperty(value = "适宜人群，字符串")
	public String getSuitableCrowds() {
		return delegate.getSuitableCrowds();
	}
}
