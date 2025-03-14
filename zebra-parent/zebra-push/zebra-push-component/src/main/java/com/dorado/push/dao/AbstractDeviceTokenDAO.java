package com.dorado.push.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.dorado.framework.datasource.DataSourceByZooKeeper;


public abstract class AbstractDeviceTokenDAO extends NamedParameterJdbcDaoSupport 
	implements DeviceTokenDAO<String> {

	public static final String BIZ_NAME = "doradoPush";
	
//	private static final RowMapper<TokenModel> MAPPER = (rs, rowNum) -> 
//			new TokenModel(rs.getInt("id"), rs.getInt("user_id"), rs.getString("token"), rs.getDate("create_time"));
	
	@PostConstruct
	public void injectDataSource() {
		setDataSource(DataSourceByZooKeeper.of(BIZ_NAME));
	}
	
	/**
	 * 表名，登陆的token记录
	 * 
	 * @return	表名
	 */
	public abstract String getLoginTableName();
	
	/**
	 * 表名，未登陆的token记录
	 * 
	 * @return	表名
	 */
	public abstract String getUnloginTableName();
	

	/**
	 * 建立用户和token绑定关系
	 * 
	 * @param userId	用户id
	 * @param token		token
	 * 
	 */
	@Override
	public void bind(Integer userId, String token) {
		if (StringUtils.isBlank(token)) {
			return;
		}
		
		// 增加未登陆token记录，如果在登陆token里有，忽略
		if (userId == null) {
			if (StringUtils.isEmpty(getExistToken(token))) {
				insertUnLogin(userId, token);
			}
		
		// 增加到登陆token记录，注意：删除上次退出时保存的token
		} else {
			insert(userId, token);
			delUnLoginByToken(token);
		}
	}

	@Override
	public void unbind(Integer userId) {
		String token = getTokenByUserId(userId);
		if (StringUtils.isNotEmpty(token)) {
			insertUnLogin(userId, token);
			del(userId, token);
		}
	}

	
	// ----------------------------------------------------------------------分割线----------------------------------------------------------------------
	@Override
	public Map<Integer, Collection<String>> getTokens(Collection<Integer> userIds) {
		if (CollectionUtils.isEmpty(userIds)) {
			return Collections.emptyMap();
		}
		String sql = "select user_id, token from "+getLoginTableName()+" where user_id in (:ids)";

		final Map<Integer, Collection<String>> result = new HashMap<>();
		getNamedParameterJdbcTemplate().query(sql, Collections.singletonMap("ids", userIds), new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				int userId = rs.getInt("user_id");
				String token = rs.getString("token");
				Collection<String> tokens = result.get(userId);
				if (tokens == null) {
					tokens = new HashSet<>();
					result.put(userId, tokens);
				}
				tokens.add(token);
			}
		});
		return result;
	}

	
	// -------------------------------------------------private method------------------------------------------------------
	/**
	 * 写一条，登陆时
	 * 
	 * @param userId
	 * @param token
	 */
	private void insert(Integer userId, String token) {
		String sql = "replace into "+getLoginTableName()+" (user_id, token, create_time)"
				+ " values (:userId, :token, now())";

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId", userId).addValue("token", token);

		getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	/**
	 * 根据用户id获取token
	 * 
	 * @param userId		用户id
	 * @return					token
	 */
	private String getTokenByUserId(Integer userId) {

		String sql = "select token from "+getLoginTableName()+" where user_id =:user_id";
		try {
			return getNamedParameterJdbcTemplate().queryForObject(sql, Collections.singletonMap("user_id", userId), String.class);
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
	
	/**
	 * 获取已存在token
	 * 
	 * @param token		token
	 * @return					token或者null，如果不存在token
	 */
	private String getExistToken(String token) {

		String sql = "select token from "+getLoginTableName()+" where token =:token";
		try {
			return getNamedParameterJdbcTemplate().queryForObject(sql, Collections.singletonMap("token", token), String.class);
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
	
	/**
	 * 删除已登录记录
	 * 
	 * @param userId			用户id
	 * @param token				token
	 */
	private void del(Integer userId, String token) {
		String sql = "delete from "+getLoginTableName()+" where user_id = :user_id and token = :token";
		MapSqlParameterSource params = new MapSqlParameterSource() //
		.addValue("user_id", userId).addValue("token", token);
		getNamedParameterJdbcTemplate().update(sql, params);
	}

	
	// -------------------------------------------------unlogin operations------------------------------------------------------
	/**
	 * 写一条，未登录时
	 * 
	 * @param userId			用户id，未登录的用户id传null就行了。
	 * @param token			token
	 */
	private void insertUnLogin(Integer userId, String token) {
		// 当一个设备换用户登录，替换用户
		String sql = "replace into "+getUnloginTableName()+" (user_id, token, create_time)"
				+ " values (:userId, :token, now())";

		MapSqlParameterSource params = new MapSqlParameterSource()
		.addValue("userId", userId).addValue("token", token);

		getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	/**
	 * 删除未登录记录，根据token
	 * @param token		token
	 */
	private void delUnLoginByToken(String token) {
		String sql = "delete from "+getUnloginTableName()+" where token = :token";
		getNamedParameterJdbcTemplate().update(sql, Collections.singletonMap("token", token));
	}

}
