package com.youshi.zebra.view;

import com.youshi.zebra.book.model.ChapterModel;
import com.youshi.zebra.core.web.view.ZebraBuildContext;

public class ChapterView {
	private ChapterModel delegate;
	private ZebraBuildContext context;

	public ChapterView(ChapterModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}

	public Integer getCid() {
		return delegate.getId();
	}

	public String getLabel() {
		return delegate.getLabel();
	}

	public String getDesc() {
		return delegate.getDesc();
	}
	
	public String getHomeworkTitle() {
		return delegate.getHomeworkTitle();
	}
	
	public String getHomeworkContent() {
		return delegate.getHomeworkContent();
	}
	
	public Integer getCnt() {
		return delegate.getCnt();
	}
	
	public long getCreateTime() {
		return delegate.getCreateTime();
	}
	
	
	
}