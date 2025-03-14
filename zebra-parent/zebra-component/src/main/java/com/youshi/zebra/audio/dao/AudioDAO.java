package com.youshi.zebra.audio.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.audio.model.AudioModel;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@Repository
public class AudioDAO extends AbstractDAO<Integer, AudioModel>{
	private static final String TABLE_NAME = "audio";
	
	private static final RowMapper<AudioModel> MAPPER = (rs, num)->{
		return new AudioModel(rs.getInt("id"), rs.getString("data"), 
				rs.getLong("create_time"), rs.getInt("status"));
	};
	
	
	public AudioDAO() {
		super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
	}
}
