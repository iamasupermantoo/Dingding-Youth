package com.youshi.zebra.admin.log.service;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.model.PageView;
import com.youshi.zebra.admin.log.constants.AdminLogType;
import com.youshi.zebra.admin.log.model.AdminLogModel;
import com.youshi.zebra.admin.log.model.AdminLogModel.AdminLogKeys;
import com.youshi.zebra.admin.log.utils.AdminLogUtils;

/**
 * 
 * @author wangsch
 * @date 2017年2月25日
 */
@Service
public class AdminLogService extends AbstractService<Long, AdminLogModel>{
	
	@Autowired
	private AdminLogUtils adminLogDAO;
	
	public PageView<AdminLogModel, HasUuid<Long>> getLog(Long from, Long to,
            Collection<AdminLogType> types, Integer adminId, String keyword,
            Long cursor, int limit) {
		
		WhereClause params = WhereClause.create();
		if(from != null) {
			params.and().gt(AdminLogKeys.create_time, from);
		}
		if(to != null) {
			params.and().lt(AdminLogKeys.create_time, to);
		}
		if(CollectionUtils.isNotEmpty(types)) {
			params.and().in(AdminLogKeys.type, types);
		}
		if(adminId != null) {
			params.and().in(AdminLogKeys.admin_id, adminId);
		}
		if(StringUtils.isNotEmpty(keyword)) {
			params.and().like(AdminLogKeys.data, keyword);
		}
		
		PageView<AdminLogModel, HasUuid<Long>> page = getByCursor(cursor, limit, params);
		
		return page;
	}
	
	@Override
	protected AbstractDAO<Long, AdminLogModel> dao() {
		return adminLogDAO;
	}
}
