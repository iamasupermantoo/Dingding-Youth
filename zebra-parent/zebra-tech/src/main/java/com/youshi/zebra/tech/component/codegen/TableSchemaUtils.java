package com.youshi.zebra.tech.component.codegen;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import com.dorado.framework.datasource.DataSourceByZooKeeper;

/**
 * 
 * @author wangsch
 * @date 2017年4月7日
 */
public class TableSchemaUtils {

	
	@Test
	public void aa() {
		String tableName = "ticket";
		String zkDSNode = "passport";
		
		test(tableName, zkDSNode);
		
	}
	
	public void test(String tableName, String zkDSNode) {
		String sql = "select * from "+ tableName;
		DataSourceByZooKeeper dataSource = DataSourceByZooKeeper.of(zkDSNode);
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
		SqlRowSetMetaData sqlRsmd = sqlRowSet.getMetaData();
		int columnCount = sqlRsmd.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			Map<String, String> fieldMap = new HashMap<String, String>();
			fieldMap.put("name", sqlRsmd.getColumnName(i));
			fieldMap.put("fieldType", String.valueOf(sqlRsmd.getColumnType(i)));
			
			System.out.println(sqlRsmd.getColumnName(i) + ", " + sqlRsmd.getColumnTypeName(i));
		}

	}

}
