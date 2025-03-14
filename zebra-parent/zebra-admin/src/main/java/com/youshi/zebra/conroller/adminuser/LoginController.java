package com.youshi.zebra.conroller.adminuser;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.patchca.service.Captcha;
import org.patchca.service.CaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.threshold.ThresholdUtils;
import com.dorado.mvc.interceptors.annotation.NoCache;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.youshi.zebra.admin.adminuser.exception.AdminUserLoginException;
import com.youshi.zebra.admin.adminuser.model.AdminUserPassport;
import com.youshi.zebra.admin.adminuser.service.AdminUserPassportService;
import com.youshi.zebra.admin.log.annotation.AdminLog;
import com.youshi.zebra.admin.log.constants.AdminLogType;
import com.youshi.zebra.core.constants.ZebraThreshold;
import com.youshi.zebra.passport.utils.LoginUtil;
import com.youshi.zebra.utils.CaptchaServiceFactory;

/**
 * 
 * 管理员登陆
 * 
 * @author wangsch
 * @date 2016-09-12
 */
@Controller
public class LoginController {

    private static final int TICKET_EXPIRE = (int) TimeUnit.DAYS.toSeconds(25);

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String LOGIN_ERROR_TIPS = "errorTips";

	private static final String SESSION_PIC_CODE_KEY = "pcode";

    @Autowired
    private AdminUserPassportService adminUserPassportService;

    @Autowired
    private LoginUtil loginUtil;
    
    private static CaptchaService captchaService = CaptchaServiceFactory.create();
    
    /**
     * 获取图片验证码
     */
    @RequestMapping(value = "/login/picture", method=RequestMethod.GET)
    @NoCache
    public void getCode(
    		HttpServletRequest req,
    		HttpServletResponse resp
    		) {
		Captcha captcha = captchaService.getCaptcha();
		setPictureCode(req, captcha);
		outputPictureCode(resp, captcha);
    }
    
    /**
     * 未登录跳转登录页，已登录跳转到主页
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView toLogin(HttpServletResponse response) {
    	Integer userId = WebRequestContext.getUserId();
        if (userId != null) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView modelAndView = new ModelAndView("/zebra-login");
        return modelAndView;
    }
    
    /**
     * 执行登录逻辑
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @AdminLog(value = AdminLogType.AdminLogin)
    public ModelAndView doLogin(
    		@RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("code") String code,
            HttpServletRequest request, HttpServletResponse response) {
    	Integer userId = WebRequestContext.getUserId();
    	if (userId != null) {
    		return new ModelAndView("redirect:/");
    	}
    	
    	if(!allowLogin(username)) {
    		logger.warn("Danger, login threshold reached. username: {}", username);
            return toLogin(response).addObject(LOGIN_ERROR_TIPS, "登陆失败！");
    	}
        
        // 验证
        HttpSession session = request.getSession();
        String sessionCode = (String)session.getAttribute(SESSION_PIC_CODE_KEY);
        AdminUserPassport passport;
		try {
			passport = adminUserPassportService.verifyPassport(username, password, 
					code, sessionCode);
		} catch (AdminUserLoginException e) {
			return toLogin(response).addObject(LOGIN_ERROR_TIPS, e.getMessage());
		}
        
        loginUtil.setTicket(request, response, passport.getId(), TICKET_EXPIRE, false);
        session.invalidate();
        WebRequestContext.setUserId(passport.getId());
        
        return new ModelAndView("redirect:/");
    }

    // ------------------------------------- private methods -------------------------------------
	private void setPictureCode(HttpServletRequest req, Captcha captcha) {
		HttpSession session = req.getSession(false);
		if (session == null) {
			session = req.getSession();
		}
		session.setAttribute(SESSION_PIC_CODE_KEY, captcha.getChallenge());
	}

	private void outputPictureCode(HttpServletResponse resp, Captcha captcha) {
		try {
			resp.setContentType("image/png");
			ServletOutputStream outputStream = resp.getOutputStream();
			BufferedImage bufferedImage = captcha.getImage();
			ImageIO.write(bufferedImage, "png", outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			logger.error("Fail output picture code.", e);
		}
	}

	
	private boolean allowLogin(String username) {
		return !ThresholdUtils.isReached(ZebraThreshold.ADMIN_LOGIN, username) 
				&& !ThresholdUtils.isReached(ZebraThreshold.ADMIN_LOGIN_IP, 
						WebRequestContext.getCurrentIpInLong());
	}
	
}
