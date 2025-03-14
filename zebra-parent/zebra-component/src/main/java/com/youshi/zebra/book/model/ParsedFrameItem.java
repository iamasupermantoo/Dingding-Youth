package com.youshi.zebra.book.model;

import java.util.List;

public class ParsedFrameItem {
	private Integer idx;
	
	private Integer type;
	
	private ImageItem image;
	
	private String audio;
	
	private String video;
	
	private List<ParsedFrameItem> children;
	

	public ParsedFrameItem() {
	}

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

	public List<ParsedFrameItem> getChildren() {
		return children;
	}

	public void setChildren(List<ParsedFrameItem> children) {
		this.children = children;
	}
}