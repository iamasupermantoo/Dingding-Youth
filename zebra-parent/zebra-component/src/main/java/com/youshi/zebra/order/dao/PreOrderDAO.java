package com.youshi.zebra.order.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.EntityStatus;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.order.model.PreOrderModel;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class PreOrderDAO extends AbstractDAO<Integer, PreOrderModel>{
	private static final String TABLE_NAME = "pre_order";
	
	private static final RowMapper<PreOrderModel> MAPPER = (rs, num)->
		new PreOrderModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("user_id"),
			rs.getInt("product_id"),
			rs.getInt("product_type"),
			rs.getInt("total_price"),
			rs.getLong("update_time")
			);
	
    public PreOrderDAO() {
        super(ZebraDB.order.getZKName(), TABLE_NAME, MAPPER);
    }
    
    
    public int insert(
		int userId,
		int productId,
		int productType,
		int totalPrice,
		long updateTime,
    	String data, PreOrderStatus status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(user_id,product_id,product_type,total_price,update_time,data, status, create_time) "
    			+ "values(:userId,:productId,:productType,:totalPrice,:updateTime, :data, :status, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId", userId)
				.addValue("productId", productId)
				.addValue("productType", productType)
				.addValue("totalPrice", totalPrice)
				.addValue("updateTime", updateTime)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
    public enum PreOrderStatus implements EntityStatus {
		
		/**
		 * 用户提交了订单
		 */
		USER_COMMITED(0, "用户提交了预约单"),
		
		/**
		 * 订单处理中，销售看到了订单并点击“处理”，转换到该状态
		 */
		ADMIN_PROCESS(1, "预约单处理中"),
		
		ADMIN_CONFIRMED(2, "预约单被销售确认"),
		
		;
		
		private final int value;
		private final String name;
		PreOrderStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<PreOrderStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (PreOrderStatus e : PreOrderStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final PreOrderStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	    
}
