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
import com.youshi.zebra.order.model.ProductPriceModel;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class ProductPriceDAO extends AbstractDAO<Integer, ProductPriceModel>{
	private static final String TABLE_NAME = "product_price";
	
	private static final RowMapper<ProductPriceModel> MAPPER = (rs, num)->
		new ProductPriceModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("product_id"),
			rs.getString("apple_product_id"),
			rs.getInt("price"),
			rs.getInt("original_price"),
			rs.getInt("type"),
			rs.getLong("update_time")
			);
	
    public ProductPriceDAO() {
        super(ZebraDB.order.getZKName(), TABLE_NAME, MAPPER);
    }
    
	public ProductPriceModel getProductPrice(Integer productId, ProductType productType) {
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
		String appleProductId,
		int price,
		int originalPrice,
		int type,
		Long updateTime,
    	String data, ProductPriceStatus status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(product_id,apple_product_id,price,original_price,type,update_time,data, status, create_time) "
    			+ "values(:productId,:appleProductId,:price,:originalPrice,:type,:updateTime, :data, :status, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("productId", productId)
				.addValue("appleProductId", appleProductId)
				.addValue("price", price)
				.addValue("originalPrice", originalPrice)
				.addValue("type", type)
				.addValue("updateTime", updateTime)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
    public enum ProductPriceStatus implements EntityStatus {
		Normal(0, "正常"),
		;
		
		private final int value;
		private final String name;
		ProductPriceStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<ProductPriceStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (ProductPriceStatus e : ProductPriceStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final ProductPriceStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	    
}
