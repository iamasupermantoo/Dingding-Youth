package com.youshi.zebra.admin.adminuser.dao;

import java.util.Map;

import com.youshi.zebra.admin.adminuser.constant.Privilege;
import com.youshi.zebra.admin.adminuser.model.AdminPrivilegeModel;

/**
 * 
 * 后台管理员权限管理DAO
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public interface AdminPrivilegeDAO {

    public Map<Privilege, AdminPrivilegeModel> getAll(Integer userId);

    public int deleteAll(Integer userId);

    public int insertAll(Integer userId, Map<Privilege, Long> roles);
}
