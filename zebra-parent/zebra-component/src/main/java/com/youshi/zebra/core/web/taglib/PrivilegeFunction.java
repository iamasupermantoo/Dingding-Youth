package com.youshi.zebra.core.web.taglib;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.dorado.mvc.reqcontext.WebRequestContext;
import com.youshi.zebra.admin.adminuser.constant.Privilege;
import com.youshi.zebra.admin.adminuser.constant.PrivilegeConstants;

/**
 * 管理员权限判断函数
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public class PrivilegeFunction {

    /**
     * 是否拥有指定的权限
     * 
     * @param requirePrivilege	权限字符串{@link Privilege}
     * @return					有权限返回true，否则返回fasle
     */
	@SuppressWarnings("unchecked")
    public static boolean hasPrivilege(String requirePrivilege) {
        Set<Privilege> privileges = (Set<Privilege>) WebRequestContext
                .getRequest().getAttribute(PrivilegeConstants.PRIVILEGES_SET_IN_REQUEST);
        if(CollectionUtils.isEmpty(privileges)) {
        	return false;
        }
        
        return privileges.contains(Privilege.Godlike) 
        		|| privileges.contains(Privilege.valueOf(requirePrivilege));
    }

}
