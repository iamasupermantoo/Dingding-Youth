package com.youshi.zebra.homework.dao;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.homework.constants.AnswerType;
import com.youshi.zebra.homework.constants.HomeworkAnswerStatus;
import com.youshi.zebra.homework.model.HomeworkAnswerModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@Repository
public class HomeworkAnswerDAO extends AbstractDAO<Integer, HomeworkAnswerModel>{
	private static final String TABLE_NAME = "homework_answer";
	
	private static final RowMapper<HomeworkAnswerModel> MAPPER = (rs, num)
			->new HomeworkAnswerModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), rs.getInt("status"), 
					rs.getInt("homework_id"), rs.getInt("type"), rs.getString("content"));
	
	protected HomeworkAnswerDAO() {
		super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
	}
	
	
	/**
	 * 插入一条答案
	 * 
	 * @return 主键id
	 * 
	 */
	public int insert(Integer homeworkId, String content, AnswerType type, 
			String data, HomeworkAnswerStatus status, long createTime) {
		String sql = "insert into " + TABLE_NAME + "(homework_id, content, type, data, status, create_time) "
				+ "values(:homeworkId, :content, :type, :data, :status, :createTime)";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("homeworkId", homeworkId)
			.addValue("content", content)
			.addValue("type", type.getValue())
			.addValue("data", data)
			.addValue("status", status.getValue())
			.addValue("createTime", createTime);
		
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	public List<HomeworkAnswerModel> getNormalAnswers(Integer homeworkId) {
		String sql = "select * from  " + TABLE_NAME
				+ " where homework_id = :homeworkId and status = :status order by create_time desc";
		
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("homeworkId", homeworkId)
				.addValue("status", HomeworkAnswerStatus.Normal.getValue());
		return getNamedParameterJdbcTemplate().query(sql, params, MAPPER);
	}
	
	
	public List<HomeworkAnswerModel> getAllAnswers(Integer homeworkId) {
		String sql = "select * from  " + TABLE_NAME
				+ " where homework_id = :homeworkId order by create_time desc";
		
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("homeworkId", homeworkId);
		return getNamedParameterJdbcTemplate().query(sql, params, MAPPER);
	}


	/**
	 * @param homeworkId
	 * @param answerId
	 * @return
	 */
	public HomeworkAnswerModel getOneAnswer(Integer homeworkId, Integer answerId) {
		String sql = "select * from  " + TABLE_NAME
				+ " where homework_id = :homeworkId and id = :id";
		
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("homeworkId", homeworkId)
				.addValue("id", answerId);
		try {
			HomeworkAnswerModel answerModel = getNamedParameterJdbcTemplate().queryForObject(sql, params, MAPPER);
			return answerModel;
		} catch(IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
	
	
	
	
}
