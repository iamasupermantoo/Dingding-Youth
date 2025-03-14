package com.youshi.zebra.account.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.EntityStatus;
import com.youshi.zebra.account.model.AccountChargeItemModel;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class AccountChargeItemDAO extends AbstractDAO<Integer, AccountChargeItemModel>{
	private static final String TABLE_NAME = "account_charge_item";
	
	private static final RowMapper<AccountChargeItemModel> MAPPER = (rs, num)->
		new AccountChargeItemModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("plus_amount")
			);
	
    public AccountChargeItemDAO() {
        super(ZebraDB.order.getZKName(), TABLE_NAME, MAPPER);
    }
    
    
    public int insert(
		int plusAmount,
    	String data, AccountChargeItemStatus status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(plus_amount,data, status, create_time) "
    			+ "values(:plusAmount, :data, :status, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("plusAmount", plusAmount)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
    public enum AccountChargeItemStatus implements EntityStatus {
		Normal(0, "正常"),
		AdminDel(2, "已删除"),
		;
		
		private final int value;
		private final String name;
		AccountChargeItemStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<AccountChargeItemStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (AccountChargeItemStatus e : AccountChargeItemStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final AccountChargeItemStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	    
}
