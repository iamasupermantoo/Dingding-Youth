package com.youshi.zebra.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "roomInfo", description = "房间基本信息+客户端对接“声网”需要的参数")
public class LiveRoomInfoView {
	private String appId;
	
	private String channelName;

	private long account;
	
	private long studentAccount;
	
	private long teacherAccount;

	private Integer role;
	
	private String channelKey;
	
	private String signalingKey;
	
	private String cid;
	
	private String lid;
	
	@ApiModelProperty(value = "声网：appId")
	public String getAppId() {
		return appId;
	}
	
	@ApiModelProperty(value = "声网：唯一的频道名")
	public String getChannelName() {
		return channelName;
	}
	
	@ApiModelProperty(value = "声网：加入直播channelKey参数")
	public String getChannelKey() {
		return channelKey;
	}
	
	@ApiModelProperty(value = "声网：登录信令系统signalingKey（tokekn）参数")
	public String getSignalingKey() {
		return signalingKey;
	}

	@ApiModelProperty(value = "声网：唯一用户账号")
	public long getAccount() {
		return account;
	}

	@ApiModelProperty(value = "声网：角色。1:主播（推流）2:观众（拉流）")
	public Integer getRole() {
		return role;
	}
	
	@ApiModelProperty(value = "声网：学生account")
	public long getStudentAccount() {
		return studentAccount;
	}
	
	public void setStudentAccount(long studentAccount) {
		this.studentAccount = studentAccount;
	}
	
	@ApiModelProperty(value = "声网：老师account")
	public long getTeacherAccount() {
		return teacherAccount;
	}
	
	@ApiModelProperty(value = "课程id")
	public String getCid() {
		return cid;
	}
	
	@ApiModelProperty(value = "lesson id")
	public String getLid() {
		return lid;
	}
	
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public void setTeacherAccount(long teacherAccount) {
		this.teacherAccount = teacherAccount;
	}
	
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}
	
	public void setSignalingKey(String signalingKey) {
		this.signalingKey = signalingKey;
	}
	
	public void setAccount(long account) {
		this.account = account;
	}
	
	public void setRole(Integer role) {
		this.role = role;
	}
	
	public void setCid(String cid) {
		this.cid = cid;
	}
	
	public void setLid(String lid) {
		this.lid = lid;
	}
}