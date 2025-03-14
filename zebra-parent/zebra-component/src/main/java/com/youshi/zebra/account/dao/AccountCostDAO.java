package com.youshi.zebra.account.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.EntityStatus;
import com.youshi.zebra.account.model.AccountCostModel;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class AccountCostDAO extends AbstractDAO<Integer, AccountCostModel>{
	private static final String TABLE_NAME = "account_cost";
	
	private static final RowMapper<AccountCostModel> MAPPER = (rs, num)->
		new AccountCostModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("user_id"),
			rs.getInt("cost_amount")
			);
	
    public AccountCostDAO() {
        super(ZebraDB.order.getZKName(), TABLE_NAME, MAPPER);
    }
    
    
    public int insert(
		int userId,
		int costAmount,
    	String data, AccountCostStatus status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(user_id,cost_amount,data, status, create_time) "
    			+ "values(:userId,:costAmount, :data, :status, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId", userId)
				.addValue("costAmount", costAmount)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
    public enum AccountCostStatus implements EntityStatus {
		Normal(0, "正常"),
		;
		
		private final int value;
		private final String name;
		AccountCostStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<AccountCostStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (AccountCostStatus e : AccountCostStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final AccountCostStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	    
}
