package com.youshi.zebra.recommend.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.recommend.constants.RecommendFeedType;
import com.youshi.zebra.recommend.model.RecommendFeedModel;

/**
 * @author wangsch
 * @date 2016-09-24
 */
@Repository
public class RecommendFeedDAO extends AbstractDAO<Integer, RecommendFeedModel> {

	private static final String TABLE_NAME = "recommend_feed";
	
	private static final RowMapper<RecommendFeedModel> MAPPER = 
			new RowMapper<RecommendFeedModel>() {
				@Override
				public RecommendFeedModel mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new RecommendFeedModel(
							rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), rs.getInt("status"), 
							rs.getInt("author_id"), rs.getInt("data_id"), rs.getInt("type"));
				}
			};
	
	protected RecommendFeedDAO() {
		super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
	}
	
	/**
	 * 写入一条feed记录
	 * 
	 * @return	返回影响行数
	 */
	public int insert(Integer dataId, Integer authorId, RecommendFeedType type, 
			RecommendFeedStatus status, String data, long createTime) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        return getNamedParameterJdbcTemplate().update(
                "insert ignore into " + TABLE_NAME + "(data_id, author_id, type, status, data, create_time) "
                		+ "values(:dataId, :authorId, :type, :status, :data, :createTime)",
                new MapSqlParameterSource("dataId", dataId)
                	.addValue("authorId", authorId)
                	.addValue("type", type.getValue())
                	.addValue("status", status.getValue())
                	.addValue("data", data)
                	.addValue("createTime", createTime), keyHolder);
	}

	public RecommendFeedModel getFeed(Integer dataIntId, RecommendFeedType type) {
		String sql = "select * from " + TABLE_NAME + " where data_id = :dataId and type = :type";
		MapSqlParameterSource params = new MapSqlParameterSource("dataId", dataIntId)
				.addValue("type", type.getValue());
		try {
			return getNamedParameterJdbcTemplate().queryForObject(sql, params, MAPPER);
		} catch(IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}
	
	
	public int updateStatus(Integer id,	RecommendFeedStatus status ) {

        return getNamedParameterJdbcTemplate().update(
                "update  " + TABLE_NAME + "  set  `status` = :status  "
                		+ " where id = :id ",
                new MapSqlParameterSource("id", id)
                	.addValue("status", status.getValue()));
	}
	
	public int updateStatusOnPut(Integer id,RecommendFeedStatus status,Integer newId ) {

        return getNamedParameterJdbcTemplate().update(
                "update  " + TABLE_NAME + "  set  `id`= :newId , `status` = :status  "
                		+ " where id = :id ",
                new MapSqlParameterSource("id", id)
                	.addValue("newId", newId)
                	.addValue("status", status.getValue()));
	}
	
	public int delete(Integer dataId, RecommendFeedType type) {
        return getNamedParameterJdbcTemplate().update(
                "delete from  " + TABLE_NAME + " where data_id = :dataId and type = :type ",
                new MapSqlParameterSource("dataId", dataId).addValue("type", type.getValue()));
	}
	
	public enum RecommendFeedStatus {
		Normal(0, "正常"),
		AdminDel(1, "管理员删除"),
		;
		
		private final int value;
		private final String name;
		RecommendFeedStatus(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }

	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }

	    private static final IntObjectMap<RecommendFeedStatus> map = new IntObjectOpenHashMap<>();
	    static {
	        for (RecommendFeedStatus e : RecommendFeedStatus.values()) {
	            map.put(e.getValue(), e);
	        }
	    }

	    public static final RecommendFeedStatus fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	
	public int getMaxId() {

        return getNamedParameterJdbcTemplate().queryForObject
        		("select   MAX(id)+1 from " +  TABLE_NAME 
        		,new MapSqlParameterSource()
        		,Integer.class);
	}
}
