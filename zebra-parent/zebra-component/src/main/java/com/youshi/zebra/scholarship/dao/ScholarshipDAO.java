package com.youshi.zebra.scholarship.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.EntityStatus;
import com.dorado.framework.crud.model.HasData;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.scholarship.model.ScholarshipModel;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class ScholarshipDAO extends AbstractDAO<Integer, ScholarshipModel>{
	private static final String TABLE_NAME = "scholarship";
	
	private static final RowMapper<ScholarshipModel> MAPPER = (rs, num)->
		new ScholarshipModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("user_id"),rs.getInt("total_amount")
			);
	
    public ScholarshipDAO() {
        super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
    }
    
    
    public int updateData(
    		int userId, String data, long time) {
    	String sql = "insert into " + TABLE_NAME + "(user_id,total_amount,data, status, create_time) "
    			+ " values(:userId, :amount,:data, :status, :createTime)"
    			+ "on duplicate key update `data` = :data";
    			;
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId", userId)
				.addValue("amount", 0)
				.addValue("data", data)
				.addValue("status", ScholarshipStatus.Normal.getValue())
				.addValue("createTime", time)
				;
		return getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    }
    
    public int incrTotalAmount(
		int userId, int amount, long time) {
    	String sql = "insert into " + TABLE_NAME + "(user_id,total_amount,data, status, create_time) "
    			+ " values(:userId, :amount,:data, :status, :createTime)"
    			+ "on duplicate key update `total_amount` = `total_amount` + :amount";
    			;
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId", userId)
				.addValue("amount", amount)
				.addValue("data", HasData.EMPTY_DATA)
				.addValue("status", ScholarshipStatus.Normal.getValue())
				.addValue("createTime", time)
				;
		return getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    }
    
    public int decrTotalAmount(
    		int userId, int amount, long time) {
        	String sql = "update " + TABLE_NAME + " set `total_amount` = `total_amount` - :amount"
        			+ " where user_id = :userId"
        			;
        	
    		MapSqlParameterSource params = new MapSqlParameterSource()
    				.addValue("userId", userId)
    				.addValue("amount", amount)
    				;
    		return getNamedParameterJdbcTemplate().update(sql, params);
        }

	public ScholarshipModel getByUserId(Integer userId) {
		
		String sql = "select * from " + TABLE_NAME + " where user_id = :userId";
		try {
			return getNamedParameterJdbcTemplate().queryForObject(sql, new MapSqlParameterSource("userId", userId), MAPPER);
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
    
    public enum ScholarshipStatus implements EntityStatus {
		Normal(0, "正常"),
		;
		
		private final int value;
		private final String name;
		ScholarshipStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<ScholarshipStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (ScholarshipStatus e : ScholarshipStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final ScholarshipStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	    
}
