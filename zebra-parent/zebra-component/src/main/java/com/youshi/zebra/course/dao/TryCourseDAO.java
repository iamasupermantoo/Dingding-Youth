package com.youshi.zebra.course.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.EntityStatus;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.course.model.TryCourseModel;
import com.youshi.zebra.order.constants.ChatResult;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class TryCourseDAO extends AbstractDAO<Integer, TryCourseModel>{
	private static final String TABLE_NAME = "try_course";
	
	private static final RowMapper<TryCourseModel> MAPPER = (rs, num)->
		new TryCourseModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("cm_id"),
			rs.getInt("course_id"),
			rs.getInt("student_id"),
			rs.getInt("chat_result"),
			rs.getBoolean("paid"),
			rs.getBoolean("planned"),
			rs.getLong("update_time")
			);
	
    public TryCourseDAO() {
        super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
    }
    
    
    public int insert(
    	Integer cmId,
		Integer courseId,
		int studentId,
		int chatResult,
		boolean paid,
		boolean planned,
		Long updateTime,
    	String data, TryCourseStatus status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(cm_id,course_id,student_id,chat_result,paid,planned,update_time,data, status, create_time) "
    			+ "values(:cmId,:courseId,:studentId,:chatResult,:paid,:planned,:updateTime, :data, :status, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("cmId", cmId)
				.addValue("courseId", courseId)
				.addValue("studentId", studentId)
				.addValue("chatResult", chatResult)
				.addValue("paid", paid)
				.addValue("planned", planned)
				.addValue("updateTime", updateTime)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
    public TryCourseModel getByStudentId(Integer studentId) {
    	String sql = "select * from " + TABLE_NAME + " where student_id = :sid";
		MapSqlParameterSource params = new MapSqlParameterSource("sid", studentId);
		
		try {
			return getNamedParameterJdbcTemplate().queryForObject(sql, params, MAPPER);
		} catch(IncorrectResultSizeDataAccessException e) {
			return null;
		}
    }
    
    public TryCourseModel getTryCourse(Integer studentId, Integer cmId) {
    	String sql = "select * from " + TABLE_NAME + " where student_id = :sid and cm_id = :cmId";
		MapSqlParameterSource params = new MapSqlParameterSource("sid", studentId)
				.addValue("cmId", cmId)
				;
		
		try {
			return getNamedParameterJdbcTemplate().queryForObject(sql, params, MAPPER);
		} catch(IncorrectResultSizeDataAccessException e) {
			return null;
		}
    }
    

	public int updateCourseId(Integer studentId, int courseId, int chatResult, 
			boolean paid, boolean planned) {
		String sql = "update " + TABLE_NAME + " set course_id = :cid, chat_result = :chatResult, paid = :paid, planned = :planned "
				+ "where student_id = :sid";
		MapSqlParameterSource params = new MapSqlParameterSource("cid", courseId)
				.addValue("chatResult", chatResult)
				.addValue("paid", paid)
				.addValue("planned", planned)
				.addValue("sid", studentId)
				;
		int c = getNamedParameterJdbcTemplate().update(sql, params);
		return c;
	}
    
    public enum TryCourseStatus implements EntityStatus {
		Normal(0, "正常"),
		;
		
		private final int value;
		private final String name;
		TryCourseStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<TryCourseStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (TryCourseStatus e : TryCourseStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final TryCourseStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
    
    public int updateChatResult(Integer studentId, ChatResult result, String data) {
		String sql = "update " + TABLE_NAME 
				+ " set chat_result = :result, data=:data where student_id = :studentId";
		MapSqlParameterSource params = new MapSqlParameterSource("studentId", studentId)
				.addValue("result", result.getValue())
				.addValue("data", data)
				;
		
		int count = getNamedParameterJdbcTemplate().update(sql, params);
		return count;
	}
}
