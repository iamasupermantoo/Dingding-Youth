package com.youshi.zebra.view;

import com.youshi.zebra.admin.log.constants.AdminLogType;
import com.youshi.zebra.admin.log.model.AdminLogModel;
import com.youshi.zebra.core.web.view.ZebraBuildContext;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月25日
 */
public class AdminLogView {
		private AdminLogModel delegate;
		private ZebraBuildContext context;
		
		public AdminLogView(AdminLogModel delegate, ZebraBuildContext context) {
			this.delegate = delegate;
			this.context = context;
		}
		
		public AdminLogModel getDelegate() {
			return delegate;
		}
		public void setDelegate(AdminLogModel delegate) {
			this.delegate = delegate;
		}
		public ZebraBuildContext getContext() {
			return context;
		}
		public void setContext(ZebraBuildContext context) {
			this.context = context;
		}
		
		public AdminUserInfoView getAdminUser() {
			return new AdminUserInfoView(context.getUser(delegate.getAdminId()), context) ;
		}
		
		public long getCreateTime() {
			return delegate.getCreateTime();
		}
		
		public String getType() {
			return delegate.getType().getName();
		}
		
		public Long getLid() {
			return delegate.getId();
		}
		
		public String getData() {
			return delegate.getData();
		}
	}