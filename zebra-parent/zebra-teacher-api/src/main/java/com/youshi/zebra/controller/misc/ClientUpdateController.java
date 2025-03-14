package com.youshi.zebra.controller.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.reqcontext.AppVer;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.dorado.mvc.reqcontext.Platform;
import com.youshi.zebra.misc.service.ClientVersionService;
import com.youshi.zebra.misc.service.ClientVersionService.LatestVersion;

/**
 * 客户端版本
 * 
 * @author wangsch
 * @date		2016年11月14日
 *
 */
@RequestMapping(value = "/client/version")
@RestController
public class ClientUpdateController {
	
	
	@Autowired
	private ClientVersionService clientVersionService;
	
	@RequestMapping(value = "", method=RequestMethod.GET)
	public Object version() {
		Platform platform = WebRequestContext.getAppPlatform();
		AppVer appver = WebRequestContext.getAppVer();
		LatestVersion version = clientVersionService.checkVersionTest(platform, appver);
		
		JsonResultView view = new JsonResultView();
		if(version != null) {
			view.addValue("version", version);
		}
		
		return view;
	}
	
	/**
	 * 检查新版本
	 */
	@Deprecated
	@RequestMapping(value = "/check", method=RequestMethod.GET)
	public Object check() {
		return JsonResultView.SUCCESS;
	}
}
