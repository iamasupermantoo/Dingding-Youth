package com.youshi.zebra.lesson.dao;

import javax.annotation.PostConstruct;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.dorado.framework.datasource.DataSourceByZooKeeper;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.lesson.model.UserScheduleModel;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月17日
 */
@Repository
public class UserScheduleDAO extends NamedParameterJdbcDaoSupport {
	private static final String TABLE_NAME = "user_schedule";
	
	private static final RowMapper<UserScheduleModel> MAPPER = (rs, num)
			->new UserScheduleModel(rs.getInt("id"), rs.getInt("user_id"), rs.getString("date"), rs.getString("schedule"));
			
	@PostConstruct
	protected void init() {
		setDataSource(DataSourceByZooKeeper.of(ZebraDB.teaching.getZKName()));
	}
	
	public UserScheduleModel getOne(Integer userId, String date) {
		String sql = "select * from " + TABLE_NAME + " where user_id = :userId and date = :date";
		try {
			UserScheduleModel result = getNamedParameterJdbcTemplate().queryForObject(sql,
					new MapSqlParameterSource().addValue("userId", userId).addValue("date", date), MAPPER);
			return result;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
	
	// get by date range/ get by dates
	
	public int insertOrUpdate(Integer userId, String date, String schedule) {
		String sql = "insert into " + TABLE_NAME + "(user_id, date, schedule) "
				+ "values(:userId, :date, :schedule) "
				+ "on duplicate key update schedule = values(schedule)";
		int count = getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource()
				.addValue("userId", userId)
				.addValue("date", date)
				.addValue("schedule", schedule));
		
		return count;
	}
	
	public int delete(Integer userId, String date) {
		String sql = "delete from " + TABLE_NAME + " where user_id = :userId and date = :date";
		int count = getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource()
				.addValue("userId", userId)
				.addValue("date", date));
		
		return count;
	}
}
