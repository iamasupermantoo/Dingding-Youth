package com.youshi.zebra.conroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.youshi.zebra.admin.adminuser.model.AdminUserPassport;
import com.youshi.zebra.admin.adminuser.service.AdminUserPassportService;

/**
 * 
 * 后台，首页
 * 
 * @author wangsch
 * @date 2016-09-12
 */
@Controller
public class HomeController extends LoginRequiredController {
	
	@Autowired
	private AdminUserPassportService adminUserPassportService;
	
	
    @RequestMapping(value = "/", method=RequestMethod.GET)
    public ModelAndView index(
    		@Visitor Integer userId
    		) {
    	AdminUserPassport passport = adminUserPassportService.getById(userId);
    	return new ModelAndView("index").addObject("passport", passport);
    }
    
    @RequestMapping(value = "/dashboard", method=RequestMethod.GET)
    public ModelAndView dashboard() {
    	return new ModelAndView("dashboard");
    }
}
