package com.youshi.zebra.live.model;

/**
 * 公开课直播，房间信息
 * 
 * @author wangsch
 * @date 2017年11月11日
 */
public class OpenCourseLiveInfo {
	private String appId;

	private String channelName;

	private long account;

	private Integer role;
	
	private long studentAccount;
	
	private long teacherAccount;

	private String signalingKey;

	private String channelKey;
	

	public String getAppId() {
		return appId;
	}
	
	public OpenCourseLiveInfo setAppId(String appId) {
		this.appId = appId;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public OpenCourseLiveInfo setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public long getAccount() {
		return account;
	}

	public OpenCourseLiveInfo setAccount(long account) {
		this.account = account;
		return this;
	}
	
	public long getStudentAccount() {
		return studentAccount;
	}

	public OpenCourseLiveInfo setStudentAccount(long studentAccount) {
		this.studentAccount = studentAccount;
		return this;
	}

	public long getTeacherAccount() {
		return teacherAccount;
	}

	public OpenCourseLiveInfo setTeacherAccount(long teacherAccount) {
		this.teacherAccount = teacherAccount;
		return this;
	}

	public Integer getRole() {
		return role;
	}

	public OpenCourseLiveInfo setRole(Integer role) {
		this.role = role;
		return this;
	}

	public String getSignalingKey() {
		return signalingKey;
	}

	public OpenCourseLiveInfo setSignalingKey(String signalingKey) {
		this.signalingKey = signalingKey;
		return this;
	}

	public String getChannelKey() {
		return channelKey;
	}

	public OpenCourseLiveInfo setChannelKey(String channelKey) {
		this.channelKey = channelKey;
		return this;
	}
}