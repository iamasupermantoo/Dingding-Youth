package com.youshi.zebra.conroller.adminuser;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.mvc.reqcontext.WebRequestContext;
import com.youshi.zebra.conroller.LoginRequiredController;
import com.youshi.zebra.passport.service.UserPassportService;

/**
 * 修改密码
 * 
 * @author wangsch
 * @date 2016-09-12
 */
@Controller
public class PasswordController extends LoginRequiredController {

    private String VIEW = "user/changepassword";

//    @Autowired
//    private AdminPassportService adminPassportService;

    @Autowired
    private UserPassportService passportService;

    @RequestMapping(value = "/user/changepassword", method = RequestMethod.GET)
    public ModelAndView get() {

        return new ModelAndView(VIEW);
    }

    @RequestMapping(value = "/user/changepassword", method = RequestMethod.POST)
    public ModelAndView post(/*@RequestParam("password") String paassword,*/
    @RequestParam("newPassword") String newPassword) {

    	Integer userId = WebRequestContext.getUserId();

        ModelAndView mav = new ModelAndView(VIEW);
        String message = null;
        if (StringUtils.isEmpty(newPassword)) {
            message = "密码不能为空";
        } else {
            if (!isPasswordStrengthEnough(newPassword)) {
                message = "密码强度不够";
            } else {
//                adminPassportService.changePassword(userId, newPassword);

                // 去掉所有票
                passportService.removeTicket(userId);
                message = "修改成功，请重新登录";
            }
        }
        mav.addObject("message", message);

        return mav;
    }

    Pattern patternDigit = Pattern.compile("([0-9])");

    Pattern patternChar = Pattern.compile("([a-zA-Z])");

    private boolean isPasswordStrengthEnough(String password) {
        if (StringUtils.length(password) < 8) {
            return false;
        }
        return patternChar.matcher(password).find() && patternDigit.matcher(password).find();
    }
}
