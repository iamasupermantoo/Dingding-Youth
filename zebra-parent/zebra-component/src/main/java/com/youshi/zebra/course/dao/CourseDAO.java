package com.youshi.zebra.course.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.course.constants.CourseStatus;
import com.youshi.zebra.course.constants.CourseType;
import com.youshi.zebra.course.model.CourseModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@Repository
public class CourseDAO extends AbstractDAO<Integer, CourseModel>{
	private static final String TABLE_NAME = "course";
	
	private static final RowMapper<CourseModel> MAPPER = (rs, num)->
		new CourseModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
				rs.getInt("cm_id"), rs.getInt("student_id"), rs.getInt("book_id"), rs.getInt("type")
				);
	
    public CourseDAO() {
        super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
    }
    
    public int insert(Integer cmId, Integer bookId, Integer studentId, 
    		String data, CourseStatus status, CourseType type, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(cm_id, book_id, student_id, data, status, type, create_time) "
    			+ "values(:cmid, :bid, :sid, :data, :status, :type, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource("cmId", cmId)
				.addValue("cmid", cmId)
				.addValue("bid", bookId)
				.addValue("sid", studentId)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("type", type.getValue())
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
	public List<CourseModel> getByStudentId(Integer studentId) {
		
		String sql = "select * from " + TABLE_NAME + " where student_id = :studentId";
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("studentId", studentId);
		
		List<CourseModel> list = getNamedParameterJdbcTemplate().query(sql, params, MAPPER);
		return list;
	}
	
}
