package com.youshi.zebra.admin.adminuser.dao.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.dorado.framework.datasource.DataSourceByZooKeeper;
import com.youshi.zebra.admin.adminuser.constant.AdminUserPassportStatus;
import com.youshi.zebra.admin.adminuser.dao.AdminUserPassportDAO;
import com.youshi.zebra.admin.adminuser.model.AdminUserPassport;
import com.youshi.zebra.admin.adminuser.model.impl.AdminUserPassportModel;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * @author wangsch
 * @date 2016-09-12
 */
@Repository
public class AdminUserPassportDAOImpl extends NamedParameterJdbcDaoSupport 
	implements AdminUserPassportDAO {
    private RowMapper<AdminUserPassport> rowMapper = (rs, rowNum) -> new AdminUserPassportModel(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getInt("status"),
            rs.getString("signature"));

    @PostConstruct
    protected void init() {
        setDataSource(DataSourceByZooKeeper.of(ZebraDB.admin.getZKName()));
    }

    @Override
    public Map<Integer, AdminUserPassport> getByIds(Collection<Integer> userIds) {
        Map<Integer, AdminUserPassport> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIds)) {
            getNamedParameterJdbcTemplate().query("select * from admin_user_passport where id in (:ids)",
                    Collections.singletonMap("ids", userIds), (rs) -> {
                        AdminUserPassport userPassport = rowMapper.mapRow(rs, 0);
                        result.put(userPassport.getId(), userPassport);
                    });
        }
        return result;
    }

    @Override
    public List<AdminUserPassport> getAll() {
        String sql = "select * from admin_user_passport";
        return getNamedParameterJdbcTemplate().query(sql, Collections.emptyMap(), rowMapper);
    }

    @Override
    public AdminUserPassport getByUsername(String username) {
        String sql = "select * from admin_user_passport where username = :username";
        MapSqlParameterSource params = new MapSqlParameterSource("username", username);

        try {
            return getNamedParameterJdbcTemplate().queryForObject(sql, params, rowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public int insert(int id, String username, String password, String data, long createTime) {
        String sql = "insert into admin_user_passport (id, username, password, status, create_time, data) "
                + "values (:id, :username, :password, :status, :createTime,:data)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("username", username)
                .addValue("password", password)
                .addValue("status", AdminUserPassportStatus.Normal.getValue())
                .addValue("data", data)
                .addValue("createTime", createTime)
                ;

        return getNamedParameterJdbcTemplate().update(sql, params);
    }

    @Override
    public int updateStatus(int id, AdminUserPassportStatus status) {
        String sql = "update admin_user_passport set status = :status where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("status", status.getValue());

        return getNamedParameterJdbcTemplate().update(sql, params);
    }

    @Override
    public int updatePassword(int id, String password) {
        String sql = "update admin_user_passport set password=:psw where id=:id";
		MapSqlParameterSource params = new MapSqlParameterSource("id", id).addValue("psw", password);
		return getNamedParameterJdbcTemplate().update(sql, params);
    }

	@Override
	public int updateData(int id, String data, long updateTime) {
		String sql = "update admin_user_passport set data=:data where id=:id";
		MapSqlParameterSource params = new MapSqlParameterSource("id", id).addValue("data", data);
		return getNamedParameterJdbcTemplate().update(sql, params);
	}
}
