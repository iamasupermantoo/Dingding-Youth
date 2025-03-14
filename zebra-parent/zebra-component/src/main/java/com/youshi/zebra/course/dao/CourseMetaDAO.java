package com.youshi.zebra.course.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.EntityStatus;
import com.youshi.zebra.core.constants.db.ZebraDB;
import com.youshi.zebra.course.constants.CourseType;
import com.youshi.zebra.course.model.CourseMetaModel;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@Repository
public class CourseMetaDAO extends AbstractDAO<Integer, CourseMetaModel>{
	private static final String TABLE_NAME = "course_meta";
	
	private static final RowMapper<CourseMetaModel> MAPPER = (rs, num)->
		new CourseMetaModel(rs.getInt("id"), rs.getString("data"), rs.getLong("create_time"),rs.getInt("status")
				,rs.getInt("book_id"), rs.getInt("type"));
	
    public CourseMetaDAO() {
        super(ZebraDB.teaching.getZKName(), TABLE_NAME, MAPPER);
    }

    public Number insert(Integer bookId, String data, EntityStatus status, Integer type, long createTime) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(
                "insert into " + tableName + "(book_id, data, status, type, create_time) values(:bid, :d, :s, :type, :ct)",
                new MapSqlParameterSource("d", data)
	                .addValue("bid", bookId)
                	.addValue("s", status.getValue())
                	.addValue("type", type)
                	.addValue("ct", createTime), keyHolder);
        return keyHolder.getKey();
    }
}
