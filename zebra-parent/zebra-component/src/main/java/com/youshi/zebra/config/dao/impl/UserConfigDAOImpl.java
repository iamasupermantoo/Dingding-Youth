package com.youshi.zebra.config.dao.impl;

import java.util.Collections;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.config.constants.UserConfigStatus;
import com.youshi.zebra.config.model.UserConfigModel;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * 用户配置信息，push开关之类的
 * 
 * @author wangsch
 * @date 2016-10-20
 */
@Repository
public class UserConfigDAOImpl extends AbstractDAO<Integer, UserConfigModel> 
	implements UserConfigDAO{
	
	private static final RowMapper<UserConfigModel> MAPPER = (rs, rownum)->{
		return new UserConfigModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), rs.getInt("status"),
				rs.getInt("user_id"));
	};
	
	protected UserConfigDAOImpl() {
		super(ZebraDB.user.getZKName(), "user_config", MAPPER);
	}

	@Override
	public boolean save(Integer userId, String key, String value) {
		UserConfigModel config = getByUserId(userId);
		int rowAffect = 0;
		if(config == null) {
			String sql = "insert into " + tableName + " (user_id, data, status, create_time) values (:userId, :data, :status, :createTime) ";
			rowAffect = getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource()
					.addValue("userId", userId)
					.addValue("data", ObjectMapperUtils.toJSON(Collections.singletonMap(key, value)))
					.addValue("status", UserConfigStatus.Normal.getValue())
					.addValue("createTime", System.currentTimeMillis()));
		} else {
			rowAffect = updateData(config.getId(), (oldDataMap)->{
				oldDataMap.put(key, value);
			}, config);
		}
		
		return rowAffect == 1;
	}

	public UserConfigModel getByUserId(Integer userId) {
		String sql = "select * from " + tableName + " where user_id = :userId";
		
		try {
			return getNamedParameterJdbcTemplate().queryForObject(sql, 
					new MapSqlParameterSource("userId", userId), MAPPER);
		} catch(DataAccessException e) {
			return null;
		}
	}

}
