package com.youshi.zebra.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "rejoinInfo")
public class LiveRejoinInfoView {
	private Integer currentFrameX;
	
	private Integer currentFrameY;
	
	public LiveRejoinInfoView(Integer currentFrameX, Integer currentFrameY) {
		this.currentFrameX = currentFrameX;
		this.currentFrameY = currentFrameY;
	}
	
	@ApiModelProperty(value = "当前的currentFrame x")
	public Integer getCurrentFrameX() {
		return currentFrameX;
	}

	@ApiModelProperty(value = "当前的currentFrame y")
	public Integer getCurrentFrameY() {
		return currentFrameY;
	}

	public void setCurrentFrameX(Integer currentFrameX) {
		this.currentFrameX = currentFrameX;
	}
	
	public void setCurrentFrameY(Integer currentFrameY) {
		this.currentFrameY = currentFrameY;
	}
	
}