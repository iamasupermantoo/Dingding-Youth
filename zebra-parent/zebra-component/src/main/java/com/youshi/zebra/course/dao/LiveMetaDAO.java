package com.youshi.zebra.course.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.EntityStatus;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.course.model.LiveMetaModel;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class LiveMetaDAO extends AbstractDAO<Integer, LiveMetaModel>{
	private static final String TABLE_NAME = "live_meta";
	
	private static final RowMapper<LiveMetaModel> MAPPER = (rs, num)->
		new LiveMetaModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("type"),
			rs.getString("open_time"),
			rs.getInt("teacher_id")
			);
	
    public LiveMetaDAO() {
        super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
    }
    
    
    public int insert(
		int type,
		String openTime,
		Integer teacher,
    	String data, LiveMetaStatus status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(type,open_time,data, status, create_time,teacher_id) "
    			+ "values(:type,:openTime, :data, :status, :createTime,:teacher)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("type", type)
				.addValue("openTime", openTime)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				.addValue("teacher", teacher)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
    
    
    public int updateStatus( Integer id , Integer status ) {
    	String sql = " UPDATE " + TABLE_NAME + " SET status=:status where id=:id ";
    	MapSqlParameterSource params = new MapSqlParameterSource()
    			.addValue("id", id)
    			.addValue("status", status);
    	int update = getNamedParameterJdbcTemplate().update(sql, params);
    	return update;
    }
    
    
    public int updateMeta( Integer id , String openTime , String data ) {
    	String sql = " UPDATE " + TABLE_NAME + " SET "
    			+ " data=:data "
    			+ " , open_time=:openTime "
    			+ "WHERE id=:id ";
    	MapSqlParameterSource params = new MapSqlParameterSource()
    			.addValue("id", id)
    			.addValue("data", data)
    			.addValue("openTime", openTime);
    	int update = getNamedParameterJdbcTemplate().update(sql, params);
    	return update;
    }
    
    
    
    public enum LiveMetaStatus implements EntityStatus {
		Normal(0, "正常"),
		Del(4, "正常"),
		;
		
		private final int value;
		private final String name;
		LiveMetaStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<LiveMetaStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (LiveMetaStatus e : LiveMetaStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final LiveMetaStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	    
}
