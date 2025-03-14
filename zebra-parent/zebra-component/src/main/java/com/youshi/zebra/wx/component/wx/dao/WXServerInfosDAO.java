package com.youshi.zebra.wx.component.wx.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.wx.component.wx.model.WXServerInfos;

/**
 * 
 * 微信公众号，token、ticket等信息，DAO
 * 
 * 
 * @author wangsch
 * @date 2017年4月21日
 */
@Repository
public class WXServerInfosDAO extends AbstractDAO<Integer ,WXServerInfos> {
	public static final RowMapper<WXServerInfos> MAPPER = (rs, num)->
		new WXServerInfos(rs.getLong("create_time"), rs.getString("app_id"), 
			rs.getString("access_token"), rs.getInt("token_expires_in"), 
			rs.getString("jsapi_ticket"), rs.getInt("ticket_expires_in"));
	
	public WXServerInfosDAO() {
		super(ZebraDB.order.getZKName(), "wx_server_infos", MAPPER);
	}
	
	
	public WXServerInfos get(String appId) {
		String sql = "select * from wx_server_infos where app_id = :appId";
		MapSqlParameterSource params = new MapSqlParameterSource("appId", appId);
		List<WXServerInfos> config = getNamedParameterJdbcTemplate().query(sql, params, MAPPER);
		
		return config == null || config.size() == 0 ? null : config.get(0);
	}

	public int insert(String appid, String accessToken, int tokenExpiresIn, String jsapiTicket,
			Integer ticketExpiresIn, long createTime) {
		
		String sql = "insert into wx_server_infos values(:appId, :accessToken, :tokenExpiresIn, :jsapiTicket,"
				+ ":ticketExpiresIn, :createTime) on duplicate key update access_token = values(access_token), "
				+ "token_expires_in = values(token_expires_in), jsapi_ticket = values(jsapi_ticket), "
				+ "ticket_expires_in = values(ticket_expires_in), create_time = values(create_time)";
		MapSqlParameterSource params = new MapSqlParameterSource()
			.addValue("appId", appid)
			.addValue("accessToken", accessToken)
			.addValue("tokenExpiresIn", tokenExpiresIn)
			.addValue("jsapiTicket", jsapiTicket)
			.addValue("ticketExpiresIn", ticketExpiresIn)
			.addValue("createTime", createTime);
		
		return getNamedParameterJdbcTemplate().update(sql, params);
	}
}
