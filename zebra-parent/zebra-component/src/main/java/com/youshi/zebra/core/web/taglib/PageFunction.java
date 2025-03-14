package com.youshi.zebra.core.web.taglib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageModel;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.reqcontext.WebRequestContext;

/**
 * 分页控制
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public class PageFunction {
    /**
     * 分页查询时，跳转到首页。获取首页url
     * 
     * @return	首页地址
     */
    public static String firstPage() {
        HttpServletRequest request = WebRequestContext.getRequest();
        
        StringBuilder sb = new StringBuilder(request.getRequestURI());
        // remove nextCursor param
        Map<String, String[]> params = request.getParameterMap();
        
		// 拼接请求参数
		sb.append('?');
        for (Entry<String, String[]> entry : params.entrySet()) {
        	if(entry.getKey().equals("cursor")) {
        		continue;
        	}
            for (String value : entry.getValue()) {
                String encoded = null;
				try {
					encoded = URLEncoder.encode(value, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// here should impossible
				}
				sb.append(entry.getKey()).append('=')
                	.append(encoded)
                	.append('&');
            }
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 分页查询时，跳转到下一页。获取下一页url
     * 
     * @return	下一页地址，或者为null
     */
    @SuppressWarnings("rawtypes")
    public static <E, K> String nextPage(PageView<E, K> rv) {
    	if(rv == null) {
    		return null;
    	}
        HttpServletRequest request = WebRequestContext.getRequest();
        if (request == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(request.getRequestURI());
        K k = rv.getNextCursor();
        if (k == null) {
            return null;
        }
        String nextCursor;
        if (k instanceof HasUuid) {
            nextCursor = ((HasUuid) k).getUuid();
        } else {
            nextCursor = k.toString();
        }
        sb.append("?cursor=").append(nextCursor);

        Map<String, String[]> params = request.getParameterMap();
		for (Entry<String, String[]> entry : params.entrySet()) {
			if(entry.getKey().equals("cursor")) {
        		continue;
        	}
			for (String value : entry.getValue()) {
                String encode = null;
				try {
					encode = URLEncoder.encode(value, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// here should impossible
				}
				sb.append('&').append(entry.getKey()).append('=').append(encode);
            }
        }
        return sb.substring(0, sb.length());
    }
    
    public static <E, K> String toPage(PageModel<E> rv, int page) {
    	if(rv == null) {
    		return null;
    	}
        HttpServletRequest request = WebRequestContext.getRequest();
        if (request == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(request.getRequestURI());
        sb.append("?page=").append(page);
        
        Map<String, String[]> params = request.getParameterMap();
		for (Entry<String, String[]> entry : params.entrySet()) {
			if(entry.getKey().equals("page")) {
        		continue;
        	}
			for (String value : entry.getValue()) {
                String encode = null;
				try {
					encode = URLEncoder.encode(value, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// here should impossible
				}
				sb.append('&').append(entry.getKey()).append('=').append(encode);
            }
        }
        return sb.substring(0, sb.length());
    }
}
