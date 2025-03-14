package com.youshi.zebra.order.dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.order.constants.ChatResult;
import com.youshi.zebra.order.constants.OrderStatus;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.model.OrderModel;
import com.youshi.zebra.pay.constants.PayChannel;

/**
 * 
 * @author wangsch
 * @date 2017年2月18日
 */
@Repository
public class OrderDAO extends AbstractDAO<Integer, OrderModel>{
	private static final String TABLE_NAME = "`order`";
	
	private static final RowMapper<OrderModel> MAPPER = (rs, num)->
		new OrderModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("order_status"), 
				rs.getInt("user_id"), rs.getInt("product_id"), rs.getInt("product_type"), 
				rs.getString("order_sn"), rs.getInt("pay_status"), (String)rs.getObject ("pay_channel"), rs.getInt("chat_result"),
				rs.getInt("total_price"), rs.getInt("charge_amount"), rs.getLong("update_time"));
	
	protected OrderDAO() {
		super(ZebraDB.order.getZKName(), TABLE_NAME, MAPPER);
	}
	
	public int insert(
			Integer userId, Integer productId, ProductType productType, String orderSn, OrderStatus orderStatus, PayStatus payStatus,
			int totalPrice, int chargeAmount, String data, long createTime
			) {
		String sql = "insert into `order`(user_id, product_id, product_type, order_sn, order_status, pay_status, total_price, charge_amount, data, create_time) "
				+ "values(:uid, :productId, :productType, :orderSn, :status, :payStatus, :totalPrice, :chargeAmount, :data, :ct)";
		GeneratedKeyHolder kh = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource("uid", userId)
				.addValue("productId", productId)
				.addValue("productType", productType.getValue())
				.addValue("orderSn", orderSn)
				.addValue("status", orderStatus.getValue())
				.addValue("payStatus", payStatus.getValue())
				.addValue("totalPrice", totalPrice)
				.addValue("chargeAmount", chargeAmount)
				.addValue("data", data)
				.addValue("ct", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, kh);
		
		return kh.getKey().intValue();
	}
	
	public int updateStatus(Integer orderId, 
			OrderStatus orderStatus, PayStatus payStatus, PayChannel payChannel, long updateTime) {
		String sql = "update " + TABLE_NAME 
				+ " set order_status = :orderStatus, pay_status = :payStatus, pay_channel = :payChannel, update_time = :ut where id = :orderId";
		MapSqlParameterSource params = new MapSqlParameterSource("orderId", orderId)
				.addValue("orderStatus", orderStatus.getValue())
				.addValue("payStatus", payStatus.getValue())
				.addValue("payChannel", payChannel.getValue())
				.addValue("ut", updateTime)
				;
		
		int count = getNamedParameterJdbcTemplate().update(sql, params);
		return count;
	}
	
	public int updateOrder(Integer orderId, 
			OrderStatus orderStatus, Integer price, long updateTime) {
		String sql = "update " + TABLE_NAME 
				+ " set order_status = :orderStatus, total_price = :price, update_time = :ut where id = :orderId";
		MapSqlParameterSource params = new MapSqlParameterSource("orderId", orderId)
				.addValue("orderStatus", orderStatus.getValue())
				.addValue("price", price)
				.addValue("ut", updateTime)
				;
		
		int count = getNamedParameterJdbcTemplate().update(sql, params);
		return count;
	}
	
	public OrderModel getByOrderSn(String orderSn) {
		String sql = "select * from " + TABLE_NAME + " where order_sn = :orderSn";
		MapSqlParameterSource params = new MapSqlParameterSource("orderSn", orderSn);
		
		try {
			OrderModel result = getNamedParameterJdbcTemplate().queryForObject(sql, params, MAPPER);
			return result;
		} catch(IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
	
	public int updateChatResult(Integer orderId, ChatResult result, String data) {
		String sql = "update " + TABLE_NAME 
				+ " set chat_result = :result, data=:data where id = :orderId";
		MapSqlParameterSource params = new MapSqlParameterSource("orderId", orderId)
				.addValue("result", result.getValue())
				.addValue("data", data)
				;
		
		int count = getNamedParameterJdbcTemplate().update(sql, params);
		return count;
	}
	
	private static final List<Integer> PRE_ORDER_STATUS_LIST 
		= Arrays.asList(OrderStatus.USER_COMMITED.getValue(), OrderStatus.ADMIN_CONFIRMED.getValue());
	
	public List<OrderModel> getPreOrder(Integer userId, Integer productId, ProductType productType) {
		String sql = "select * from " + TABLE_NAME 
				+ " where user_id = :userId and product_id = :productId and product_type = :type and order_status in (:status)  ";
		MapSqlParameterSource params = new MapSqlParameterSource("userId", userId)
				.addValue("productId", productId)
				.addValue("type", productType.getValue())
				.addValue("status", PRE_ORDER_STATUS_LIST)
				;
		
		List<OrderModel> list = getNamedParameterJdbcTemplate().query(sql, params, MAPPER);
		return list;
	}
}
