package com.youshi.zebra.admin.adminuser.service;

import java.util.Map;

import com.youshi.zebra.admin.adminuser.constant.Privilege;
import com.youshi.zebra.admin.adminuser.model.AdminPrivilegeModel;

/**
 * 
 * 后台管理员权限管理Service
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public interface PrivilegeService {

    /**
     * 取一个用户拥有的权限
     * 
     * @param userId	用户id
     * @return			Map权限集合
     * 					k: {@link Privilege}
     * 					v: {@link AdminPrivilegeModel}
     */
    Map<Privilege, AdminPrivilegeModel> getPrivileges(Integer userId);

    /**
     * 修改用户的权限，删除用户所有权限，替换为{@code roles}参数制定的权限
     * 
     * @param userId		用户id
     * @param privileges 	权限和有效时间，传空则会抹掉所有权限
     */
    void updatePrivileges(int userId, Map<Privilege, Long> privileges);
}
