package com.youshi.zebra.book.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.book.constants.ChapterStatus;
import com.youshi.zebra.book.model.ChapterModel;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
@Repository
public class ChapterDAO extends AbstractDAO<Integer, ChapterModel>{
	private static final String TABLE_NAME = "chapter";
	
	private static final RowMapper<ChapterModel> MAPPER = (rs, num)-> 
		new ChapterModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), rs.getInt("status"),
				rs.getInt("book_id"), rs.getInt("cnt"));
	
	protected ChapterDAO() {
		super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
	}
	
	public int insert(Integer bookId, Integer cnt, 
			ChapterStatus status, String data, long createTime) {
		String sql = "insert into " + TABLE_NAME + " (book_id, cnt, status, data, create_time) "
				+ "values(:bookId, :cnt, :status, :data, :ct)";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("bookId", bookId)
				.addValue("cnt", cnt)
				.addValue("status", status.getValue())
				.addValue("data", data)
				.addValue("ct", createTime);
		
		GeneratedKeyHolder kh = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sql, params, kh);
		
		return kh.getKey().intValue();
	}

	public int updateCnt(Integer bookId, Integer chapterId, int cnt) {
		String sql = "update " + TABLE_NAME + " set cnt = :cnt where book_id = :bid and id = :cid" ;
		MapSqlParameterSource params = new MapSqlParameterSource("cid", chapterId)
				.addValue("bid", bookId)
				.addValue("cnt", cnt);
		
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		
		return c;
	}
	
	public int deleteChapter(Integer bookId, Integer chapterId) {
		String sql = "delete from " + TABLE_NAME + " where id = :chapterId and book_id = :bookId";
		MapSqlParameterSource params = new MapSqlParameterSource("chapterId", chapterId)
				.addValue("bookId", bookId);
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		
		return c;
	}
	
	public int update(Integer bookId, Integer chapterId, Integer cnt, String data) {
		String sql = "update " + TABLE_NAME + " set cnt = :cnt, data=:data "
				+ "where id = :chapterId and book_id = :bookId";
		MapSqlParameterSource params = new MapSqlParameterSource("cnt", cnt)
				.addValue("data", data)
				.addValue("chapterId", chapterId)
				.addValue("bookId", bookId);
		
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		
		return c;
	}

	/**
	 * @param bookId
	 * @param cnt
	 * @return
	 */
	public ChapterModel getChapter(Integer bookId, int cnt) {
		String sql = "select * from " + TABLE_NAME + " where book_id = :bid and cnt = :cnt";
		MapSqlParameterSource params = new MapSqlParameterSource("bid", bookId)
				.addValue("cnt", cnt);
		try{
			ChapterModel result = getNamedParameterJdbcTemplate().queryForObject(sql, params, MAPPER);
			return result;
		} catch(IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
}
