package com.youshi.zebra.book.model;

import com.youshi.zebra.image.utils.ImageUtils;

public class ImageItem {
	private String id;
	private int width;
	private int height;
	private String format;
	
	private String hostname;

	public ImageItem(){}
	
	public ImageItem(String id, int width, int height, String format) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.format = format;
	}

	public String getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getPattern() {
		return ImageUtils.getPattern(getId(), getFormat());
	}
	
	public String getHostname() {
		return hostname;
	}

	public String getFormat() {
		return format;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
}