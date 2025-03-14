package com.youshi.zebra.controller.misc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.model.JsonResultView;

/**
 * 
 * @author wangsch
 * @date 2017年8月25日
 */
@RestController
public class MiscController {
	
	@RequestMapping(value = "/intro", method=RequestMethod.GET)
	public Object intro() {
		return new JsonResultView().addValue("video", "http://vi.src.ddlad.com/ddlad-intro.mp4");
	}
}
