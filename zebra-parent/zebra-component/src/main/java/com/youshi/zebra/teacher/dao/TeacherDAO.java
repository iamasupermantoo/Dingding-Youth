package com.youshi.zebra.teacher.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.teacher.model.TeacherModel;
import com.youshi.zebra.user.constant.UserStatus;

/**
 * 老师DAO
 * 
 * @author wangsch
 * @date 2017年2月13日
 */
@Repository
public class TeacherDAO extends AbstractDAO<Integer, TeacherModel> {
	private static final String TABLE_NAME = "teacher";
	
	private static final RowMapper<TeacherModel> MAPPER = new RowMapper<TeacherModel>() {

		@Override
		public TeacherModel mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new TeacherModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"));
		}
	};
	
	
    public TeacherDAO() {
        super(ZebraDB.user.getZKName(), TABLE_NAME, MAPPER);
    }
    
    public int insert(Integer userId, String data, UserStatus status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(id, data, status, create_time) "
    			+ "values(:uid, :data, :status, :ct)";
		MapSqlParameterSource params = new MapSqlParameterSource("uid", userId)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("ct", createTime)
				;
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		
		return c;
    }
    
    public int update(Integer teacherId, String data) {
    	String sql = "update " + TABLE_NAME + " set data = :data where id = :tid";
		MapSqlParameterSource params = new MapSqlParameterSource("tid", teacherId).addValue("data", data);
		
		return getNamedParameterJdbcTemplate().update(sql, params);
    }
}
