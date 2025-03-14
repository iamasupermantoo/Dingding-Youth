package com.youshi.zebra.misc.dao;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.misc.model.ClientVersion;
import com.dorado.mvc.reqcontext.Platform;

/**
 * 
 * 
 * 
 * @author wangsch
 * @date		2016年11月14日
 *
 */
@Repository
public class ClientVersionDAO extends AbstractDAO<Integer, ClientVersion> {
	private static final String TABLE_NAME = "client_version";

	private static final RowMapper<ClientVersion> MAPPER = (rs, num)->
			new ClientVersion(rs.getInt("id"), rs.getString("from_version"), rs.getString("version"), 
					rs.getBoolean("force"), Platform.valueOf(rs.getString("platform")), 
					rs.getString("url"), rs.getString("description"), rs.getString("data"), rs.getLong("create_time"));
	
	public ClientVersionDAO() {
		super(ZebraDB.admin.getZKName(), TABLE_NAME, MAPPER);
	}
	
	/**
	 * 得到最新的版本，日期最近的一个
	 * 
	 * @param platform		平台
	 * @return						{@link ClientVersion}
	 */
	public ClientVersion getLatest(Platform platform) {
		String sql = "select * from " + TABLE_NAME + " where platform = :platform order by create_time desc limit 1";
		List<ClientVersion> list = getNamedParameterJdbcTemplate()
				.query(sql, new MapSqlParameterSource("platform", platform.toString()), MAPPER);
		
		if(CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		
		return null;
	}
	
	/**
	 * @param version
	 * @param platform
	 * @param url
	 * @param description
	 * @param online
	 * @param createTime
	 * @return					影响行数
	 */
	public int insert(String version, Platform platform, String url, String description, 
			long createTime) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "insert into " + TABLE_NAME + " (version, platform, url, description, data, create_time) "
				+ "values(:version, :platform, :url, :description, :data, :ct)";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("version", version)
					.addValue("platform", platform.name()).addValue("url", url)
					.addValue("description", description)
					.addValue("data", StringUtils.EMPTY)
					.addValue("ct", createTime);

		return getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
	}

}
