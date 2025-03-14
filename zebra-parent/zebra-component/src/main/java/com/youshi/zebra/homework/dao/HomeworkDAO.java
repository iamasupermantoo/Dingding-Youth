package com.youshi.zebra.homework.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.homework.constants.HomeworkStatus;
import com.youshi.zebra.homework.model.HomeworkModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@Repository
public class HomeworkDAO extends AbstractDAO<Integer, HomeworkModel>{
	private static final String TABLE_NAME = "homework";
	
	private static final RowMapper<HomeworkModel> MAPPER = (rs, num)
			->new HomeworkModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), rs.getInt("status"), 
					rs.getInt("student_id"), rs.getInt("teacher_id"), rs.getInt("course_id"), rs.getInt("lesson_id"));
	
	protected HomeworkDAO() {
		super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
	}
	
	
	public HomeworkModel getHomework(Integer courseId, Integer lessonId) {
		String sql = "select * from " + TABLE_NAME + " where course_id = :cid and lesson_id = :lid";
		MapSqlParameterSource params = new MapSqlParameterSource("cid", courseId)
				.addValue("lid", lessonId);
		try {
			HomeworkModel result = getNamedParameterJdbcTemplate().queryForObject(sql, params, MAPPER);
			return result;
		} catch(IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
	
	public int insert(Integer teacherId, Integer studentId, Integer courseId, Integer lessonId, 
			String data, HomeworkStatus status, long createTime) {
		String sql = "insert into " + TABLE_NAME + "(course_id, lesson_id, teacher_id, student_id, data, status, create_time) "
				+ "values(:courseId, :lessonId, :teacherId, :studentId, :data, :status, :createTime)";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("courseId", courseId)
				.addValue("lessonId", lessonId)
				.addValue("teacherId", teacherId)
				.addValue("studentId", studentId)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				;
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	public int getWaitCommit(Integer studentId) {
		String sql = "select count(1) from " + TABLE_NAME + " where student_id = :sid and status = :status";
		MapSqlParameterSource params = new MapSqlParameterSource("sid", studentId)
				.addValue("status", HomeworkStatus.WAIT_COMMIT.getValue());
		Integer count = getNamedParameterJdbcTemplate().queryForObject(sql, params, Integer.class);
		
		return count;
	}
}
