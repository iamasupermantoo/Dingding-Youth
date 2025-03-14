package com.youshi.zebra.conroller.adminuser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.utils.CookieUtils;
import com.youshi.zebra.conroller.LoginRequiredController;
import com.youshi.zebra.passport.constant.PassportConstants;
import com.youshi.zebra.passport.service.UserPassportService;

/**
 * 
 * @author wangsch
 * @date 2016-09-12
 */
@RequestMapping(value = "/logout")
@Controller
public class LogoutController extends LoginRequiredController {
	@Autowired
    private UserPassportService userPassportService;

    @RequestMapping(value = "", method=RequestMethod.POST)
    @ResponseBody
    public Object logout(
    		@Visitor Integer userId,
    		HttpServletRequest request, HttpServletResponse response) {
    	
    	userPassportService.removeTicket(userId);
        removeCookieTicket(request, response);
        return JsonResultView.SUCCESS;
    }

	/**
	 * @param request
	 * @param response
	 */
	private void removeCookieTicket(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.clearCookie(response, PassportConstants.TICKET_NAME, request.getServerName(), "/");
	}
}
