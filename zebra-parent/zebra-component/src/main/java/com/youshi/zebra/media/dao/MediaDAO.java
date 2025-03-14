package com.youshi.zebra.media.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.media.constants.MediaStatus;
import com.youshi.zebra.media.constants.MediaType;
import com.youshi.zebra.media.model.MediaModel;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
@Repository
public class MediaDAO extends AbstractDAO<Integer, MediaModel>{
	private static final String TABLE_NAME = "media";
	
	private static final RowMapper<MediaModel> MAPPER = (rs, num)-> 
		new MediaModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), rs.getInt("status"), 
				rs.getInt("type"));
	
	
	protected MediaDAO() {
		super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
	}
	
	public int insert(MediaType type, String data, long createTime, MediaStatus status) {
		String sql = "insert into " + TABLE_NAME + " (type, data, status, create_time) values(:type, :data, :status, :ct)";
		MapSqlParameterSource params = new MapSqlParameterSource("type", type.getValue())
				.addValue("data", data)
				.addValue("status", status.getValue())
				.addValue("ct", createTime);
		GeneratedKeyHolder kHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(sql , params, kHolder);
		
		return kHolder.getKey().intValue();
	}
}
