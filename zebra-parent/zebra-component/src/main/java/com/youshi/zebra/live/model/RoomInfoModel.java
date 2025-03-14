package com.youshi.zebra.live.model;

import java.util.List;

/**
 * 直播课房间信息。
 * 
 * @author wangsch
 * @date 2017年3月17日
 */
public class RoomInfoModel {
	private Integer studentOnlineStatus;
	private Integer teacherOnlineStatus;
	private List<Integer> audienceIdList;
	private Integer currentFrameX;
	private Integer currentFrameY;
	private String framesInfoJson;

	public Integer getStudentOnlineStatus() {
		return studentOnlineStatus;
	}

	public void setStudentOnlineStatus(Integer studentOnlineStatus) {
		this.studentOnlineStatus = studentOnlineStatus;
	}

	public Integer getTeacherOnlineStatus() {
		return teacherOnlineStatus;
	}

	public void setTeacherOnlineStatus(Integer teacherOnlineStatus) {
		this.teacherOnlineStatus = teacherOnlineStatus;
	}

	public List<Integer> getAudienceIdList() {
		return audienceIdList;
	}

	public void setAudienceIdList(List<Integer> audienceIdList) {
		this.audienceIdList = audienceIdList;
	}

	public Integer getCurrentFrameX() {
		return currentFrameX;
	}

	public void setCurrentFrameX(Integer currentFrameX) {
		this.currentFrameX = currentFrameX;
	}

	public Integer getCurrentFrameY() {
		return currentFrameY;
	}

	public void setCurrentFrameY(Integer currentFrameY) {
		this.currentFrameY = currentFrameY;
	}

	public String getFramesInfoJson() {
		return framesInfoJson;
	}

	public void setFramesInfoJson(String framesInfoJson) {
		this.framesInfoJson = framesInfoJson;
	}
}