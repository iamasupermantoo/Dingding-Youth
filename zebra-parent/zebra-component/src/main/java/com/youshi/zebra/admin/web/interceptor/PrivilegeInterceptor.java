package com.youshi.zebra.admin.web.interceptor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dorado.mvc.interceptors.MethodInterceptorUtils;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.youshi.zebra.admin.adminuser.annotation.PrivilegeRequired;
import com.youshi.zebra.admin.adminuser.constant.Privilege;
import com.youshi.zebra.admin.adminuser.constant.PrivilegeConstants;
import com.youshi.zebra.admin.adminuser.model.AdminPrivilegeModel;
import com.youshi.zebra.admin.adminuser.service.PrivilegeService;
import com.youshi.zebra.core.web.interceptor.TicketInterceptor;
import com.youshi.zebra.user.service.UserService;

/**
 * 对Controller方法的访问，进行权限判断的拦截器。
 * 
 * Note:
 * 这个拦截器，在拦截器链中，springmvc配置文件，需要配置在 ( {@link TicketInterceptor} ) 之后。
 * 
 * @author wangsch
 * @date 2016-09-12
 * 
 * @see PrivilegeRequired
 * @see Privilege
 * 
 */
public class PrivilegeInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private UserService userService;

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {

        PrivilegeRequired anno = MethodInterceptorUtils.getAnnotaion(handler,
                PrivilegeRequired.class);
        if (anno == null) {
        	return true;
        }
        
        Integer userId = WebRequestContext.getUserId();
        if (!userService.isUserStatusNormal(userId)) {
        	logger.error("User status NOT NORMAL, so FORBIDDEN! userId: {}", userId);
        	response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        	return false;
        }
        
        Map<Privilege, AdminPrivilegeModel> config = privilegeService.getPrivileges(userId);
        Set<Privilege> valid = new HashSet<>();
        for (AdminPrivilegeModel value : config.values()) {
            if (value.getExpireTime() > System.currentTimeMillis()) {
                valid.add(value.getPrivilege());
            }
        }
        request.setAttribute(PrivilegeConstants.PRIVILEGES_SET_IN_REQUEST, valid);

        Privilege[] require = anno.value();
		boolean allow = allow(require, valid);
        if(!allow) {
        	logger.warn("User privilege NOT ALLOWED. aid: {}, required: {}", userId, require);
        }
		return allow;
    }

    /**
     * 要做到修改权限后实时生效，必须每次请求，填充可用权限集合，视图渲染时从request域获取权限，在jsp页面或者其他地方使用，
     * 请求结束后移除request中的权限
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        request.removeAttribute(PrivilegeConstants.PRIVILEGES_SET_IN_REQUEST);
    }

    private boolean allow(Privilege[] requiredRoles, Set<Privilege> userRoles) {
        if (userRoles.contains(Privilege.Godlike)) {
            return true;
        }

        for (Privilege type : requiredRoles) {
            if (userRoles.contains(type)) {
                return true;
            }
        }

        return false;
    }
}
