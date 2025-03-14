package com.youshi.zebra.image.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.image.model.ImageModel;

/**
 * @author wangsch
 * @date 2017年1月4日
 */
@Repository
public class ImageDAO extends AbstractDAO<Integer, ImageModel>{
	private static final String TABLE_NAME = "image";
	
	private static final RowMapper<ImageModel> MAPPER = (rs, num)->{
		return new ImageModel(rs.getInt("id"), rs.getString("data"), 
				rs.getLong("create_time"), rs.getInt("status"));
	};
	
	
	public ImageDAO() {
		super(ZebraDB.image.getZKName(), TABLE_NAME, MAPPER);
	}
}
