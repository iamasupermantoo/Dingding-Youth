package com.youshi.zebra.filter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dorado.mvc.reqcontext.AppVer;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.dorado.mvc.reqcontext.Platform;

/**
 * 
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class RequestInfoInjectFilter extends OncePerRequestFilter {
    private static final int MAX_UA_LENGTH = 200;

    private final Pattern MAC_PATTERN = Pattern
            .compile("^([0-9a-fA-F]{2})(([:][0-9a-fA-F]{2}){5})$"); //^([0-9a-fA-F]{2})(([:][0-9a-fA-F]{2}){5})$

    private final Pattern DEVICE_ID_PATTERN = Pattern.compile("^([0-9a-fA-F]{32})$"); //^([0-9a-fA-F]{32})$

    @Override
    protected String getAlreadyFilteredAttributeName() {
        return this.getClass().getName();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        WebRequestContext.setRequestTimestamp(System.currentTimeMillis());
		AppVer appVer = AppVer.of(request.getParameter("appver"));
        if (appVer != null) {
            WebRequestContext.setAppVer(appVer);
        }
        
        String deviceMac = request.getHeader("Device-MAC");
        if ((deviceMac != null) && MAC_PATTERN.matcher(deviceMac).matches()) {
            WebRequestContext.setAndroidDeviceMac(deviceMac.toLowerCase());
        }
        String deviceId = request.getHeader("Device-Id");
        if ((deviceId != null) && DEVICE_ID_PATTERN.matcher(deviceId).matches()) {
            WebRequestContext.setAndroidDeviceId(deviceId.toLowerCase());
        }
        
        // user-agent 和 platform
        String userAgent = request.getHeader("User-Agent");
        if (StringUtils.isNotEmpty(userAgent)) {
            userAgent = StringUtils.substring(userAgent, 0, MAX_UA_LENGTH);
            WebRequestContext.setUserAgent(userAgent);
            if (userAgent.contains("iPhone")) {
            	WebRequestContext.setPlatform(Platform.IOS);
            } else if (userAgent.contains("Android")) {
            	WebRequestContext.setPlatform(Platform.Android);
            } else {
            	WebRequestContext.setPlatform(Platform.Unknown);
            }
        }
        String idfa = request.getHeader("IDFA");
        if (StringUtils.isNotEmpty(idfa)) {
            WebRequestContext.setIdfa(idfa);
        }

        filterChain.doFilter(request, response);
    }
}
