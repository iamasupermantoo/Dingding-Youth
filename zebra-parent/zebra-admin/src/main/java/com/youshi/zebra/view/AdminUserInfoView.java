package com.youshi.zebra.view;

import com.dorado.framework.crud.model.util.ModelUtils;
import com.youshi.zebra.admin.adminuser.constant.AdminUserKeys;
import com.youshi.zebra.admin.adminuser.model.impl.AdminUserPassportModel;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.user.model.UserModel;

/**
 * 后台管理员详细信息View
 * 
 * @author wangsch
 * @date 2017年2月24日
 */
public class AdminUserInfoView {
		private UserModel delegate;
		private ZebraBuildContext context;
		
		public AdminUserInfoView(UserModel delegate, ZebraBuildContext context) {
			this.delegate = delegate;
			this.context = context;
		}
		
		public Integer getAid() {
			return delegate.getId();
		}
		
		public String getUsername() {
			return ModelUtils.getString(delegate, AdminUserKeys.username);
		}
		
		public String getName() {
			return delegate.getName();
		}
		
		public Integer getStatus() {
			return delegate.getStatus();
		}
		
		
		public long getCreateTime() {
			return delegate.getCreateTime();
		}
		
		public String getRemark() {
			return ModelUtils.getString(delegate, AdminUserKeys.remark);
		}
		
		public String getEmail() {
			return ModelUtils.getString(delegate, AdminUserKeys.email);
		}
		
		public String getMobile() {
			return ModelUtils.getString(delegate, AdminUserKeys.mobile);
		}
		
	}