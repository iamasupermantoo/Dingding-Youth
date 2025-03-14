package com.youshi.zebra.connect.dao.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.youshi.zebra.connect.dao.ConnectUserDAO;
import com.youshi.zebra.connect.model.ConnectUser;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
public abstract class AbsConnectUserDAO implements ConnectUserDAO {

    private RowMapper<ConnectUser> rowMapper = (rs, rowNum) -> new ConnectUser(
            rs.getInt("user_id"), 
            rs.getString("external_user_id"), 
            rs.getLong("create_time"));
    
	/**
	 * 获取表名
	 * 
	 * @return
	 */
    protected abstract String getTableName();

    protected abstract NamedParameterJdbcTemplate getJdbcTemplate();

    @Override
    public ConnectUser getByExternalUserId(String externalUserId) {
        String sql = "select user_id, external_user_id, create_time from " + getTableName() 
                + " where external_user_id = :externalUserId";
        MapSqlParameterSource params = new MapSqlParameterSource("externalUserId", externalUserId);
        try {
            return getJdbcTemplate().queryForObject(sql, params, rowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public ConnectUser getByUserId(int userId) {
        String sql = "select user_id, external_user_id, create_time from " + getTableName() 
                + " where user_id = :user_id";
        MapSqlParameterSource params = new MapSqlParameterSource("user_id", userId);
        try {
            return getJdbcTemplate().queryForObject(sql, params, rowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public Map<String, Integer> getByExternalUserIds(Collection<String> externalUserIds) {
        if (CollectionUtils.isEmpty(externalUserIds)) {
            return Collections.emptyMap();
        }
        String sql = "select user_id, external_user_id from " + getTableName() 
                + " where external_user_id in (:ids)";

        final Map<String, Integer> result = new HashMap<>();
        MapSqlParameterSource params = new MapSqlParameterSource("ids", externalUserIds);

        getJdbcTemplate().query(
                sql,
                params,
                (RowCallbackHandler) rs -> result.put(rs.getString("external_user_id"),
                        rs.getInt("user_id")));

        return result;
    }

    @Override
    public Map<Integer, String> getByByUserIds(Collection<Integer> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        String sql = "select user_id, external_user_id from " + getTableName() 
                + " where user_id in (:ids)";

        final Map<Integer, String> result = new HashMap<>(userIds.size());
        MapSqlParameterSource params = new MapSqlParameterSource("ids", userIds);

        getJdbcTemplate().query(
                sql,
                params,
                (RowCallbackHandler) rs -> result.put(rs.getInt("user_id"),
                        rs.getString("external_user_id")));

        return result;
    }

    @Override
    public int insert(int userId, String externalUserId, long createTime) {
        String sql = "insert ignore into " + getTableName()
                + " (user_id, external_user_id, create_time) " 
                + " values (:user_id, :externalUserId, :createTime)";
        MapSqlParameterSource params = new MapSqlParameterSource() 
                .addValue("user_id", userId) 
                .addValue("externalUserId", externalUserId)
                .addValue("createTime", createTime);

        return getJdbcTemplate().update(sql, params);
    }

    @Override
    public int remove(int userId) {
        return getJdbcTemplate().update("delete from " + getTableName() + " where user_id=:user_id",
                Collections.singletonMap("user_id", userId));
    }

}
