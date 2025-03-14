package com.youshi.zebra.connect.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.youshi.zebra.connect.dao.ConnectUserDetailDAO;
import com.youshi.zebra.connect.model.ConnectUserDetail;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public abstract class AbsConnectUserDetailDAO implements ConnectUserDetailDAO {
	private RowMapper<ConnectUserDetail> rowMapper = (rs, rowNum) -> new ConnectUserDetail(
			rs.getString("external_user_id"), 
            rs.getString("data"), 
            rs.getLong("create_time"));
    
	/**
	 * 获取表名
	 * 
	 * @return
	 */
    protected abstract String getTableName();

    protected abstract NamedParameterJdbcTemplate getJdbcTemplate();
    
    @Override
    public ConnectUserDetail getConnectUserDetail(String externalUserId) {
    	
    	String sql = "select * from " + getTableName() + " where external_user_id = :externalUserId";
		ConnectUserDetail userDetail = getJdbcTemplate().queryForObject(sql, 
				new MapSqlParameterSource("externalUserId", externalUserId), rowMapper);
    	
    	return userDetail;
    }
    
    
    public int insertOrUpdateConnectUserDetail(String externalUserId, String data, long time) {
        String sql = "insert into " + getTableName() + " (external_user_id, data, create_time, update_time) " 
                + " values (:externalUserId, :data, :createTime, :updateTime) " 
                + " on duplicate key update data = values(data), "
                + " update_time = values(update_time)";

        MapSqlParameterSource params = new MapSqlParameterSource() 
                .addValue("externalUserId", externalUserId) 
                .addValue("data", data)
                .addValue("createTime", time) 
                .addValue("updateTime", time);

        return getJdbcTemplate().update(sql, params);
    }
}
