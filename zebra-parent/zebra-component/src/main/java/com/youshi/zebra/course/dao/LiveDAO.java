package com.youshi.zebra.course.dao;

import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.EntityStatus;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.course.model.LiveModel;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class LiveDAO extends AbstractDAO<Integer, LiveModel>{
	private static final String TABLE_NAME = "live";
	
	private static final RowMapper<LiveModel> MAPPER = (rs, num)->
		new LiveModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("lm_id"),
			rs.getInt("student_id"),
			rs.getInt("type"),
			rs.getString("open_time")
			);
	
    public LiveDAO() {
        super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
    }
    
    public int insert(
		int lmId,
		int studentId,
		int type,
		String openTime,
    	String data, LiveStatus status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(lm_id,student_id,type,open_time,data, status, create_time) "
    			+ "values(:lmId,:studentId,:type,:openTime, :data, :status, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("lmId", lmId)
				.addValue("studentId", studentId)
				.addValue("type", type)
				.addValue("openTime", openTime)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
 
    public LiveModel getByStudentIdAndLmId( Integer userId , Integer lmid ) {
    	String sql = "SELECT * FROM " + TABLE_NAME + " WHERE student_id = :userId AND lm_id=:lmid ";
    	MapSqlParameterSource params = new MapSqlParameterSource()
    			.addValue("userId", userId)
    			.addValue("lmid", lmid);
    	LiveModel result = null;
    	try {
    		result = getNamedParameterJdbcTemplate().queryForObject(sql, params, MAPPER);
		} catch (DataAccessException e) {
		}
		return result;
    }
    
    
    
    
    public enum LiveStatus implements EntityStatus {
		Normal(0, "正常"),
		;
		
		private final int value;
		private final String name;
		LiveStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<LiveStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (LiveStatus e : LiveStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final LiveStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	    
}
