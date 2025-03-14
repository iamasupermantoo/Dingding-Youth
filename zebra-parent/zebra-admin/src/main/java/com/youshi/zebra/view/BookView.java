package com.youshi.zebra.view;

import com.youshi.zebra.book.model.BookModel;
import com.youshi.zebra.core.web.view.ZebraBuildContext;

public class BookView {
	private BookModel delegate;
	private ZebraBuildContext context;

	public BookView(BookModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}

	public Integer getBid() {
		return delegate.getId();
	}

	public String getName() {
		return delegate.getName();
	}

	public String getDesc() {
		return delegate.getDesc();
	}

	public Integer getTotalCnt() {
		return delegate.getTotalCnt();
	}

	public long getCreateTime() {
		return delegate.getCreateTime();
	}
}