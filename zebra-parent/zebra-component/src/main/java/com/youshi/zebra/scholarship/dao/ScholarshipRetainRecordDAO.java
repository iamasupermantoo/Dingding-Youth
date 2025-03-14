package com.youshi.zebra.scholarship.dao;

import java.util.List;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.model.EntityStatus;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.scholarship.model.ScholarshipRetainRecordModel;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class ScholarshipRetainRecordDAO extends AbstractDAO<Integer, ScholarshipRetainRecordModel>{
	private static final String TABLE_NAME = "scholarship_retain_record";
	
	private static final RowMapper<ScholarshipRetainRecordModel> MAPPER = (rs, num)->
		new ScholarshipRetainRecordModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("user_id")
			);
	
    public ScholarshipRetainRecordDAO() {
        super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
    }
    
    
    public int insert(
		int userId,
    	String data, ScholarshipRetainRecordStatus status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(user_id,data, status, create_time) "
    			+ "values(:userId, :data, :status, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId", userId)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
    public enum ScholarshipRetainRecordStatus implements EntityStatus {
		WAIT(0, "待审核"),
		SUCC(1, "提现成功"),
		FAIL(2, "提现失败"),
		;
		
		private final int value;
		private final String name;
		ScholarshipRetainRecordStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<ScholarshipRetainRecordStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (ScholarshipRetainRecordStatus e : ScholarshipRetainRecordStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final ScholarshipRetainRecordStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	    
}
