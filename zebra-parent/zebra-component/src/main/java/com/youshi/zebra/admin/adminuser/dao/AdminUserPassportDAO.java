package com.youshi.zebra.admin.adminuser.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.youshi.zebra.admin.adminuser.constant.AdminUserPassportStatus;
import com.youshi.zebra.admin.adminuser.model.AdminUserPassport;

/**
 * @author wangsch
 * 
 * @date 2016-09-12
 */
public interface AdminUserPassportDAO {
    Map<Integer, AdminUserPassport> getByIds(Collection<Integer> userIds);

    List<AdminUserPassport> getAll();
 
    AdminUserPassport getByUsername(String username);
 
    int updateStatus(int userId, AdminUserPassportStatus status);

    int updatePassword(int id, String password);
    
    int updateData(int id, String data, long updateTime);

    int insert(int id, String username, String newRawPassword, String data, long createTime);
}
