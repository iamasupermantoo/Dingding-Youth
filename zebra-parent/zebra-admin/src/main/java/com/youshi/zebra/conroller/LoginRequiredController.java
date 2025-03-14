package com.youshi.zebra.conroller;

import org.springframework.web.servlet.ModelAndView;

import com.youshi.zebra.core.web.annotation.LoginRequired;

/**
 * 
 * @author wangsch
 * @date 2016-09-12
 */
@LoginRequired(loginUrl = "/login", apiMode = false)
public abstract class LoginRequiredController {
    protected final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
    
    // FIXME
    protected ModelAndView redirect(String uri, String tips) {
        return new ModelAndView("inc/redirect").addObject("uri", uri).addObject("tips", tips);
    }
    
    protected ModelAndView back(String tips) {
        return new ModelAndView("inc/back").addObject("tips", tips);
    }
}
