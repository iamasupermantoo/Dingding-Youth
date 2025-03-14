package com.youshi.zebra.order.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.EntityStatus;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.model.ProductCostModel;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class ProductCostDAO extends AbstractDAO<Integer, ProductCostModel>{
	private static final String TABLE_NAME = "product_cost";
	
	private static final RowMapper<ProductCostModel> MAPPER = (rs, num)->
		new ProductCostModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("product_id"),
			rs.getInt("cost"),
			rs.getInt("original_cost"),
			rs.getInt("type"),
			rs.getLong("update_time")
			);
	
    public ProductCostDAO() {
        super(ZebraDB.order.getZKName(), TABLE_NAME, MAPPER);
    }

    public ProductCostModel getProductCost(Integer productId, ProductType productType) {
		String sql = "select * from " + TABLE_NAME + " where product_id = :pid and type = :ptype";
		MapSqlParameterSource params = new MapSqlParameterSource("pid", productId)
				.addValue("ptype", productType.getValue())
				;
		try {
			return getNamedParameterJdbcTemplate().queryForObject(sql, params, MAPPER);
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
    
    
    public int insert(
		int productId,
		int cost,
		int originalCost,
		int type,
		Long updateTime,
    	String data, ProductCostStatus status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(product_id,cost,original_cost,type,update_time,data, status, create_time) "
    			+ "values(:productId,:cost,:originalCost,:type,:updateTime, :data, :status, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("productId", productId)
				.addValue("cost", cost)
				.addValue("originalCost", originalCost)
				.addValue("type", type)
				.addValue("updateTime", updateTime)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }

	public int update(Integer productId, Integer cost, Integer originalCost, int type, Long updateTime) {
		String sql = "update " + TABLE_NAME + " set cost = :cost, original_cost = :originalCost, update_time = :updateTime "
				+ "where product_id = :pid and type = :type";
		MapSqlParameterSource params = new MapSqlParameterSource("pid", productId)
				.addValue("type", type)
				.addValue("cost", cost)
				.addValue("originalCost", originalCost)
				.addValue("updateTime", updateTime)
				;
		return getNamedParameterJdbcTemplate().update(sql, params);
	}
    
    public enum ProductCostStatus implements EntityStatus {
		Normal(0, "正常"),
		;
		
		private final int value;
		private final String name;
		ProductCostStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<ProductCostStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (ProductCostStatus e : ProductCostStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final ProductCostStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	    
}
