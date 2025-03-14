package com.youshi.zebra.wx.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dorado.mvc.utils.ResponseUtils;

/**
 * 
 * @author wangsch
 * @date 2017年5月5日
 */
@Controller
@RequestMapping(value = "/common")
public class CommonController {
	
	@RequestMapping(value = "/500", method=RequestMethod.GET)
	public void _500(HttpServletResponse resp, 
			@RequestParam(value = "msg", defaultValue="未知错误") String msg
			) {
		ResponseUtils.output(resp, "服务器出错了，信息：" + msg);
	}
}
