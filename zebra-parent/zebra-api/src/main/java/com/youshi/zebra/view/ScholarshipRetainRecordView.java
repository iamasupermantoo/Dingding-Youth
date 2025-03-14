package com.youshi.zebra.view;

import com.youshi.zebra.core.utils.DateTimeUtils;
import com.youshi.zebra.scholarship.model.ScholarshipRetainRecordModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "代表提现记录分页中，一个元素")
public class ScholarshipRetainRecordView {
	private ScholarshipRetainRecordModel delegate;
	
	public ScholarshipRetainRecordView(ScholarshipRetainRecordModel delegate) {
		this.delegate = delegate;
	}

	@ApiModelProperty(value = "提现时间")
	public String getDatetime() {
		return DateTimeUtils.getDateTime(delegate.getCreateTime());
	}
	
	@ApiModelProperty(value = "提现金额")
	public String getAmount() {
		return (delegate.getApplyAmount() / 100) + "元";
	}
	
	@ApiModelProperty(value = "0：审核中，1：提现失败，2：提现成功")
	public int getStatus() {
		return delegate.getStatus();
	}
}