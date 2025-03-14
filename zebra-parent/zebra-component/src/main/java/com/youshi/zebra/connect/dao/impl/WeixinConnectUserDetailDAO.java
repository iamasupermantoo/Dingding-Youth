package com.youshi.zebra.connect.dao.impl;

import javax.annotation.PostConstruct;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dorado.framework.datasource.DataSourceByZooKeeper;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@Repository("weixinConnectUserDetailDAO")
public class WeixinConnectUserDetailDAO extends AbsConnectUserDetailDAO {
	
	private static final String TABLE_NAME = "weixin_connect_user_detail";
	
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
