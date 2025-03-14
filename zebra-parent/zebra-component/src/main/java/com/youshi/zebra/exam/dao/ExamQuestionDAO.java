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
//import com.youshi.zebra.exam.constants.ExamQuestionStatus;
import com.youshi.zebra.exam.model.ExamQuestionModel;

/**
 * 
 * 
 * 
 * @author codegen
 */
@Repository
public class ExamQuestionDAO extends AbstractDAO<Integer, ExamQuestionModel>{
	private static final String TABLE_NAME = "exam_question";
	
	private static final RowMapper<ExamQuestionModel> MAPPER = (rs, num)->
		new ExamQuestionModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status"),
			rs.getInt("exam_id"),
			rs.getInt("num"),
			rs.getString("title"),
			rs.getString("options"),
			rs.getInt("options_mode")
			);
	
    public ExamQuestionDAO() {
        super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
    }
    
    
    public int insert(
		Integer examId,
		Integer num,
		String title,
		String options,
		Integer optionsMode,
    	String data, ExamQuestionStatus status, long createTime) {
    	String sql = "insert into " + TABLE_NAME + "(exam_id,num,title,options,options_mode,data, status, create_time) "
    			+ "values(:examId,:num,:title,:options,:optionsMode, :data, :status, :createTime)";
    	
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("examId", examId)
				.addValue("num", num)
				.addValue("title", title)
				.addValue("options", options)
				.addValue("optionsMode", optionsMode)
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("createTime", createTime)
				;
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
    	
    	return keyHolder.getKey().intValue();
    }
    
    public enum ExamQuestionStatus implements EntityStatus {
		Normal(0, "正常"),
		;
		
		private final int value;
		private final String name;
		ExamQuestionStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<ExamQuestionStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (ExamQuestionStatus e : ExamQuestionStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final ExamQuestionStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
    
    
    public enum OptionsMode implements EntityStatus {
		_0(0, "未知"),
		_1(1, "横排"),
		_2(2, "竖排"),
		;
		
		private final int value;
		private final String name;
		OptionsMode(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }
	
	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }
	
	    private static final IntObjectMap<OptionsMode> map = new IntObjectOpenHashMap<>();
	    static {
	        for (OptionsMode e : OptionsMode.values()) {
	            map.put(e.getValue(), e);
	        }
	    }
	
	    public static final OptionsMode fromValue(Integer value) {
	        return map.get(value);
	    }
	}
    
	public int updateTitle(Integer questionId, String title) {
		String sql = "update " + TABLE_NAME + " set title = :title where id = :id";
		return getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource("id", questionId)
				.addValue("title", title));
	}

	
	public int updateOptions(Integer questionId, String options, OptionsMode mode, String data) {
		String sql = "update " + TABLE_NAME + " set options = :options, options_mode = :mode, data = :data where id = :id";
		return getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource("id", questionId)
				.addValue("options", options)
				.addValue("mode", mode.getValue())
				.addValue("data", data)
				);
	}
	
	public int updateMode(Integer questionId, OptionsMode mode) {
		String sql = "update " + TABLE_NAME + " set options_mode = :mode where id = :id";
		return getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource("id", questionId)
				.addValue("mode", mode.getValue()));
	}
}
