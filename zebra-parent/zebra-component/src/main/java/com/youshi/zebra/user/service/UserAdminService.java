package com.youshi.zebra.user.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.passport.service.UserPassportService;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.dao.UserDAO;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.model.UserModel.UserKey;

/**
 * 用户管理Service，提供一些对于用户的通用操作，不具体区分{@link UserType}
 * 
 * @author wangsch
 * @date 2017年1月11日
 */
@Service
public class UserAdminService {
	private static final Logger logger = LoggerFactory.getLogger(UserAdminService.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserPassportService passportService;
	
	@Autowired
	private UserDAO userDAO;
    
	public PageView<UserModel, HasUuid<Integer>> query(Integer userId, UserStatus status, UserType userType, 
			Integer cursor, Integer limit) {
		WhereClause where = WhereClause.create();
		if(status != null) {
			where.and().eq(UserKey.status.name(), status);
		}
		if(userId != null) {
			where.and().eq(UserKey.id.name(), userId);
		}
		if(userType != null) {
			where.and().eq(UserKey.type.name(), userType.getValue());
		}
		
		PageView<UserModel, HasUuid<Integer>> result = userService.getByCursor(cursor, limit, where);
		
		return result;
	}
	/**
	 * 封禁用户，用户有两种状态，{@link UserStatus#UserBlocked}表示被封禁，
	 * {@link UserStatus#Normal}表示正常
	 * 
	 * @param userId	用户id
	 */
	public void block(Integer adminId, Integer userId) {
		userService.clearCache(userId);
		
		// 状态
		int c = userDAO.setStatus(userId, UserStatus.UserBlocked);
		DAOUtils.checkAffectRows(c);
		
		// 踢下线
		passportService.removeTicket(userId);
		logger.info("User BLOCKED. adminId: {}, uid: {}",adminId, userId);
	}
    
	/**
	 * 解除封禁
	 * @param userId	用户id
	 */
	public void unblock(Integer adminId, Integer userId) {
		userService.clearCache(userId);
		
		// 状态
		int c = userDAO.setStatus(userId, UserStatus.Normal);
		DAOUtils.checkAffectRows(c);
		logger.info("User UNBLOCKED. adminId: {}, uid: {}",adminId, userId);
	}
}