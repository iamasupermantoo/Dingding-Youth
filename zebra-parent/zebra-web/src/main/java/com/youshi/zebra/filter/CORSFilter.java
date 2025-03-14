//package com.youshi.zebra.filter;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.web.filter.OncePerRequestFilter;
//
///**
// * 
// * @author wangsch
// * @date 2016-09-12
// */
//public class CORSFilter extends OncePerRequestFilter {
//	// FIXME danger
//	private String accessControlAllowOrigin = "*";
//	
//	private String accessControlAllowHeaders = "Content-Type,Accept";
//	private String accessControlAllowMethods = "GET,POST,OPTIONS";
//	private String accessControlAllowCredentials = "true";
//	
//	
//	
//    @Override
//    protected String getAlreadyFilteredAttributeName() {
//        return this.getClass().getName();
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//            FilterChain filterChain) throws ServletException, IOException {
//    	System.out.println("accessControlAllowOrigin: " + accessControlAllowOrigin);
//    	
//    	
//    	response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrigin);
//		response.addHeader("Access-Control-Allow-Headers", accessControlAllowHeaders);
//		response.addHeader("Access-Control-Allow-Methods", accessControlAllowMethods);
//		response.addHeader("Access-Control-Allow-Credentials", accessControlAllowCredentials);
//    	
//        filterChain.doFilter(request, response);
//    }
//    
////    public void setAccessControlAllowOrigin(String accessControlAllowOrigin) {
////		this.accessControlAllowOrigin = accessControlAllowOrigin;
////	}
//	
//	public void setAccessControlAllowHeaders(String accessControlAllowHeaders) {
//		this.accessControlAllowHeaders = accessControlAllowHeaders;
//	}
//	
//	public void setAccessControlAllowMethods(String accessControlAllowMethods) {
//		this.accessControlAllowMethods = accessControlAllowMethods;
//	}
//	
//	public void setAccessControlAllowCredentials(String accessControlAllowCredentials) {
//		this.accessControlAllowCredentials = accessControlAllowCredentials;
//	}
//}
