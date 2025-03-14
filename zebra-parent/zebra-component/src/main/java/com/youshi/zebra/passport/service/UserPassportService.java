package com.youshi.zebra.passport.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.dorado.framework.tuple.TwoTuple;

/**
 * 
 * 用户通行证，账号管理
 * 
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface UserPassportService {
	// ----------------------------------------------mobile passport 相关----------------------------------------------
    Integer verifyAccount(String mobile, String password);

    boolean isRegisterd(String mobile);

    void createAccount(int userId, String mobile, String password);

    public void resetPassword(String mobile, String password, String code);
    
    public void changePassword(int userId, String oldPassword, String newPassword);

    Map<String, Integer> getRegisteredUserIds(Collection<String> mobiles);

    default Integer getRegisteredUserId(String mobile) {
        return getRegisteredUserIds(Collections.singleton(mobile)).get(mobile);
    }

    // ----------------------------------------------------ticket 相关----------------------------------------------------
    String createTicketWithRand(int userId);
    
    TwoTuple<Integer, Integer> verifyTicketWithRand(String ticket);

    void removeTicket(int userId);
    
    void removeSecretRandom(int userId, int rand);

}
