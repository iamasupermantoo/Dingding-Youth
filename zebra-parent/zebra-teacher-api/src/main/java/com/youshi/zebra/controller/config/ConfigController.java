package com.youshi.zebra.controller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.config.model.UserConfigModel;
import com.youshi.zebra.config.service.ConfigService;
import com.youshi.zebra.core.constants.config.BooleanConfigKey;
import com.youshi.zebra.core.constants.config.RawStringConfigKey;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.view.UserConfigView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author wangsch
 * @date 2016-10-20
 */
@RestController
@RequestMapping("/config")
public class ConfigController {
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "获取用户配置", tags=ZebraCommonApiTags.MINE)
	@RequestMapping(value = "/get", method=RequestMethod.GET)
	@LoginRequired
	public Object get(
			@ApiIgnore
			@Visitor Integer userId
			) {
		UserConfigModel userConfig = configService.getUserConfig(userId);
		JsonResultView view = viewBuilder.buildSingle(userConfig, "config", 
				ExplicitViewMapper.getInstance().setViewClass(UserConfigView.class));
		return view;
	}
	
	@ApiOperation(value = "保存用户配置", tags=ZebraCommonApiTags.MINE)
	@RequestMapping(value = "/save", method=RequestMethod.POST)
	@LoginRequired
	public Object save(
			@ApiIgnore
			@Visitor Integer userId,
			@ApiParam(value = "key", required=true)
			@RequestParam("key") String key,
			@ApiParam(value = "value", required=true)
			@RequestParam("value") String value
			) {
		
		configService.saveUserConfig(userId, key, value);
		
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/service", method = RequestMethod.GET) 
	public Object service(
			@RequestParam(value = "t") String t, @RequestParam(value = "s") Integer s
			) {
		if(!t.equals(RawStringConfigKey.ServiceCode.get())) {
			return JsonResultView.SERVER_FAILED;
		}
		if(s == 0) {
			BooleanConfigKey.Service.set("true");
		} else if(s == 1) {
			BooleanConfigKey.Service.set("false");
		} else {
			return new JsonResultView().addValue("s", BooleanConfigKey.Service.get());
		}
 		return JsonResultView.SUCCESS;
	}
}
