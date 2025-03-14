package com.youshi.zebra.view;

import com.youshi.zebra.book.model.BookModel;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.image.model.ImageModel;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
public class CourseMetaAdminView {
	private CourseMetaModel delegate;
	
	private BookModel book;
	
	private ZebraBuildContext context;
	
	public CourseMetaAdminView(CourseMetaModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		this.book = context.getBook(delegate.getBookId());
	}
	
	public BookView getBook() {
		return new BookView(book, context);
	}
	
	public String getCmId() {
		return delegate.getUuid();
	}
	public String getName() {
		return delegate.getName();
	}
	
	public String getDesc() {
		return delegate.getDesc();
	}
	
	public String getSubNotes() {
		return ""+delegate.getSubNotes();
	}
	
	public String getSuitableCrowds() {
		return delegate.getSuitableCrowds();
	}
	
	public Integer getStatus() {
		return delegate.getStatus();
	}
	
	public ImageModel getImage() {
		return context.getImage(delegate.getImageId());
	}
	
	public Integer getTotalCnt() {
		return book.getTotalCnt();
	}
	
	public String getPrice() {
		return delegate.getPrice();
	}
	
	public Integer getLevel() {
		return delegate.getLevel();
	}
	
	public Integer getType() {
		return delegate.getType();
	}
	
	public long getCreateTime() {
		return delegate.getCreateTime();
	}
	
	public Integer getJoinCnt() {
		return delegate.getJoinCount();
	}
	
	public String getShareJumpUrl() {
		return delegate.getShareJumpUrl();
	}
	
	public String getShareBrief() {
		return delegate.getShareBrief();
	}
	
}
