package com.youshi.zebra.admin.adminuser.dao.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.dorado.framework.datasource.DataSourceByZooKeeper;
import com.youshi.zebra.admin.adminuser.constant.Privilege;
import com.youshi.zebra.admin.adminuser.dao.AdminPrivilegeDAO;
import com.youshi.zebra.admin.adminuser.model.AdminPrivilegeModel;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * 
 * 
 * @author wangsch
 * @date 2016-09-12
 */
@Repository
public class AdminPrivilegeDAOImpl extends NamedParameterJdbcDaoSupport
	implements AdminPrivilegeDAO {
	private static final Logger logger = LoggerFactory.getLogger(AdminPrivilegeDAOImpl.class);
	
    @PostConstruct
    protected void init() {
        setDataSource(DataSourceByZooKeeper.of(ZebraDB.admin.getZKName()));
    }

    @Override
    public Map<Privilege, AdminPrivilegeModel> getAll(Integer userId) {
        String sql = "select * from admin_privilege where user_id = :user_id";

        MapSqlParameterSource params = new MapSqlParameterSource("user_id", userId);

        final Map<Privilege, AdminPrivilegeModel> result = new HashMap<>();

        getNamedParameterJdbcTemplate().query(sql, params, rs -> {
            try {
                Privilege type = Privilege.valueOf(rs.getString("privilege"));
                result.put(type,
                        new AdminPrivilegeModel(rs.getInt("id"), rs.getInt("user_id"), type, rs.getLong("expire_time")));
            } catch (Throwable e) {
                logger.warn("no role found for:{}", rs.getString("privilege"));
            }
        });

        return result;
    }

    @Override
    public int deleteAll(Integer userId) {
        String sql = "delete from admin_privilege where user_id = :user_id";
        MapSqlParameterSource params = new MapSqlParameterSource("user_id", userId);
        return getNamedParameterJdbcTemplate().update(sql, params);
    }

    @Override
    public int insertAll(Integer userId, Map<Privilege, Long> privileges) {
        String sql = "insert into admin_privilege (user_id, `privilege`, expire_time) "
                + "values (:user_id, :privilege, :expireTime)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId);

        int count = 0;
        for (Entry<Privilege, Long> entry : privileges.entrySet()) {
            params.addValue("privilege", entry.getKey().name());
            params.addValue("expireTime", entry.getValue());
            count += getNamedParameterJdbcTemplate().update(sql, params);
        }

        return count;
    }
}
