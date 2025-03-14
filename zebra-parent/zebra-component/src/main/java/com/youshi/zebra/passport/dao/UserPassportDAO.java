package com.youshi.zebra.passport.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.dorado.framework.datasource.DataSourceByZooKeeper;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.passport.model.UserPassportModel;

/**
 * 手机账号密码，DAO实现
 * 
 * @author wangsch
 * @date 2016年12月24日
 */
@Repository
public class UserPassportDAO extends NamedParameterJdbcDaoSupport 
	implements RowMapper<UserPassportModel>{
	
	@PostConstruct
    protected void init() {
        setDataSource(DataSourceByZooKeeper.of(ZebraDB.passport.getZKName()));
    }

	@Override
	public UserPassportModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new UserPassportModel(rs.getInt("id"), rs.getString("mobile"), 
				rs.getString("salt"), rs.getString("password"));
	}

	public UserPassportModel getUserPassport(String mobile) {
        try {
        	return getNamedParameterJdbcTemplate().queryForObject(
        			"select * from user_passport where mobile=:m", 
        			new MapSqlParameterSource("m", mobile), this);
        } catch(IncorrectResultSizeDataAccessException e) {
        	return null;
        }
    }
	
	public UserPassportModel getUserPassport(int userId) {
        try {
        	return getNamedParameterJdbcTemplate().queryForObject(
        			"select * from user_passport where id=:userId", 
        			new MapSqlParameterSource("userId", userId), this);
        } catch(IncorrectResultSizeDataAccessException e) {
        	return null;
        }
    }

    public int insert(int userId, String mobile, String md5Crypt, String salt) {
        return getNamedParameterJdbcTemplate().update(
                "insert into user_passport(id,mobile,password,salt) values(:i,:m,:p,:s) "
                        + "on duplicate key update password=values(password), salt=values(salt)",
                new MapSqlParameterSource("i", userId).addValue("m", mobile)
                        .addValue("p", md5Crypt).addValue("s", salt));
    }

    public boolean isRegisterd(String mobile) {
    		Integer count = getNamedParameterJdbcTemplate().queryForObject(
    				"select count(*) as count from user_passport where mobile=:m",
    				new MapSqlParameterSource("m", mobile), (rs, num)->rs.getInt("count"));
    	return count == 1;
    }

    public Map<String, Integer> getRegisteredUserIds(Collection<String> mobiles) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        if (CollectionUtils.isNotEmpty(mobiles)) {
                getNamedParameterJdbcTemplate().query(
                        "select id, mobile from user_passport where mobile in (:ids)",
                        new MapSqlParameterSource("ids", mobiles),
                        (RowCallbackHandler) rs -> result.put(rs.getString("mobile"), rs.getInt("id")));
        }
        return result;
    }
    
    public int updatePassword(int userId, String password, String salt, String md5Crypt) {
        return getNamedParameterJdbcTemplate().update(
                "update user_passport set password=:p, salt=:s where id=:i",
                new MapSqlParameterSource("i", userId).addValue("p", md5Crypt).addValue("s", salt));
    }
}
