package com.youshi.zebra.admin.log.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.youshi.zebra.admin.log.constants.AdminLogType;

/**
 * 
 * 管理员操作日志
 * 
 * @author wangsch
 *
 * @date 2016-09-12
 */
public final class AdminLogModel extends AbstractModel<Long> {
	public enum AdminLogKeys {
		admin_id,
		ip,
		type,
		data,
		create_time,
	}
	
    private final int adminId;

    private final AdminLogType type;

    private final long ip;

    public AdminLogModel(long id, int adminId, AdminLogType type, String data,
            long createTime, long ip) {
        this.id = id;
        this.adminId = adminId;
        this.type = type;
        this.data = data;
        this.createTime = createTime;
        this.ip = ip;
    }
    
    public int getAdminId() {
		return adminId;
	}

    public AdminLogType getType() {
        return type;
    }

    public String getIp() {
        return WebRequestContext.longToIpv4(ip);
    }

}
