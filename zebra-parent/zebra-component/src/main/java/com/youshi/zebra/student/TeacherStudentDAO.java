package com.youshi.zebra.student;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.student.model.TeacherStudentModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月21日
 */
@Repository
public class TeacherStudentDAO extends AbstractDAO<Integer, TeacherStudentModel>{
	private static final String TABLE_NAME = "teacher_student";
	
	private static final RowMapper<TeacherStudentModel> MAPPER = (rs, num)->
		new TeacherStudentModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),  rs.getInt("status"), 
			rs.getInt("teacher_id"), rs.getInt("student_id"));
	
	protected TeacherStudentDAO() {
		super(ZebraDB.user.getZKName(), TABLE_NAME, MAPPER);
	}
	
	public int insert(Integer teacherId, Integer studentId, long createTime) {
		String sql = "insert into " + TABLE_NAME + "(teacher_id, student_id, create_time) "
				+ "values(:tid, :sid, :ct)";
		GeneratedKeyHolder kh = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("tid", teacherId)
				.addValue("sid", studentId)
				.addValue("ct", createTime);
		getNamedParameterJdbcTemplate().update(sql, params, kh);
		
		return kh.getKey().intValue();
	}
}
