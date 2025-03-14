package com.youshi.zebra.recommend.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.gotopage.constant.GotoPage;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.recommend.constants.BannerStatus;
import com.youshi.zebra.recommend.constants.BannerType;
import com.youshi.zebra.recommend.model.RecommendBannerModel;

/**
 * 
 * 首页推荐，顶部轮播
 * 
 * @author wangsch
 * @date 2016-09-24
 */
@Repository
public class RecommendBannerDAO extends AbstractDAO<Integer, RecommendBannerModel> {

	private static final String TABLE_NAME = "recommend_banner";
	
	private static final RowMapper<RecommendBannerModel> MAPPER = 
			new RowMapper<RecommendBannerModel>() {
				@Override
				public RecommendBannerModel mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new RecommendBannerModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"), rs.getInt("status"), 
							rs.getInt("data_id"), rs.getInt("gotopage_type"), rs.getString("gotopage_params"), rs.getInt("type"));
				}
			};
	
	protected RecommendBannerDAO() {
		super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
	}
	
	public List<RecommendBannerModel> getAllOnline(BannerType type) {
		String sql = "select * from " + tableName + " where status = :status and type = :type order by `order` asc";
		return getNamedParameterJdbcTemplate().query(sql, 
				new MapSqlParameterSource()
					.addValue("status", BannerStatus.Online.getValue())
					.addValue("type", type.getValue()), 
				MAPPER);
	}
	
	public List<RecommendBannerModel> getAll(BannerType type) {
		String sql = "select * from " + tableName + " where type = :type order by `order` asc";
		return getNamedParameterJdbcTemplate().query(sql, 
				new MapSqlParameterSource()
					.addValue("type", type.getValue()), 
				MAPPER);
	}
	
	
	public int insert(Integer dataId, GotoPage gotoPage, String gotoParams, BannerType type, Integer order,
			BannerStatus status, String data, long createTime) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(
                "insert into " + TABLE_NAME + "(data_id, gotopage_type, gotopage_params, type, `order`, status, data, create_time) "
                		+ "values(:dataId, :gotoType, :gotoParams, :type, :order, :status, :data, :createTime)",
                new MapSqlParameterSource("dataId", dataId)
                	.addValue("gotoType", gotoPage.getValue())
                	.addValue("gotoParams", gotoParams)
                	.addValue("type", type.getValue())
                	.addValue("order", order)
                	.addValue("status", status.getValue())
                	.addValue("data", data)
                	.addValue("createTime", createTime), keyHolder);
        return keyHolder.getKey().intValue();
	}

	/**
	 * 按照List里边的顺序，保存banner的order
	 * @param bannerIds
	 * @return
	 */
	public int saveOrders(List<Integer> bannerIds) {
		String sql = "update " + TABLE_NAME + " set `order` = :order where id = :id and status=:status";
		
		MapSqlParameterSource[] params = new MapSqlParameterSource[bannerIds.size()];
		for (int i=0; i<bannerIds.size(); i++) {
			int order = i + 1;
			params[i] = new MapSqlParameterSource()
					.addValue("id", bannerIds.get(i))
					.addValue("order", order)
					.addValue("status", BannerStatus.Online.getValue());
		}
		
		int[] rowAffects = getNamedParameterJdbcTemplate().batchUpdate(sql, params);
		int totalRowAffects = 0;
		for (int rowAffect : rowAffects) {
			totalRowAffects+=rowAffect;
		}
		return totalRowAffects;
	}

}
