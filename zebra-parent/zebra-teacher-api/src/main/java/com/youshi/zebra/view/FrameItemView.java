package com.youshi.zebra.view;

import java.util.List;

import com.youshi.zebra.book.model.ImageItem;

public class FrameItemView {
	private Integer idx;
	private Integer type;
	
	private ImageItem image;
	
	private String audio;
	
	private String video;
	
	private List<FrameItemView> children;

	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public ImageItem getImage() {
		return image;
	}

	public void setImage(ImageItem image) {
		this.image = image;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public List<FrameItemView> getChildren() {
		return children;
	}

	public void setChildren(List<FrameItemView> children) {
		this.children = children;
	}
}