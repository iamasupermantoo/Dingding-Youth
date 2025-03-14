package com.youshi.zebra.admin.adminuser.service;

import java.util.List;

import com.dorado.framework.crud.service.RetrieveById;
import com.youshi.zebra.admin.adminuser.constant.AdminUserPassportStatus;
import com.youshi.zebra.admin.adminuser.exception.AdminUserLoginException;
import com.youshi.zebra.admin.adminuser.model.AdminUserPassport;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface AdminUserPassportService extends RetrieveById<Integer, AdminUserPassport> {
	List<AdminUserPassport> getAllAdminUsers();
	
	AdminUserPassport getByUsername(String adminUsername);

    void addPassport(int userId, String adminUsername, String rawPassword, String data);

    void changePassword(Integer userId, String newPassword);

    int updateStatus(int userId, AdminUserPassportStatus status);
    
    void updatePassport(int userId, String data);
    
    AdminUserPassport verifyPassport(String adminUsername, String rawPassword, 
    		String code, String sessionCode)  throws AdminUserLoginException;

    boolean verifyPassword(String rawPassword, String encodedPassword);

}
