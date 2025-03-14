package com.youshi.zebra.connect.dao.impl;

import javax.annotation.PostConstruct;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dorado.framework.datasource.DataSourceByZooKeeper;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * Date: May 10, 2016
 * 
 * @author wangsch
 *
 */
@Repository("qqAccessTokenDAO")
public class QQAccessTokenDAO extends AbsAccessTokenDAO {

	private static final String TABLE_NAME = "qq_access_token";
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@PostConstruct
	public void init() {
		jdbcTemplate = new NamedParameterJdbcTemplate(DataSourceByZooKeeper.of(ZebraDB.connect.getZKName()));
	}
	
	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

}
