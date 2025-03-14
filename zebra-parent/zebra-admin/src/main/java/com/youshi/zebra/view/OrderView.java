package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.user.model.UserModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
public class OrderView {
	private OrderModel delegate;
	
	private ZebraBuildContext context;
	
	public OrderView(OrderModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}
	
	public String getOid() {
		return delegate.getUuid();
	}
	
	public String getOrderSn() {
		return delegate.getOrderSn();
	}
	
	public Integer getStatus() {
		return delegate.getStatus();
	}
	
	public Integer getPayStatus() {
		return delegate.getPayStatus();
	}
	
	public Integer getPayChannel() {
		return delegate.getPayChannel();
	}
	
	public Integer getChatResult() {
		return delegate.getChatResult();
	}
	
	public String getRemark() {
		return delegate.getRemark();
	}
	
	public String getTotalPrice() {
		return (delegate.getTotalPrice() / 100) + "元";
	}
	
	public long getCreateTime() {
		return delegate.getCreateTime();
	}
	
	public StudentInfoView getStudent() {
		UserModel user = context.getUser(delegate.getUserId());
		if (user == null) {
			return null;
		}
		return new StudentInfoView(user, context);
	}
	
	public CourseMetaAdminView getCourseMeta() {
		CourseMetaModel courseMeta = context.getCourseMeta(delegate.getProductId());
		if (courseMeta == null) {
			return null;
		}
		return new CourseMetaAdminView(courseMeta, context);
	}
	
}
