package com.youshi.zebra.book.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.book.constants.FrameConfigStatus;
import com.youshi.zebra.book.model.FrameConfigModel;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
@Repository
public class FrameConfigDAO extends AbstractDAO<Integer, FrameConfigModel>{
	
	private static final String TABLE_NAME = "chapter_frame_config";
	
	private static final RowMapper<FrameConfigModel> MAPPER = (rs, num)->
		new FrameConfigModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), rs.getInt("status"),
				rs.getString("frame_content"));
	
	protected FrameConfigDAO() {
		super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
	}
	
	public int insertOrUpdate(Integer chapterId, String frameContent, String data, FrameConfigStatus status, long time) {
		String sql = "insert into " + TABLE_NAME + "(id, frame_content, data, status, create_time) "
				+ "values(:id, :content, :data, :status, :ct) "
				+ "on duplicate key update frame_content = values(frame_content)";
		int count = getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource()
				.addValue("id", chapterId)
				.addValue("content", frameContent)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("ct", time));
		
		return count;
	}

	/**
	 * @param chapterId
	 * @return
	 */
	public int deleteFrameConfig(Integer chapterId) {
		String sql = "delete from " + TABLE_NAME + " where id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource("id", chapterId);
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		
		return c;
	
	}
	
	
}
