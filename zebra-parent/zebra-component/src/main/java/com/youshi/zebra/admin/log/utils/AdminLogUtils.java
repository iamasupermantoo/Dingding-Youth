package com.youshi.zebra.admin.log.utils;

import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.github.phantomthief.util.ObjectMapperUtils;
import com.youshi.zebra.admin.log.constants.AdminLogType;
import com.youshi.zebra.admin.log.model.AdminLogModel;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * 
 * 运营后台，管理员操作日志工具。实际上是个DAO类，操作数据库，写操作日志到表中
 * 
 * @author wangsch
 *
 * @date 2016-09-12
 */
@Repository
public class AdminLogUtils extends AbstractDAO<Long, AdminLogModel> {
	/**
	 * 
	 */
	private static final String TABLE_NAME = "admin_log";

	private static final RowMapper<AdminLogModel> MAPPER = (rs, i) -> 
		new AdminLogModel(rs.getLong("id"), rs.getInt("admin_id"), AdminLogType.fromValue(rs.getInt("type")), 
			rs.getString("data"), rs.getLong("create_time"), rs.getLong("ip"));
	
	/**
	 * QUES
	 */
    public static final String IGNORE_LOG_SIGN = "__ignoreAdminLog";
    
    /**
     * 自定义admin log参数，admin log的参数由两部分组成。
     * 
     * 1. http request解析出的参数
     * 2. 自定义参数，业务代码中，通过{@link #setAdminLogParams(Map)}设置
     * 
     */
    public static final String ADMIN_LOG_PARAMS = "__ADMIN_LOG_PARAMS__";
    
    /**
     * 使用独立的日志logger记录，记录到独立的文件中
     */
    private final org.slf4j.Logger adminLog = org.slf4j.LoggerFactory.getLogger("adminLog");

	public AdminLogUtils() {
		super(ZebraDB.admin.getZKName(), TABLE_NAME, MAPPER);
	}
    
    
    /**
     * 添加一条记录
     * 
     * @param userId		管理员id
     * @param type			日志类型
     * @param params		此次操作涉及到的具体参数{@link #ADMIN_LOG_PARAMS}
     */
    public void record(Integer userId, AdminLogType type, Long ip, Map<String, Object> params) {
    	if(userId == null) {
    		return; 
    	}
    	
        String data = ObjectMapperUtils.toJSON(params);
        // pattern: 用户id|日志类型|参数|操作者ip
        adminLog.info("{}|{}|{}|{}", userId, type, data, WebRequestContext.getCurrentIpInString());
        
        getNamedParameterJdbcTemplate().update(
                "insert into admin_log(admin_id,type,data,create_time,ip)"
                        + "values(:u,:t,:d,:ct,:ip)",
                new MapSqlParameterSource("u", userId).addValue("t", type.getValue())
                        .addValue("d", data).addValue("ct", System.currentTimeMillis())
                        .addValue("ip", ip));
    }

    /*
     * what's this??? QUES
     */
    public static final void ignoreAdminLog() {
        RequestContextHolder.currentRequestAttributes().setAttribute(IGNORE_LOG_SIGN, true,
                RequestAttributes.SCOPE_REQUEST);
    }
    
    
    /**
     * admin log自定义参数，这个需要手动调用{@link #setAdminLogParams(Map)}方法。
     * @return	Map
     */
    @SuppressWarnings("unchecked")
    public static final Map<String, String> getAdminLogParams() {
        try {
            Map<String, String> map = (Map<String, String>) RequestContextHolder.currentRequestAttributes()
                    .getAttribute(ADMIN_LOG_PARAMS, RequestAttributes.SCOPE_REQUEST);
            return map;
        } catch (IllegalStateException e) {
            return null;
        }
    }
    
    /**
     * 如果需要记录自定义参数，调用这个
     * @param param	参数Map
     */
    public static void setAdminLogParams(Map<String, String> param) {
        RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
        ra.setAttribute(ADMIN_LOG_PARAMS, param, RequestAttributes.SCOPE_REQUEST);
    }
}
