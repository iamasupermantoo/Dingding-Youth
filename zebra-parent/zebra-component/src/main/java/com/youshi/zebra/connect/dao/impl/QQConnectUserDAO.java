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
@Repository("qqConnectUserDAO")
public class QQConnectUserDAO extends AbsConnectUserDAO {
	private static final String TABLE_NAME = "qq_connect_user";
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@PostConstruct
	public void init() {
		jdbcTemplate = new NamedParameterJdbcTemplate(DataSourceByZooKeeper.of(ZebraDB.connect.getZKName()));
	}
	
	/* (non-Javadoc)
	 * @see com.dorado.connect.dao.impl.AbsConnectUserDAO#getTableName()
	 */
	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	/* (non-Javadoc)
	 * @see com.dorado.connect.dao.impl.AbsConnectUserDAO#getJdbcTemplate()
	 */
	@Override
	protected NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

}
