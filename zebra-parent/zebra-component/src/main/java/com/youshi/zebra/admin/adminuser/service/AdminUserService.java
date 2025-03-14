package com.youshi.zebra.admin.adminuser.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.model.HasData;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.admin.adminuser.constant.AdminUserKeys;
import com.youshi.zebra.admin.adminuser.constant.AdminUserPassportStatus;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.dao.UserDAO;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.model.UserModel.UserKey;
import com.youshi.zebra.user.service.UserAdminService;
import com.youshi.zebra.user.service.UserService;

/**
 * 
 * 后台用户管理Service
 * 
 * Note:请区分{@link AdminUserService}和{@link UserAdminService}两者的不同，前者是用于管理后台用户，后者是用来管理所有用户。<br />
 * 当然，后台用户是用户的一种，{@link UserType}
 * 
 * @author wangsch
 * @date 2017年2月24日
 */
@Service
public class AdminUserService {
	private Logger logger = LoggerFactory.getLogger(AdminUserService.class);
	
	@Autowired
	private AdminUserPassportService adminUserPassportService;
	
	@Autowired
	private UserAdminService userAdminService;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserService userService;
	
	public PageView<UserModel, HasUuid<Integer>> getAdminUsers(Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create()
			.and().eq(UserKey.type, UserType.Admin.getValue());
		PageView<UserModel, HasUuid<Integer>> page = userService.getByCursor(cursor, limit, params);
		
		return page;
	}
	
	
	public int addAdminUser(String name, String username, String password, String email, String mobile, String remark) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(AdminUserKeys.name.name(), name);
		dataMap.put(AdminUserKeys.email.name(), email);
		dataMap.put(AdminUserKeys.mobile.name(), mobile);
		dataMap.put(AdminUserKeys.remark.name(), remark);
		dataMap.put(AdminUserKeys.username.name(), username);
		String data = DoradoMapperUtils.toJSON(dataMap);
		
		int adminId = userDAO.insert(name, UserType.Admin, null, 
				data, UserStatus.Normal, System.currentTimeMillis());
		
		adminUserPassportService.addPassport(adminId, username, password, HasData.EMPTY_DATA);
		logger.info("Admin user created. adminId: {}", adminId);
		return adminId;
	}
	
	public void updateAdminUser(Integer adminId, String name, String email, String mobile, String remark) {
		UserModel user = userService.getById(adminId);
		
		Map<String, Object> dataMap = user.resolvedData();
		dataMap.put(AdminUserKeys.name.name(), name);
		dataMap.put(AdminUserKeys.email.name(), email);
		dataMap.put(AdminUserKeys.mobile.name(), mobile);
		dataMap.put(AdminUserKeys.remark.name(), remark);
		String data = DoradoMapperUtils.toJSON(dataMap);
		
		userService.update(adminId, name, null, data);
		adminUserPassportService.updatePassport(adminId, HasData.EMPTY_DATA);
		
		logger.info("Admin user updated. adminId: {}", adminId);
	}
	
	public void blockAdminUser(Integer loginAdminId, Integer userId) {
		userService.expectUserType(userId, UserType.Admin);
		
		adminUserPassportService.updateStatus(userId, AdminUserPassportStatus.Blocked);
        userAdminService.block(loginAdminId, userId);
        
        logger.info("Admin user blocked. loginAdminId: {}, adminId: {}", loginAdminId, userId);
	}

	/**
	 * @param loginAdminId
	 * @param userId
	 */
	public void unblockAdminUser(Integer loginAdminId, Integer userId) {
		userService.expectUserType(userId, UserType.Admin);
		
		adminUserPassportService.updateStatus(userId, AdminUserPassportStatus.Normal);
        userAdminService.unblock(loginAdminId, userId);
        
        logger.info("Admin user unblocked. loginAdminId: {}, adminId: {}", loginAdminId, userId);
	}
}
