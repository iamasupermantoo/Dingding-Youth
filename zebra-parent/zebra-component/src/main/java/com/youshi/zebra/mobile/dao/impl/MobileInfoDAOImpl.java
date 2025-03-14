package com.youshi.zebra.mobile.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.datasource.DataSourceByZooKeeper;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.mobile.constants.MobileCodeStatus;
import com.youshi.zebra.mobile.dao.MobileInfoDAO;
import com.youshi.zebra.mobile.model.MobileInfo;

/**
 * 验证码相关DAO实现
 * 
 * @author wangsch
 * @date 2016-09-12
 */
@Repository
public class MobileInfoDAOImpl extends NamedParameterJdbcDaoSupport
        implements MobileInfoDAO, RowMapper<MobileInfo> {

    @PostConstruct
    protected void injectDataSource() {
        setDataSource(DataSourceByZooKeeper.of(ZebraDB.mobile.getZKName()));
    }

    @Override
    public MobileInfo getRegisterCode(String phone) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(
                    "select * from mobile_info where phone=:phone",
                    Collections.singletonMap("phone", phone), this);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * 重新获取验证码时：把重试次数、更新时间、code、状态，更新下
     */
    @Override
    public int updateRegisterCode(String phone, String code, MobileCodeStatus status) {
    	return getNamedParameterJdbcTemplate().update(
    			"update mobile_info set status=:status, code = :code, try_count = 0, update_time = :upTime where phone=:phone",
    			new MapSqlParameterSource("phone", phone)
    				.addValue("code", code)
    				.addValue("upTime", System.currentTimeMillis())
    				.addValue("status", status.getValue()));
        
    }
    
    @Override
    public int updateRegisterCodeStatus(String phone, MobileCodeStatus status) {
    	return getNamedParameterJdbcTemplate().update(
                "update mobile_info set status=:status where phone=:phone",
                new MapSqlParameterSource("phone", phone).addValue("status", status.getValue()));
    }

    @Override
    public long createRegisterCode(String phone, String code) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(
                "insert into mobile_info (phone, code, status, create_time) values(:phone, :code, :status, :createTime)",
                new MapSqlParameterSource("phone", phone).addValue("code", code)
                        .addValue("status", MobileCodeStatus.MobileCodePending.getValue())
                        .addValue("createTime", System.currentTimeMillis()),
                keyHolder);
        return keyHolder.getKey().longValue();
    }

	@Override
	public int incrRegisterCodeTryCount(String phone) {
        return getNamedParameterJdbcTemplate().update(
                "update mobile_info set try_count = try_count + 1 where phone=:phone",
                new MapSqlParameterSource("phone", phone));
    }

    @Override
    public MobileInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MobileInfo(rs.getLong("id"), rs.getString("phone"), rs.getString("code"),
                rs.getLong("update_time"), rs.getLong("create_time"), 
                rs.getInt("status"), rs.getInt("ttl"), rs.getInt("try_count"));
    }

    // --------------------------------------------------- 重置密码 ---------------------------------------------------
    @Override
    public MobileInfo getResetCode(String phone) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(
                    "select * from mobile_reset_code where phone=:phone",
                    Collections.singletonMap("phone", phone), this);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int removeResetCode(String phone) {
        return getNamedParameterJdbcTemplate().update(
                "delete from mobile_reset_code where phone=:phone",
                Collections.singletonMap("phone", phone));
    }

    @Override
    public int createOrUpdateResetCode(String phone, String code, MobileCodeStatus status) {
        long time = System.currentTimeMillis();
		return getNamedParameterJdbcTemplate().update(
                "insert into mobile_reset_code (phone, code, status, create_time) "
		                + "values(:phone, :code, :status, :createTime) "
		                + "on duplicate key update code=values(code), try_count = 0, status = :status, update_time = :upTime",
                new MapSqlParameterSource("phone", phone).addValue("code", code)
                        .addValue("upTime", time)
                        .addValue("status", status.getValue())
                        .addValue("createTime", time));
    }

	@Override
	public int incrResetCodeTryCount(String phone) {
        return getNamedParameterJdbcTemplate().update(
                "update mobile_reset_code set try_count = try_count + 1 where phone=:phone",
                new MapSqlParameterSource("phone", phone));
    }

	@Override
	public int updateResetCodeStatus(String phone, MobileCodeStatus status) {
    	return getNamedParameterJdbcTemplate().update(
                "update mobile_reset_code set status=:status where phone=:phone",
                new MapSqlParameterSource("phone", phone).addValue("status", status.getValue()));
	}

	// --------------------------------------------------- 绑定第三方 ---------------------------------------------------
	@Override
	public MobileInfo getConnectCode(String phone) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(
                    "select * from mobile_connect_code where phone=:phone",
                    Collections.singletonMap("phone", phone), this);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
	}

	@Override
	public int createOrUpdateConnectCode(String phone, String code, MobileCodeStatus status) {
        long time = System.currentTimeMillis();
		return getNamedParameterJdbcTemplate().update(
                "insert into mobile_connect_code (phone, code, status, create_time) "
		                + "values(:phone, :code, :status, :createTime) "
		                + "on duplicate key update code=values(code), try_count = 0, status = :status, update_time = :upTime",
                new MapSqlParameterSource("phone", phone).addValue("code", code)
                        .addValue("upTime", time)
                        .addValue("status", status.getValue())
                        .addValue("createTime", time));
	}

	@Override
	public int removeConnectCode(String phone) {
        return getNamedParameterJdbcTemplate().update(
                "delete from mobile_connect_code where phone=:phone",
                Collections.singletonMap("phone", phone));
    
	}

	/* (non-Javadoc)
	 * @see com.youshi.zebra.mobile.dao.MobileInfoDAO#updateConnectCodeStatus(java.lang.String, com.youshi.zebra.mobile.constants.MobileCodeStatus)
	 */
	@Override
	public int updateConnectCodeStatus(String phone, MobileCodeStatus status) {
    	return getNamedParameterJdbcTemplate().update(
                "update mobile_connect_code set status=:status where phone=:phone",
                new MapSqlParameterSource("phone", phone).addValue("status", status.getValue()));
	
	}

	/* (non-Javadoc)
	 * @see com.youshi.zebra.mobile.dao.MobileInfoDAO#incrConnectCodeTryCount(java.lang.String)
	 */
	@Override
	public int incrConnectCodeTryCount(String phone) {
        return getNamedParameterJdbcTemplate().update(
                "update mobile_connect_code set try_count = try_count + 1 where phone=:phone",
                new MapSqlParameterSource("phone", phone));
	}
}
