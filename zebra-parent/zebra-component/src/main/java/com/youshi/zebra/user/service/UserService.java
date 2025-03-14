package com.youshi.zebra.user.service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.model.HasModelCache;
import com.dorado.framework.crud.service.RetrieveByCursor;
import com.dorado.framework.crud.service.RetrieveById;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.model.UserModel;
/**
 * 基本用户信息相关，“管理员”代表运营后台用户，“用户”代表普通用户
 * 
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface UserService extends RetrieveById<Integer, UserModel>, 
	RetrieveByCursor<Integer, UserModel, WhereClause>, HasModelCache<Integer> {
    
    /**
     * 是否正常用户
     * @param user_id
     * @return
     */
    public boolean isUserStatusNormal(Integer userId);

	/**
	 * @param userId
	 * @param name
	 * @param signature
	 * @param data
	 */
	public void update(Integer userId, String name, String signature, String data);
	
	public UserModel expectUserType(Integer userId, UserType expectedUserType);
    
	public UserModel unexpectUserType(Integer userId, UserType unexpectUserType);
}
