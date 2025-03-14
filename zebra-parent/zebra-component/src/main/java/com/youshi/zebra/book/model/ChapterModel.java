package com.youshi.zebra.book.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * 章节，一个教材里边，可以有多个章节。每个章节会对应一个作业，有作业标题和内容
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
public class ChapterModel extends AbstractModel<Integer>{
	public enum ChapterKeys {
		cnt,
		book_id,
		
		// data字段
		label,
		desc,
		
		// 作业标题
		homework_title,
		
		// 作业内容
		homework_content,
	}
	private int bookId;
	
	private int cnt;

	public ChapterModel(Integer id, String data, long createTime, int status,
			int bookId, int cnt) {
		super(id, data, createTime, status);
		this.bookId = bookId;
		this.cnt = cnt;
	}
	
	public int getBookId() {
		return bookId;
	}
	
	public int getCnt() {
		return cnt;
	}
	
	public String getLabel() {
		return ModelUtils.getString(this, ChapterKeys.label);
	}
	
	public String getDesc() {
		return ModelUtils.getString(this, ChapterKeys.desc);
	}
	
	public String getHomeworkTitle() {
		return ModelUtils.getString(this, ChapterKeys.homework_title);
	}
	
	public String getHomeworkContent() {
		return ModelUtils.getString(this, ChapterKeys.homework_content);
	}
}
