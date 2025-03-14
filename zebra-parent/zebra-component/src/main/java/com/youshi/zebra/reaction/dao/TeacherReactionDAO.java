package com.youshi.zebra.reaction.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.reaction.constants.TeacherReactionStatus;
import com.youshi.zebra.reaction.model.TeacherReactionModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
@Repository
public class TeacherReactionDAO extends AbstractDAO<Integer, TeacherReactionModel>{
	
	private static final String TABLE_NAME = "teacher_reaction";
	
	private static final RowMapper<TeacherReactionModel> MAPPER = (rs, num)->
		new TeacherReactionModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), rs.getInt("status"),
				rs.getInt("course_id"), rs.getInt("lesson_id"), rs.getInt("student_id"), rs.getInt("teacher_id"));
	
	
	public TeacherReactionDAO() {
		super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
	}

	public Integer insert(Integer courseId, Integer lessonId, Integer studentId, Integer teacherId, 
			String data, TeacherReactionStatus status, long createTime) {
		
		String sql = "insert into " + TABLE_NAME + " (course_id, lesson_id, student_id, teacher_id, data, status, create_time) "
				+ "values (:cid, :lid, :sid, :tid, :data, :status, :ct)";
		MapSqlParameterSource params = new MapSqlParameterSource("cid", courseId)
				.addValue("lid", lessonId)
				.addValue("sid", studentId)
				.addValue("tid", teacherId)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("ct", createTime)
				;
		GeneratedKeyHolder gh = new GeneratedKeyHolder();
		
		getNamedParameterJdbcTemplate().update(sql, params, gh);
		return gh.getKey().intValue();
	}
	
	public TeacherReactionModel getOne(Integer courseId, Integer lessonId) {
		String sql = "select * from " + TABLE_NAME + " where course_id = :cid and lesson_id = :lid";
		MapSqlParameterSource params = new MapSqlParameterSource("cid", courseId)
				.addValue("lid", lessonId);
		try {
			TeacherReactionModel result = getNamedParameterJdbcTemplate()
					.queryForObject(sql, params, MAPPER);
			return result;
		} catch(IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	public int update(Integer courseId, Integer lessonId, String newData, TeacherReactionStatus status) {
		String sql = "update " + TABLE_NAME + " set status = :status, data = :data "
				+ "where course_id = :cid and lesson_id = :lid";
		MapSqlParameterSource params = new MapSqlParameterSource("cid", courseId)
				.addValue("lid", lessonId)
				.addValue("data", newData)
				.addValue("status", status.getValue())
				;
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		return c;
	}
	
}
