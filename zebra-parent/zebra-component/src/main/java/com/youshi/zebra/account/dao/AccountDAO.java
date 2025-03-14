package com.youshi.zebra.account.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.EntityStatus;
import com.dorado.framework.crud.model.HasData;
import com.youshi.zebra.account.model.AccountModel;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class AccountDAO extends AbstractDAO<Integer, AccountModel>{
	private static final String TABLE_NAME = "account";
	
	private static final RowMapper<AccountModel> MAPPER = (rs, num)->
		new AccountModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("user_id"),
			rs.getInt("total_amount"),
			rs.getLong("update_time")
			);
	
    public AccountDAO() {
        super(ZebraDB.order.getZKName(), TABLE_NAME, MAPPER);
    }
    
    public int incrTotalAmount(int userId, int chargeAmount, long currTime) {
    	
    	String sql = "insert into " + TABLE_NAME + "(user_id, total_amount, data, status, create_time) values(:userId, :chargeAmount, :data, :status, :currTime) "
				+ "on duplicate key update `total_amount` = `total_amount` + :chargeAmount, `update_time` = :currTime";
		
		MapSqlParameterSource params = new MapSqlParameterSource("userId", userId)
				.addValue("chargeAmount", chargeAmount).addValue("currTime", currTime)
				.addValue("data", HasData.EMPTY_DATA)
				.addValue("status", AccountStatus.Normal.getValue())
				;
		return getNamedParameterJdbcTemplate().update(sql, params);
	}
    

	public int decrTotalAmount(int userId, int costAmount, long currTime) {
    	String sql = "update " + TABLE_NAME + " set `total_amount` = `total_amount` - :costAmount, `update_time` = :currTime "
    			+ "where user_id = :userId";
		
		MapSqlParameterSource params = new MapSqlParameterSource("userId", userId)
				.addValue("costAmount", costAmount).addValue("currTime", currTime);
		return getNamedParameterJdbcTemplate().update(sql, params);
	}
    
    public AccountModel getByUserId(Integer userId) {
    	String sql = "select * from " + TABLE_NAME + " where user_id = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);
		try {
			return getNamedParameterJdbcTemplate().queryForObject(sql, params, MAPPER);
		} catch(IncorrectResultSizeDataAccessException e) {
			return null;
		}
    }
    
    public enum AccountStatus implements EntityStatus {
		Normal(0, "正常"),
		;
		
		private final int value;
		private final String name;
		AccountStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<AccountStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (AccountStatus e : AccountStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final AccountStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
}
