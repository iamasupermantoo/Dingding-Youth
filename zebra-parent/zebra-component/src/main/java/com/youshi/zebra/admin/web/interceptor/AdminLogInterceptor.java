package com.youshi.zebra.admin.web.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dorado.mvc.interceptors.MethodInterceptorUtils;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.youshi.zebra.admin.log.annotation.AdminLog;
import com.youshi.zebra.admin.log.utils.AdminLogUtils;

/**
 * 
 * 管理员操作日志，拦截器。配合使用{@link AdminLog}注解，完成操作日志记录
 * 
 * @author wangsch
 * @date 2016-09-12
 * 
 * @see AdminLog
 * 
 */
@Component
public class AdminLogInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AdminLogUtils adminLogUtils;
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) throws Exception {
        Object ignore = RequestContextHolder.currentRequestAttributes().getAttribute(
                AdminLogUtils.IGNORE_LOG_SIGN, RequestAttributes.SCOPE_REQUEST);
        if (ignore != null) {
            return;
        }
        if (response.getStatus() != HttpServletResponse.SC_OK) {
        	return;
        }
        
        String uri = request.getRequestURI();
        if (StringUtils.isNotEmpty(request.getQueryString())) {
        	uri += "?" + request.getQueryString();
        }
        
        AdminLog anno = MethodInterceptorUtils.getAnnotaion(handler, AdminLog.class);
        if (anno != null) {
        	Map<String, String[]> requestParams = request.getParameterMap();
        	boolean recordUri = anno.recordUri();
			Map<String, Object> data = params(uri, requestParams, recordUri);
        	adminLogUtils.record(WebRequestContext.getUserId(), 
        			anno.value(), WebRequestContext.getCurrentIpInLong(), data);
        }
    }

	private Map<String, Object> params(String uri, Map<String, String[]> requestParams, boolean recordUri) {
		Map<String, Object> transParameterMap = requestParams.entrySet()
				.stream()
				.filter(e -> ArrayUtils.isNotEmpty(e.getValue()))
				.collect(Collectors.toMap(Entry::getKey, entry -> {
					if (entry.getValue().length == 1) {
						return entry.getValue()[0];
					} else {
						return entry.getValue();
					}
				}));
		Map<String, Object> data = new HashMap<String, Object>(transParameterMap);
		if (recordUri) {
			data.put("uri", new String[] { uri });
		}
		if (AdminLogUtils.getAdminLogParams() != null) {
			data.putAll(AdminLogUtils.getAdminLogParams());
		}
		return data;
	}

}
