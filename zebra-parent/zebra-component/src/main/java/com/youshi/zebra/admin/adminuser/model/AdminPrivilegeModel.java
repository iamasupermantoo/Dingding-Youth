package com.youshi.zebra.admin.adminuser.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.youshi.zebra.admin.adminuser.constant.Privilege;
import com.youshi.zebra.core.utils.DateTimeUtils;

/**
 * 
 * 后台用户权限，用户id和权限的对应关系
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public class AdminPrivilegeModel {
	private int id;
	
    private final int userId;

    private final Privilege privilege;

    private long expireTime;

    public AdminPrivilegeModel(int id, int userId, Privilege privilege, long expireTime) {
    	this.id = id;
        this.userId = userId;
        this.privilege = privilege;
        this.expireTime = expireTime;
    }
    
    public int getId() {
		return id;
	}
    
    public int getUserId() {
        return userId;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public long getExpireTime() {
    	return expireTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
