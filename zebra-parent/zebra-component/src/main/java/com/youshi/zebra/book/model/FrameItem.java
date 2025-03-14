package com.youshi.zebra.book.model;

import java.util.List;

public class FrameItem {
	/*
	 *  {
		    "version": "1.0",
		    "frames": [
		        {
		            "idx": 1,
		            "type": "Image",
		            "image": "http://www.baidu.com/logo.jpeg",
		            "children": "[
		            	{
		            		idx:1, 
		            		type:audio, 
		            		image: {},
		            		audio: "http://"}
		            ]"
		        }
		    ]
		}
	 */
	private Integer idx;
	private Integer type;
	private Integer mediaId;
	private boolean isChild;
	private List<FrameItem> children;

	public FrameItem() {
	}

	public FrameItem(Integer type, Integer mediaId) {
		this.type = type;
		this.mediaId = mediaId;
	}

	public Integer getIdx() {
		return idx;
	}

	/**
	 * 必须通过setter注入
	 */
	public void setIdx(Integer idx) {
		this.idx = idx;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public boolean getIsChild() {
		return isChild;
	}
	
	public void setIsChild(boolean isChild) {
		this.isChild = isChild;
	}
	
	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public List<FrameItem> getChildren() {
		return children;
	}

	public void setChildren(List<FrameItem> children) {
		this.children = children;
	}
}