package com.youshi.zebra.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.model.UserModel;

/**
 * 用户DAO
 * 
 * Date: Jun 6, 2016
 * 
 * @author wangsch
 *
 */
@Repository
public class UserDAO extends AbstractDAO<Integer, UserModel> {
	
	private static final RowMapper<UserModel> MAPPER = new RowMapper<UserModel>() {

		@Override
		public UserModel mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new UserModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status") , 
					rs.getString("name"), rs.getInt("type"), rs.getString("signature"), rs.getBoolean("verified"));
		}
	};
	
	
    public UserDAO() {
        super(ZebraDB.user.getZKName(), "user", MAPPER);
    }
    
    /**
     * 写入一条记录
     */
    public int insert(String name, UserType type, String signature, String data, 
    		UserStatus status, long createTime) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(
                "insert into " + tableName + "(name, type, signature, data, status, create_time)"
                		+ " values(:name, :type, :signature, :data, :status, :createTime)",
                new MapSqlParameterSource("name", name)
                	.addValue("type", type.getValue())
                	.addValue("signature", signature)
                	.addValue("data", data)
                	.addValue("status", status.getValue())
                	.addValue("createTime", createTime), keyHolder);
        return keyHolder.getKey().intValue();
    }

	/**
	 * 
	 * 更新用户信息
	 * 
	 * @param userId
	 * @param name
	 * @param signature
	 * @param newData
	 * @param verified
	 * @return
	 */
	public int update(Integer userId, String name, String signature, String newData, boolean verified) {
		String sql = "update " + tableName + " set name = :name, signature = :signature, data=:newData,verified=:verified "
				+ "where id=:id";
		int count = getNamedParameterJdbcTemplate().update(sql,
				new MapSqlParameterSource("id", userId)
					.addValue("name", name)
					.addValue("signature", signature)
					.addValue("newData", newData)
					.addValue("verified", verified)
				);
		return count;
	}
}
