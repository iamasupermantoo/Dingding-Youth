package com.youshi.zebra.wx.view;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.core.utils.DateTimeUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.order.model.OrderModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
@ApiModel(value = "订单列表中，代表一个订单")
public class OrderView {
	private OrderModel delegate;
	
	private CourseMetaModel courseMeta;
	
	private ZebraBuildContext context;
	
	public OrderView(OrderModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		
		this.courseMeta = context.getCourseMeta(delegate.getProductId());
	}
	
	@ApiModelProperty(value = "课程名")
	public String getCourse() {
		return courseMeta.getName();
	}
	
	@ApiModelProperty(value = "总课次")
	public Integer getTotalCnt() {
		return courseMeta.getTotalCnt();
	}
	
	@ApiModelProperty(value = "图")
	public ImageView getImage() {
		ImageModel image = context.getImage(courseMeta.getImageId());
		if(image == null) {
			return null;
		}
		return new ImageView(image);
	}
	
	@ApiModelProperty(value = "课程级别")
	public Integer getLevel() {
		return courseMeta.getLevel();
	}
	
	@ApiModelProperty(value = "课程id，订单中的商品id")
	public String getCmId() {
		return UuidUtils.getUuid(CourseMetaModel.class, delegate.getProductId());
	}
	
	// ---------------------------------------- order ----------------------------------------
	@ApiModelProperty(value = "订单id")
	public String getOid() {
		return delegate.getUuid();
	}
	
	@ApiModelProperty(value = "订单号")
	public String getOrderSn() {
		return delegate.getOrderSn();
	}
	
	@ApiModelProperty(value = "订单状态。0：用户提交了订单， 1：订单处理中，2：已确认（去支付），3：已完成，4：已关闭")
	public Integer getStatus() {
		return delegate.getStatus();
	}
	
	@ApiModelProperty(value = "支付状态。0：未支付， 1：已支付")
	public Integer getPayStatus() {
		return delegate.getPayStatus();
	}
	
	@ApiModelProperty(value = "价格，单位：元")
	public Integer getPrice() {
		return delegate.getTotalPrice() / 100;
	}
	
	@ApiModelProperty(value = "订单时间")
	public String getCreateTime() {
		return DateTimeUtils.getDate(delegate.getCreateTime());
	}
}
