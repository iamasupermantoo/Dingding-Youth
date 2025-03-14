package com.youshi.zebra.counts.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.counts.model.UserCountsModel;

/**
 * 用户的一些计数，DAO
 * 
 * @author wangsch
 * @date		2016年11月6日
 *
 */
@Repository
public class UserCountsDAO extends AbstractDAO<Integer, UserCountsModel>{
	private static final String TABLE_NAME = "user_counts";
	
	private static final RowMapper<UserCountsModel> MAPPER = 
		new RowMapper<UserCountsModel>() {
			@Override
			public UserCountsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new UserCountsModel(rs.getInt("id"), 
						rs.getInt("course_count"), rs.getInt("finished_count"), rs.getInt("duration"));
			}
		};
	
	protected UserCountsDAO() {
		super(ZebraDB.user.getZKName(), TABLE_NAME, MAPPER);
	}
	
	public int insertOrIncreseCourseCount(Integer userId, Integer incr, long createTime) {
		String sql = "insert into " + TABLE_NAME + "(id, course_count, create_time) values(:userId, :incr, :createTime) "
				+ "on duplicate key update `course_count` = `course_count` + :incr";
		
		return getNamedParameterJdbcTemplate().update(sql, 
				new MapSqlParameterSource("userId", userId)
				.addValue("incr", incr).addValue("createTime", createTime));
	}
	
	public int insertOrIncreseFinishedCount(Integer userId, Integer incr, long createTime) {
		String sql = "insert into " + TABLE_NAME + "(id, finished_count, create_time) values(:userId, :incr, :createTime) "
				+ "on duplicate key update `finished_count` = `finished_count` + :incr";
		
		return getNamedParameterJdbcTemplate().update(sql, 
				new MapSqlParameterSource("userId", userId)
				.addValue("incr", incr).addValue("createTime", createTime));
	}
	
	public int insertOrIncreseDuration(Integer userId, Integer incr, long createTime) {
		String sql = "insert into " + TABLE_NAME + "(id, duration, create_time) values(:userId, :incr, :createTime) "
				+ "on duplicate key update `duration` = `duration` + :incr";
		
		return getNamedParameterJdbcTemplate().update(sql, 
				new MapSqlParameterSource("userId", userId)
				.addValue("incr", incr).addValue("createTime", createTime));
	}
}
