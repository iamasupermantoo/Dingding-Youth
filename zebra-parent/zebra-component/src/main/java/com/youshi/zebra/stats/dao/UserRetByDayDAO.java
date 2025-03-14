package com.youshi.zebra.stats.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
//import com.youshi.zebra.stats.constants.UserRetByDayStatus;
import com.youshi.zebra.stats.model.UserRetByDayModel;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class UserRetByDayDAO extends AbstractDAO<Integer, UserRetByDayModel>{
	private static final String TABLE_NAME = "user_ret_by_day";
	
	private static final RowMapper<UserRetByDayModel> MAPPER = (rs, num)->
		new UserRetByDayModel(rs.getInt("id"), rs.getLong("create_time"),
			rs.getInt("normal_user_count"),
			rs.getInt("pay_user_count"),
			rs.getString("normal_user_rets"),
			rs.getString("pay_user_rets"),
			rs.getString("day")
			);
	
    public UserRetByDayDAO() {
        super(ZebraDB.admin.getZKName(), TABLE_NAME, MAPPER);
    }
    
    public UserRetByDayModel getByDay(String day) {
    	String sql = "select * from " + TABLE_NAME + " where day = :day";
    	try {
    		return getNamedParameterJdbcTemplate().queryForObject(sql, new MapSqlParameterSource("day", day), MAPPER);
    	} catch(IncorrectResultSizeDataAccessException e) {
    		return null;
    	}
    }
    
    public int insert(
		Integer normalUserCount,
		Integer payUserCount,
		String normalUserRets,
		String payUserRets,
		String day,
    	long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(normal_user_count,pay_user_count,normal_user_rets,pay_user_rets,day,create_time) "
    			+ "values(:normalUserCount,:payUserCount,:normalUserRets,:payUserRets,:day, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("normalUserCount", normalUserCount)
				.addValue("payUserCount", payUserCount)
				.addValue("normalUserRets", normalUserRets)
				.addValue("payUserRets", payUserRets)
				.addValue("day", day)
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
    public int update(String day, 
    		String normalUserRets,
    		String payUserRets) {
    	String sql = "update " + TABLE_NAME + " set normal_user_rets = :normalUserRets, pay_user_rets = :payUserRets "
    			+ "where day = :day";
		MapSqlParameterSource params = new MapSqlParameterSource("normalUserRets", normalUserRets)
				.addValue("payUserRets", payUserRets)
				.addValue("day", day);
				;
		return getNamedParameterJdbcTemplate().update(sql, params);
    }
}
