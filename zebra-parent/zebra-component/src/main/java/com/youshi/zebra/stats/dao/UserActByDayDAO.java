package com.youshi.zebra.stats.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
//import com.youshi.zebra.stats.constants.UserActByDayStatus;
import com.youshi.zebra.stats.model.UserActByDayModel;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class UserActByDayDAO extends AbstractDAO<Integer, UserActByDayModel>{
	private static final String TABLE_NAME = "user_act_by_day";
	
	private static final RowMapper<UserActByDayModel> MAPPER = (rs, num)->
		new UserActByDayModel(rs.getInt("id"), rs.getLong("create_time"),
			rs.getInt("normal_user_count"),
			rs.getInt("pay_user_count"),
			rs.getString("day")
			);
	
    public UserActByDayDAO() {
        super(ZebraDB.admin.getZKName(), TABLE_NAME, MAPPER);
    }
    
    
    public int insert(
		Integer normalUserCount,
		Integer payUserCount,
		String day, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(normal_user_count,pay_user_count,day,create_time) "
    			+ "values(:normalUserCount,:payUserCount,:day, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("normalUserCount", normalUserCount)
				.addValue("payUserCount", payUserCount)
				.addValue("day", day)
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
    public UserActByDayModel getByDay(String day) {
		String sql = "select * from " + TABLE_NAME + " where day = :day";
		try {
			return getNamedParameterJdbcTemplate().queryForObject(sql, new MapSqlParameterSource("day", day), MAPPER);
		} catch(IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
}
