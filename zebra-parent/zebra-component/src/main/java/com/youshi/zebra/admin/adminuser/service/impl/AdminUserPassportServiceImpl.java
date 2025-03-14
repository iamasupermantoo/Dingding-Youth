package com.youshi.zebra.admin.adminuser.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youshi.zebra.admin.adminuser.constant.AdminUserPassportStatus;
import com.youshi.zebra.admin.adminuser.dao.AdminUserPassportDAO;
import com.youshi.zebra.admin.adminuser.exception.AdminUserLoginException;
import com.youshi.zebra.admin.adminuser.model.AdminUserPassport;
import com.youshi.zebra.admin.adminuser.service.AdminUserPassportService;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.exception.base.DoradoRuntimeException;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;

/**
 * @author wangsch
 * 
 * @date 2016-09-12
 */
@Service
public class AdminUserPassportServiceImpl implements AdminUserPassportService {
	private static final Logger logger = LoggerFactory.getLogger(AdminUserPassportServiceImpl.class);
	
    @Autowired
    private AdminUserPassportDAO adminUserPassportDAO;
    
    @Autowired
    private UserService userService;

    @Override
    public void addPassport(int userId, String username, String rawPassword,
            String data) {
        AdminUserPassport passport = adminUserPassportDAO.getByUsername(username);
        if (passport != null) {
            throw new DoradoRuntimeException();
        }
        adminUserPassportDAO.insert(userId, username, encodeMd5Password(rawPassword),
        		data, System.currentTimeMillis());
    }

    @Override
    public void changePassword(Integer userId, String newPassword) {
        AdminUserPassport userPassport = getById(userId);
        if (userPassport == null) {
            return;
        }
        adminUserPassportDAO.updatePassword(userId, encodeMd5Password(newPassword));
    }

    @Override
    public List<AdminUserPassport> getAllAdminUsers() {
        return adminUserPassportDAO.getAll();
    }

    @Override
    public AdminUserPassport getByUsername(String adminUsername) {
        return adminUserPassportDAO.getByUsername(adminUsername);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return StringUtils.equals(encodeMd5Password(rawPassword), encodedPassword);
    }

    @Override
    public int updateStatus(int userId, AdminUserPassportStatus status) {
        int c = adminUserPassportDAO.updateStatus(userId, status);
        DAOUtils.checkAffectRows(c);
		return c;
    }

    @Override
    public Map<Integer, AdminUserPassport> getByIds(Collection<Integer> ids) {
        return adminUserPassportDAO.getByIds(ids);
    }
    
    // ---------------------------------- private methods ----------------------------------
    public static final String encodeMd5Password(String rawPassword) {
        String result = DigestUtils.md5Hex(rawPassword + rawPassword.substring(0, 2));
        return result;
    }

	@Override
	public AdminUserPassport verifyPassport(String adminUsername, String rawPassword, 
			String code, String sessionCode) throws AdminUserLoginException {
		// 验证码
		if(!code.equalsIgnoreCase(sessionCode)) {
    		logger.error("Picture code verify FAIL. username: {}, code: {}, sessionCode: {}", 
    				adminUsername, code, sessionCode);
    		throw new AdminUserLoginException("验证码不正确，请重新输入");
    	}
		
		// passport
		AdminUserPassport passport = getByUsername(adminUsername);
		if(passport == null) {
			logger.error("Admin user NOT FOUND. username: {}", adminUsername);
			throw new AdminUserLoginException("用户名或密码不正确");
		}
		
		// 状态
		UserModel user = userService.getById(passport.getId());
		Integer userId = user.getId();
        UserType userType = UserType.fromValue(user.getType());
		if (UserStatus.isNotNormal(user) || userType != UserType.Admin) {
        	logger.error("Admin user NOT VALID. userId: {}, username: {}, userType: {}", 
        			userId, adminUsername, userType);
        	throw new AdminUserLoginException("用户状态有异常");
        }
		
		// 密码
		boolean verifyOK = verifyPassword(rawPassword, passport.getPassword());
		if(verifyOK) {
			logger.info("Admin user login succ. userId: {}, username: {}", userId, adminUsername);
		} else {
			logger.error("Password incorrect. userId: {}, username: {}", 
    				userId, adminUsername);
			throw new AdminUserLoginException("用户名或密码不正确");
		}
		
		return passport;
	}

	@Override
	public void updatePassport(int userId, String data) {
		int c = adminUserPassportDAO.updateData(userId, data, System.currentTimeMillis());
		DAOUtils.checkAffectRows(c);
	}
	
	public static void main(String[] args) {
		System.out.println(encodeMd5Password("ddLad_adm@2017"));
	}

}
