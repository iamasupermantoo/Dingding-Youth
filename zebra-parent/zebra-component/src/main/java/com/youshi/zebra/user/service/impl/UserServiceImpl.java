package com.youshi.zebra.user.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.cache.RequestContextCache;
import com.dorado.framework.cache.access.JsonRedisCacheAccess;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.jedis.JedisByZooKeeper;
import com.dorado.framework.utils.RetrieveIdUtils.IMultiDataAccess;
import com.youshi.zebra.core.constants.jedis.CachedRedisKey;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.exception.common.ForbiddenException;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.dao.UserDAO;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;

/**
 * 用户管理服务类
* 
* Date: Jun 6, 2016
* 
 * @author wangsch
 *
 */
@Service
public class UserServiceImpl extends AbstractService<Integer, UserModel> 
	implements UserService {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
    
    @Autowired
    private UserDAO userDAO;
    
    private RequestContextCache<Integer, UserModel> cache = new RequestContextCache<>();
    
    private JsonRedisCacheAccess<Integer, UserModel> redisCache = new JsonRedisCacheAccess<>(
            JedisByZooKeeper.of("cache"), (id) -> CachedRedisKey.User.of(id), UserModel::new);
    
    @Override
    public boolean isUserStatusNormal(Integer userId) {
    	UserModel user = getById(userId);
        if (user == null) {
            return false;
        }
        return UserStatus.isNormal(user);
    }

	@Override
	public void update(Integer userId, String name, String signature, String data) {
		int c = userDAO.update(userId, name, signature, data, false);
		DAOUtils.checkAffectRows(c);
		
		clearCache(Collections.singletonList(userId));
		
	}

	/**
	 * 
	 * 检查用户类型，期望用户实际类型和{@code expectedUserType}相同，如果不相同则抛出异常。
	 * 
	 * @param userId				用户id
	 * @param expectedUserType		期望的用户类型{@link UserType}
	 * @throws ForbiddenException	用户的实际类型和期望不相同时
	 * 
	 * @return 						成功返回{@link UserModel}
	 */
	public UserModel expectUserType(Integer userId, UserType expectedUserType) {
		UserModel user = getById(userId);
		UserType type = UserType.fromValue(user.getType());
		if(type != expectedUserType) {
			logger.error("Check FAIL. uid: {}, userType: {}, expectedUserType: {}", 
					userId, type, expectedUserType);
			throw new ForbiddenException();
		}
		
		return user;
	}
	
	/**
	 * 检查用户类型，期望用户实际类型和{@code unexpectUserType}不相同，如果相同则抛出异常。
	 * 
	 * @param userId				用户id
	 * @param unexpectUserType		不期望的用户类型{@link UserType}
	 * @throws ForbiddenException	用户的实际类型和期望相同时
	 * @return						成功返回{@link UserModel}
	 */
	public UserModel unexpectUserType(Integer userId, UserType unexpectUserType) {
		UserModel user = getById(userId);
		UserType type = UserType.fromValue(user.getType());
		if(type == unexpectUserType) {
			logger.error("Check FAIL. uid: {}, userType: {}, unexpectUserType: {}", 
					userId, type, unexpectUserType);
			throw new ForbiddenException();
		}
		
		return user;
	}
	
	@Override
	protected AbstractDAO<Integer, UserModel> dao() {
		return userDAO;
	}
	
	@Override
	protected List<IMultiDataAccess<Integer, UserModel>> multiDAList() {
		return Arrays.asList(cache, redisCache, userDAO);
	}
	
}
