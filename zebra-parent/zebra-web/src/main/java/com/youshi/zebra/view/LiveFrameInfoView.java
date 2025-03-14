package com.youshi.zebra.view;


import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "framesInfo")
public class LiveFrameInfoView {
	private String version;

	private List<FrameItemView> frames;

	@ApiModelProperty(value = "课件版本")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@ApiModelProperty(value = "课件frame，json字符串")
	public List<FrameItemView> getFrames() {
		return frames;
	}

	public void setFrames(List<FrameItemView> frames) {
		this.frames = frames;
	}
}