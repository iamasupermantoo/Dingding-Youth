package com.youshi.zebra.pay.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.mvc.reqcontext.Platform;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.order.constants.PayStatus;
import com.youshi.zebra.pay.constants.PayChannel;
import com.youshi.zebra.pay.model.PayLogModel;

/**
 * 
 * 支付日志DAO
 * 
 * @author wangsch
 * @date 2017年4月18日
 */
@Repository
public class PayLogDAO extends AbstractDAO<Integer, PayLogModel>{
	private static final String TABLE_NAME = "pay_log";
	
	private static final RowMapper<PayLogModel> MAPPER = (rs, num)->
		new PayLogModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), 
				rs.getString("order_sn"), 
				rs.getInt("pay_status"), rs.getInt("pay_channel"), rs.getInt("platform"), rs.getLong("update_time"));
	
	public PayLogDAO() {
		super(ZebraDB.order.getZKName(), TABLE_NAME, MAPPER);
	}
	
	public PayLogModel getByOrderSn(String orderSn) {
		String sql = "select * from " + TABLE_NAME + " where order_sn = :orderSn";
		try {
			
			PayLogModel result = getNamedParameterJdbcTemplate().queryForObject(sql, 
					new MapSqlParameterSource("orderSn", orderSn), MAPPER);
			return result;
		} catch(IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
	
	
	public int insert(String orderSn, PayStatus payStatus, PayChannel payChannel, Platform platform,
			String data, long createTime) {
		String sql = "insert into " + TABLE_NAME + " (order_sn, pay_status, pay_channel, platform, data, create_time) "
				+ " values(:orderSn, :payStatus, :payChannel, :platform, :data, :createTime)";
		MapSqlParameterSource params = new MapSqlParameterSource("orderSn", orderSn)
				.addValue("payStatus", payStatus.getValue())
				.addValue("payChannel", payChannel.getValue())
				.addValue("platform", platform.getValue())
				.addValue("data", data)
				.addValue("createTime", createTime)
				;
		GeneratedKeyHolder hk = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sql, params, hk);
		
		return hk.getKey().intValue();
	}
	
	public int update(String orderSn, PayStatus payStatus, PayChannel payChannel, 
			String oldData, String data, long updateTime) {
		String sql = "update " + TABLE_NAME + " set pay_status = :payStatus, data = :data, update_time = :updateTime "
				+ "where order_sn = :orderSn and pay_channel = :payChannel and data = :oldData";
		MapSqlParameterSource params = new MapSqlParameterSource("orderSn", orderSn)
				.addValue("payChannel", payChannel.getValue())
				.addValue("oldData", oldData)
				.addValue("payStatus", payStatus.getValue())
				.addValue("data", data)
				.addValue("updateTime", updateTime)
				;
		int count = getNamedParameterJdbcTemplate().update(sql, params);
		
		return count;
	}
	
	
}
