package com.youshi.zebra.lesson.dao;

import javax.annotation.PostConstruct;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.dorado.framework.datasource.DataSourceByZooKeeper;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.lesson.model.LessonReadyStatusModel;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月17日
 */
@Repository
public class LessonReadyStatusDAO extends NamedParameterJdbcDaoSupport {
	private static final String TABLE_NAME = "lesson_ready_status";
	
	private static final RowMapper<LessonReadyStatusModel> MAPPER = (rs, num)
			->new LessonReadyStatusModel(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("user_id"), 
					rs.getInt("user_id"), rs.getInt("user_id"), rs.getInt("user_id"), rs.getString("data"));
			
	@PostConstruct
	protected void init() {
		setDataSource(DataSourceByZooKeeper.of(ZebraDB.teaching.getZKName()));
	}
	
	
	
}
