package com.youshi.zebra.book.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * 教材，后端叫做“book”（教材可以理解为一本书，而且book这个单词容易理解和记忆）
 * 
 * book下有多个chapter（章节）
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
public class BookModel extends AbstractModel<Integer>{
	public enum BookKeys {
		name,
		total_cnt, desc,
	}
	
	public BookModel(Integer id, String data, long createTime, int status) {
		super(id, data, createTime, status);
	}
	
	public String getName() {
		return ModelUtils.getString(this, BookKeys.name);
	}
	
	public Integer getTotalCnt() {
		return ModelUtils.getInt(this, BookKeys.total_cnt);
	}
	
	public String getDesc() {
		return ModelUtils.getString(this, BookKeys.desc);
	}
}
