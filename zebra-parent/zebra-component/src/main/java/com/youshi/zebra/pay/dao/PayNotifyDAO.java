package com.youshi.zebra.pay.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.pay.constants.PayChannel;
import com.youshi.zebra.pay.model.PayNotifyModel;

/**
 * 
 * 支付通知，支付宝和微信回掉后，把所有信息都存下来。通过order_sn和订单表，一一关联
 * 
 * @author wangsch
 * @date 2017年4月18日
 */
@Repository
public class PayNotifyDAO extends AbstractDAO<Integer, PayNotifyModel>{
	private static final String TABLE_NAME = "pay_notify";
	
	private static RowMapper<PayNotifyModel> MAPPER = (rs, num)->
		new PayNotifyModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), rs.getString("order_sn"), 
				rs.getInt("pay_channel"));
	
	public PayNotifyDAO() {
		super(ZebraDB.order.getZKName(), TABLE_NAME, MAPPER);
	}
	
	public int insert(String orderSn, PayChannel payChannel, String data, long createTime) {
		String sql = "insert into " + TABLE_NAME + "(order_sn, pay_channel, data, create_time) "
				+ "values(:orderSn, :payChannel, :data, :createTime)";
		MapSqlParameterSource params = new MapSqlParameterSource("orderSn", orderSn)
				.addValue("payChannel", payChannel.getValue())
				.addValue("data", data)
				.addValue("createTime", createTime)
				;
		GeneratedKeyHolder kh = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sql, params, kh);
		
		return kh.getKey().intValue();
	}

	/**
	 * @param notify_id
	 * @return
	 */
	public PayNotifyModel getByNotifyId(String notifyId) {
		
		
		
		return null;
	}
}
