package com.youshi.zebra.conroller.live;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.model.JsonResultView;

/**
 * 
 * @author wangsch
 * @date 2017年3月17日
 */
@RequestMapping(value = "/live/admin")
@RestController
public class LiveAdminController {
	
	@RequestMapping(value = "/info", method=RequestMethod.GET)
	public Object info() {
		return JsonResultView.SUCCESS;
	}
	
	
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public Object list() {
		
		
		
		return JsonResultView.SUCCESS;
	}
	
	
}
