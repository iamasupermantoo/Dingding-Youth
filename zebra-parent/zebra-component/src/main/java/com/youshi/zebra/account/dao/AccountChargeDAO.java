package com.youshi.zebra.account.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.EntityStatus;
import com.youshi.zebra.account.model.AccountChargeModel;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class AccountChargeDAO extends AbstractDAO<Integer, AccountChargeModel>{
	private static final String TABLE_NAME = "account_charge";
	
	private static final RowMapper<AccountChargeModel> MAPPER = (rs, num)->
		new AccountChargeModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("user_id"),
			rs.getInt("charge_amount")
			);
	
    public AccountChargeDAO() {
        super(ZebraDB.order.getZKName(), TABLE_NAME, MAPPER);
    }
    
    
    public int insert(
		int userId,
		int chargeAmount,
    	String data, AccountChargeStatus status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(user_id,charge_amount,data, status, create_time) "
    			+ "values(:userId,:chargeAmount, :data, :status, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId", userId)
				.addValue("chargeAmount", chargeAmount)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
    public enum AccountChargeStatus implements EntityStatus {
		Normal(0, "正常"),
		;
		
		private final int value;
		private final String name;
		AccountChargeStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<AccountChargeStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (AccountChargeStatus e : AccountChargeStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final AccountChargeStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	    
}
