package com.youshi.zebra.exam.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.EntityStatus;
import com.youshi.zebra.core.constants.db.ZebraDB;
//import com.youshi.zebra.exam.constants.ExamStatus;
import com.youshi.zebra.exam.model.ExamModel;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class ExamDAO extends AbstractDAO<Integer, ExamModel>{
	private static final String TABLE_NAME = "exam";
	
	private static final RowMapper<ExamModel> MAPPER = (rs, num)->
		new ExamModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("level"),
			rs.getString("name")
			);
	
    public ExamDAO() {
        super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
    }
    
    
    public int insert(
		Integer level,
		String name,
    	String data, ExamStatus status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(level,name,data, status, create_time) "
    			+ "values(:level,:name, :data, :status, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("level", level)
				.addValue("name", name)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
    public enum ExamStatus implements EntityStatus {
		Normal(0, "正常"),
		;
		
		private final int value;
		private final String name;
		ExamStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<ExamStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (ExamStatus e : ExamStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final ExamStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	    
}
