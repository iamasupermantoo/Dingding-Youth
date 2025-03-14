package com.youshi.zebra.passport.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.dorado.framework.datasource.DataSourceByZooKeeper;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.passport.model.TicketModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月20日
 */
@Repository
public class TicketDAO extends NamedParameterJdbcDaoSupport 
	implements RowMapper<TicketModel>{
	
	@PostConstruct
    protected void init() {
        setDataSource(DataSourceByZooKeeper.of(ZebraDB.passport.getZKName()));
    }
	
	@Override
	public TicketModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new TicketModel(rs.getInt("id"), rs.getInt("user_id"), rs.getBytes("secret"), 
				rs.getString("randoms"), rs.getLong("create_time"));
	}
	
	public TicketModel getTicket(int userId) {
		try {
			return getNamedParameterJdbcTemplate().queryForObject(
					"select * from ticket where user_id = :u",
			new MapSqlParameterSource("u", userId), this);
		} catch(IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
	
	public int insertTicket(int userId, byte[] secretKey) {
		String sql = "insert into ticket (user_id, secret, create_time) values(:u, :s, :c)";
		MapSqlParameterSource params = new MapSqlParameterSource("u", userId)
		        .addValue("s", secretKey)
		        .addValue("c", System.currentTimeMillis());
		int effectLine = getNamedParameterJdbcTemplate().update(sql, params);
		return effectLine;
	}
	
	public int updateRandoms(int userId, String randomsJson) {
		int c = getNamedParameterJdbcTemplate().update("update ticket set randoms = :randoms where user_id = :userId", 
        		new MapSqlParameterSource("userId", userId).addValue("randoms", randomsJson));
		return c;
	}
	
	public int deleteTicket(int userId) {
		int c = getNamedParameterJdbcTemplate().update("delete from ticket where user_id = :u",
                new MapSqlParameterSource("u", userId));
		return c;
	}

}
