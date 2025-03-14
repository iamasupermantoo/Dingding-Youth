package com.youshi.zebra.live.model;

public class LiveInfo {
	private String appId;

	private String channelName;

	private long account;

	private Integer role;
	
	private long studentAccount;
	
	private long teacherAccount;

	private String signalingKey;

	private String channelKey;
	
	private Integer currentFrameX;

	private Integer currentFrameY;

	private String framesInfo;
	
	private Integer courseId;
	
	private Integer lessonId;

	public String getAppId() {
		return appId;
	}
	
	public LiveInfo setAppId(String appId) {
		this.appId = appId;
		return this;
	}
	
	public Integer getCurrentFrameX() {
		return currentFrameX;
	}

	public LiveInfo setCurrentFrameX(Integer currentFrameX) {
		this.currentFrameX = currentFrameX;
		return this;
	}

	public Integer getCurrentFrameY() {
		return currentFrameY;
	}

	public LiveInfo setCurrentFrameY(Integer currentFrameY) {
		this.currentFrameY = currentFrameY;
		return this;
	}
	
	public String getFramesInfo() {
		return this.framesInfo;
	}
	
	public LiveInfo setFramesInfo(String framesInfo) {
		this.framesInfo = framesInfo;
		return this;
	}

	public String getChannelName() {
		return channelName;
	}

	public LiveInfo setChannelName(String channelName) {
		this.channelName = channelName;
		return this;
	}

	public long getAccount() {
		return account;
	}

	public LiveInfo setAccount(long account) {
		this.account = account;
		return this;
	}
	
	public long getStudentAccount() {
		return studentAccount;
	}

	public LiveInfo setStudentAccount(long studentAccount) {
		this.studentAccount = studentAccount;
		return this;
	}

	public long getTeacherAccount() {
		return teacherAccount;
	}

	public LiveInfo setTeacherAccount(long teacherAccount) {
		this.teacherAccount = teacherAccount;
		return this;
	}

	public Integer getRole() {
		return role;
	}

	public LiveInfo setRole(Integer role) {
		this.role = role;
		return this;
	}

	public String getSignalingKey() {
		return signalingKey;
	}

	public LiveInfo setSignalingKey(String signalingKey) {
		this.signalingKey = signalingKey;
		return this;
	}

	public String getChannelKey() {
		return channelKey;
	}

	public LiveInfo setChannelKey(String channelKey) {
		this.channelKey = channelKey;
		return this;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public LiveInfo setCourseId(Integer courseId) {
		this.courseId = courseId;
		return this;
	}

	public Integer getLessonId() {
		return lessonId;
	}

	public LiveInfo setLessonId(Integer lessonId) {
		this.lessonId = lessonId;
		return this;
	}
}