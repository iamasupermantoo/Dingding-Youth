package com.youshi.zebra.book.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.book.model.BookModel;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
@Repository
public class BookDAO extends AbstractDAO<Integer, BookModel>{
	private static final String TABLE_NAME = "book";
	
	private static final RowMapper<BookModel> MAPPER = (rs, num)-> 
		new BookModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), rs.getInt("status"));
	
	protected BookDAO() {
		super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
	}
}
