package com.youshi.zebra.lesson.dao;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.homework.constants.HomeworkStatus;
import com.youshi.zebra.lesson.constants.LessonStatus;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.reaction.constants.StudentReactionStatus;
import com.youshi.zebra.reaction.constants.TeacherReactionStatus;
import com.youshi.zebra.user.constant.UserType;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月14日
 */
@Repository
public class LessonDAO extends AbstractDAO<Integer, LessonModel>{
	private static final String TABLE_NAME = "lesson";
	
	private static final RowMapper<LessonModel> MAPPER = (rs, num)
			->new LessonModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), rs.getInt("status"), 
					rs.getInt("homework_status"), rs.getInt("student_reaction_status"), rs.getInt("teacher_reaction_status"), 
					rs.getInt("course_id"), rs.getInt("teacher_id"), rs.getInt("chapter_id"), rs.getInt("student_id"),
					rs.getString("date"), rs.getString("time"));
	
	protected LessonDAO() {
		super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
	}
	
	public int insert(Integer courseId, Integer chapterId, Integer teacherId, Integer studentId, 
			String data, LessonStatus status, String date, String time, long createTime) {
		String sql = "insert into " + TABLE_NAME + "(course_id, chapter_id, teacher_id, student_id, data, status, date, time, create_time) "
				+ "values(:courseId, :chapterId, :teacherId, :studentId, :data, :status, :date, :time, :createTime)";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("courseId", courseId)
				.addValue("chapterId", chapterId)
				.addValue("teacherId", teacherId)
				.addValue("studentId", studentId)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("date", date)
				.addValue("time", time)
				.addValue("createTime", createTime)
				;
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	public List<LessonModel> getDayLessons(Integer courseId, Integer userId, UserType userType, String date) {
		StringBuilder sb = new StringBuilder("select * from " + TABLE_NAME 
				+ " where date = :date");
		MapSqlParameterSource params = new MapSqlParameterSource("date", date);
		
		switch(userType) {
			case Student:
				params.addValue("sid", userId);
				sb.append(" and student_id = :sid ");
				break;
			case Teacher:
				params.addValue("tid", userId);
				sb.append(" and teacher_id = :tid ");
				break;
			default:
				throw new IllegalArgumentException("Unsupported userType: " + userType);
		}
		
		if(courseId != null) {
			sb.append(" and course_id = :cid");
			params.addValue("cid", courseId);
		}
		List<LessonModel> result = getNamedParameterJdbcTemplate()
				.query(sb.toString(), params, MAPPER);
		
		return result;
	}
	
	public List<LessonModel> getTeacherLessons(Integer courseId, Integer teacherId, String date) {
		StringBuilder sb = new StringBuilder("select * from " + TABLE_NAME 
				+ " where date = :date and teacher_id = :tid");
		MapSqlParameterSource params = new MapSqlParameterSource("date", date)
				.addValue("tid", teacherId);
		if(courseId != null) {
			sb.append(" and course_id = :cid");
			params.addValue("cid", courseId);
		}
		List<LessonModel> result = getNamedParameterJdbcTemplate()
				.query(sb.toString(), params, MAPPER);
		
		return result;
	}
	
	public List<String> getPoints(Integer courseId, Integer userId, 
			UserType userType, String dateStart, String dateEnd) {
		StringBuilder sb = new StringBuilder("select date from " + TABLE_NAME 
				+ " where date >= :start and date < :end");
		
		MapSqlParameterSource params = new MapSqlParameterSource("start", dateStart)
				.addValue("end", dateEnd)
				;
		
		switch(userType) {
			case Student:
				params.addValue("sid", userId);
				sb.append(" and student_id = :sid ");
				break;
			case Teacher:
				params.addValue("tid", userId);
				sb.append(" and teacher_id = :tid ");
				break;
			default:
				throw new IllegalArgumentException("Unsupported userType: " + userType);
		}
		if(courseId != null) {
			sb.append(" and course_id = :cid");
			params.addValue("cid", courseId);
		}
		List<String> result = getNamedParameterJdbcTemplate()
				.queryForList(sb.toString(), params, String.class);
		
		return result;
	}
	
	public int deleteLesson(Integer courseId, Integer lessonId, LessonStatus wait) {
		String sql = "delete from " + TABLE_NAME + " where course_id = :cid and id = :lid and status = :wait";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("cid", courseId)
				.addValue("lid", lessonId)
				.addValue("wait", wait.getValue())
				;
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		return c;
	}

	/**
	 * @param courseId
	 * @param lessonId
	 * @return
	 */
	public LessonModel getOneLesson(Integer courseId, Integer lessonId) {
		String sql = "select * from " + TABLE_NAME + " where course_id = :cid and id = :lid";
		MapSqlParameterSource params = new MapSqlParameterSource("cid", courseId).addValue("lid", lessonId);
		try {
			LessonModel result = getNamedParameterJdbcTemplate().queryForObject(sql, params, MAPPER);
			return result;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
		
	}

	public int updateStatus(Integer courseId, Integer lessonId, LessonStatus status) {
		String sql = "update " + TABLE_NAME + " set status = :status where course_id = :cid and id = :lid";
		MapSqlParameterSource params = new MapSqlParameterSource("lid", lessonId)
				.addValue("cid", courseId)
				.addValue("status", status.getValue());
		
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		
		return c;
	}
	
	public int updateHomeworkStatus(Integer courseId, Integer lessonId, HomeworkStatus status) {
		String sql = "update " + TABLE_NAME + " set homework_status = :status where course_id = :cid and id = :lid";
		MapSqlParameterSource params = new MapSqlParameterSource("lid", lessonId)
				.addValue("cid", courseId)
				.addValue("status", status.getValue());
		
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		
		return c;
	}
	
	public int updateTeacherReactionStatus(Integer courseId, Integer lessonId, TeacherReactionStatus status) {
		String sql = "update " + TABLE_NAME + " set teacher_reaction_status = :status where course_id = :cid and id = :lid";
		MapSqlParameterSource params = new MapSqlParameterSource("lid", lessonId)
				.addValue("cid", courseId)
				.addValue("status", status.getValue());
		
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		
		return c;
	}
	
	public int updateStudentReactionStatus(Integer courseId, Integer lessonId, StudentReactionStatus status) {
		String sql = "update " + TABLE_NAME + " set student_reaction_status = :status where course_id = :cid and id = :lid";
		MapSqlParameterSource params = new MapSqlParameterSource("lid", lessonId)
				.addValue("cid", courseId)
				.addValue("status", status.getValue());
		
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		
		return c;
	}

	public int update(Integer courseId, Integer lessonId, Integer teacherId, String date, String time) {
		String sql = "update " + TABLE_NAME + " set date = :date, time = :time, teacher_id = :tid where course_id = :cid and id = :lid";
		MapSqlParameterSource params = new MapSqlParameterSource("lid", lessonId)
				.addValue("cid", courseId)
				.addValue("tid", teacherId)
				.addValue("date", date).addValue("time", time);
		
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		
		return c;
	}

	public int updateData(Integer courseId, Integer lessonId, String data) {
		String sql = "update " + TABLE_NAME + " set data = :data where course_id = :cid and id = :id";
		MapSqlParameterSource params = new MapSqlParameterSource("cid", courseId)
				.addValue("id", lessonId)
				.addValue("data", data)
				;
		return getNamedParameterJdbcTemplate().update(sql, params);
	}
}
