package com.youshi.zebra.course.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.course.model.CourseStatusModel;

/**
 * 
 * @author wangsch
 * @date 2017年3月27日
 */
@Repository
public class CourseStatusDAO extends AbstractDAO<Integer, CourseStatusModel>{
	private static final String TABLE_NAME = "course_status";
	
	private static final RowMapper<CourseStatusModel> MAPPER = (rs, num) -> 
		new CourseStatusModel(rs.getInt("id"), 
				rs.getInt("planed_max_cnt"), rs.getInt("finished_max_cnt"), rs.getLong("create_time"));
	
	public CourseStatusDAO() {
		super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
	}
	
	public int insert(Integer courseId, long createTime) {
		String sql = "insert into " + TABLE_NAME + " (id, planed_max_cnt, finished_max_cnt, create_time) "
				+ "values (:cid, :planedCnt, :finishedCnt, :ct)";
		MapSqlParameterSource params = new MapSqlParameterSource("cid", courseId)
				.addValue("planedCnt", 0)
				.addValue("finishedCnt", 0)
				.addValue("ct", createTime)
				;
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		
		return c;
		
	}
	
	public int incrPlanedMaxCnt(Integer courseId, int incr, long time) {
		String sql = "update "+TABLE_NAME+" set `planed_max_cnt` = `planed_max_cnt` + :incr "
				+ "where id = :cid";
		MapSqlParameterSource params = new MapSqlParameterSource("cid", courseId)
				.addValue("incr", incr)
				;
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		return c;
	}
	
	public int incrFinishedMaxCnt(Integer courseId, int incr, long time) {
		String sql = "update " + TABLE_NAME + " set `finished_max_cnt` = `finished_max_cnt` + :incr "
				+ "where id = :cid";
		MapSqlParameterSource params = new MapSqlParameterSource("cid", courseId)
				.addValue("incr", incr)
				;
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		
		return c;
	}
	
}
