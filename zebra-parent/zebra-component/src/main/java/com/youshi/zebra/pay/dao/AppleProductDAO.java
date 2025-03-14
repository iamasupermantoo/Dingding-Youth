package com.youshi.zebra.pay.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.model.ProductPriceModel;
import com.youshi.zebra.pay.model.AppleProductModel;

/**
 * 
 * 这个DAO，操作苹果支付中的商品
 * 
 * @author wangsch
 * @date 2017年4月17日
 * 
 * @deprecated use {@link ProductPriceModel}
 * 
 */
@Repository
@Deprecated
public class AppleProductDAO extends AbstractDAO<Integer, AppleProductModel>{
	private static final String TABLE_NAME = "apple_product";
	
	private static final RowMapper<AppleProductModel> MAPPER = (rs, num)->
		new AppleProductModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), rs.getInt("status"), 
			rs.getInt("product_id"), rs.getString("apple_product_id"), rs.getInt("product_type"));
	
	public AppleProductDAO() {
		super(ZebraDB.order.getZKName(), TABLE_NAME, MAPPER);
	}
	
	public AppleProductModel getProduct(Integer productId, ProductType productType ) {
		String sql = "select * from " + TABLE_NAME 
				+ " where product_id = :productId and product_type = :productType";
		MapSqlParameterSource params = new MapSqlParameterSource("productId", productId)
				.addValue("productType", productType.getValue())
				;
		try {
			AppleProductModel result = getNamedParameterJdbcTemplate()
					.queryForObject(sql, params, MAPPER);
			return result;
		} catch(IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
	
}
