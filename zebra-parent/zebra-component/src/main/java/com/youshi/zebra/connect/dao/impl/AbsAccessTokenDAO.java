package com.youshi.zebra.connect.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.youshi.zebra.connect.dao.AccessTokenDAO;
import com.youshi.zebra.connect.model.AccessToken;

/**
 * {@link AccessToken} DAO抽象类，因为第三方账号，通常都有相同的授权机制和流程（如：oauth）。<br />
 * 不同的第三方账号信息，DB表名不同，但结构相同，所以可以对此就行抽象。
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
public abstract class AbsAccessTokenDAO implements AccessTokenDAO {
    protected org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    private RowMapper<AccessToken> rowMapper = (rs, rowNum) -> new AccessToken(rs.getInt("id"), 
            rs.getString("access_token"), 
            rs.getString("refresh_token"), 
            rs.getString("external_user_id"), 
            rs.getLong("create_time"));

	/**
	 * 获取表名
	 * @return 表名
	 */
    protected abstract String getTableName();
    
    /**
     * 获取{@link NamedParameterJdbcTemplate}
     * 
     * @return {@link NamedParameterJdbcTemplate}
     */
    protected abstract NamedParameterJdbcTemplate getJdbcTemplate();

    @Override
    public AccessToken getAccessToken(String accessToken) {
        String sql = "select id, access_token, refresh_token, external_user_id, create_time from "
                + getTableName() + " where access_token = :accessToken";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("accessToken", accessToken);
        try {
            return getJdbcTemplate().queryForObject(sql, params, rowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public AccessToken getAccessTokenByExternalUser(String externalUserId) {
        String sql = "select id, access_token, refresh_token, external_user_id, create_time from "
                + getTableName() + " where external_user_id = :externalUserId";
        MapSqlParameterSource params = new MapSqlParameterSource() 
                .addValue("externalUserId", externalUserId);
        try {
            return getJdbcTemplate().queryForObject(sql, params, rowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public int insertOrUpdateAccessToken(String externalUserId, String accessToken, String refreshToken, long time) {
        String sql = "insert into " + getTableName() + " (access_token, refresh_token, external_user_id, create_time, update_time) " 
                + " values (:accessToken, :refreshToken, :externalUserId, :createTime, :updateTime) " 
                + " on duplicate key update access_token = values(access_token), "
                + " refresh_token = values(refresh_token),"
                + " update_time = values(update_time)";

        MapSqlParameterSource params = new MapSqlParameterSource() 
                .addValue("accessToken", accessToken) 
                .addValue("refreshToken", StringUtils.trimToNull(refreshToken)) 
                .addValue("externalUserId", externalUserId) 
                .addValue("createTime", time) 
                .addValue("updateTime", time);

        return getJdbcTemplate().update(sql, params);
    }
}
