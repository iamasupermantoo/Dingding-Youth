package com.youshi.zebra.admin.adminuser.service.impl;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youshi.zebra.admin.adminuser.constant.Privilege;
import com.youshi.zebra.admin.adminuser.dao.AdminPrivilegeDAO;
import com.youshi.zebra.admin.adminuser.model.AdminPrivilegeModel;
import com.youshi.zebra.admin.adminuser.service.PrivilegeService;


@Service
public class PrivilegeServiceImpl implements PrivilegeService {
	private Logger logger = LoggerFactory.getLogger(PrivilegeServiceImpl.class);
	
    @Autowired
    private AdminPrivilegeDAO adminPrivilegeDAO;
    
    @Override
    public Map<Privilege, AdminPrivilegeModel> getPrivileges(Integer userId) {
    	Map<Privilege, AdminPrivilegeModel> result = adminPrivilegeDAO.getAll(userId);
    	for(Privilege p : Privilege.values()) {
    		result.put(p, result.get(p));
    	}
    	
    	return result;
    }

    @Override
    public void updatePrivileges(int userId, Map<Privilege, Long> privileges) {
        int c = adminPrivilegeDAO.deleteAll(userId);
        int c2 = 0;
        if (MapUtils.isNotEmpty(privileges)) {
            c2 = adminPrivilegeDAO.insertAll(userId, privileges);
        }
        logger.info("Update privilege succ. delete: {}, add: {}", c, c2);
    }
}
