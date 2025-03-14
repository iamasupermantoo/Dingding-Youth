package com.youshi.zebra.view;

import com.youshi.zebra.core.utils.DateTimeUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.scholarship.model.ScholarshipModel;
import com.youshi.zebra.scholarship.model.ScholarshipRetainRecordModel;
import com.youshi.zebra.user.model.UserModel;

public class ScholarshipRetainRecordView {
	private ScholarshipRetainRecordModel delegate;
	private ZebraBuildContext context;
	private ScholarshipModel scholarship;
	private UserModel user;
	
	public ScholarshipRetainRecordView(ScholarshipRetainRecordModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		
		scholarship = delegate.getScholarship();
		user = context.getUser(delegate.getUserId());
	}
	
	public String getId() {
		return delegate.getUuid();
	}
	
	public String getUsername() {
		return user.getName();
	}
	
	public String getBankName() {
		return scholarship.getBankName();
	}
	public String getBankCardNum() {
		return scholarship.getBankCardNum();
	}
	public String getBankUser() {
		return scholarship.getBankUser();
	}
	public String getBankUserMobile() {
		return scholarship.getBankUserMobile();
	}
	
	public String getDatetime() {
		return DateTimeUtils.getDateTime(delegate.getCreateTime());
	}
	
	public String getApplyAmount() {
		return (delegate.getApplyAmount() / 100) + "元";
	}
	
	public int getStatus() {
		return delegate.getStatus();
	}
}