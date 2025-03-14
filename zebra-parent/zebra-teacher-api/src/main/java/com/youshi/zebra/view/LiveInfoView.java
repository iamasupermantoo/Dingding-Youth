package com.youshi.zebra.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "liveInfo", description="这个类，代表了进行直播需要的各种信息")
public class LiveInfoView {
	
	
	private LiveRoomInfoView roomInfo;

	private LiveFrameInfoView framesInfo;

	private LiveRejoinInfoView rejoinInfo;

	public LiveInfoView(LiveRoomInfoView roomInfo, LiveFrameInfoView framesInfo, LiveRejoinInfoView rejoinInfo) {
		this.roomInfo = roomInfo;
		this.framesInfo = framesInfo;
		this.rejoinInfo = rejoinInfo;
	}

	@ApiModelProperty(value = "加入直播房间需要的参数信息")
	public LiveRoomInfoView getRoomInfo() {
		return roomInfo;
	}

	@ApiModelProperty(value = "本节课需要的课件（由很多frame组成）类似ppt模式")
	public LiveFrameInfoView getFramesInfo() {
		return framesInfo;
	}

	@ApiModelProperty(value = "当且仅当断线后重新加入房间时返回，断线前的状态")
	public LiveRejoinInfoView getRejoinInfo() {
		return rejoinInfo;
	}
}