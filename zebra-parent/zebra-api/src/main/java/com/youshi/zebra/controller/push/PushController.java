package com.youshi.zebra.controller.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.model.JsonResultView;
import com.dorado.push.service.impl.PushTokenServiceImpl;
import com.youshi.zebra.controller.account.LogoutController;
import com.youshi.zebra.controller.config.ConfigController;

/**
 * 客户端push，绑定、解绑token
 * 
 * @author wangsch
 * @date 2016年11月2日
 */
@RequestMapping("/push")
@RestController
public class PushController {
	private static final Logger logger = LoggerFactory.getLogger(PushController.class);
	@Autowired
	private PushTokenServiceImpl pushTokenService;
	
	/**
	 * 绑定token
	 * 
	 * 客户端调用这个接口：<br />
	 * 1. app启动时调用
	 * 2. 注册成功后调用
	 * 3. 登陆成功后调用
	 * 
	 */
	@RequestMapping(value = "/bind", method=RequestMethod.POST)
	public Object bind(
//			@RequestParam("token") String token
			) {
//		Integer userId = DoradoRequestContext.getUserId();		// userId可能为null
//		PushDevice device = PushDeviceUtils.getDevice(DoradoRequestContext.getAppPlatform());
//		logger.info("userId: {}, token: {}, device: {}", userId, token, device);
//		pushTokenService.bindPushToken(userId, device, token);
		
		return JsonResultView.SUCCESS;
	}
	
	/**
	 * 解绑在其他接口中自动调用.
	 * 
	 * 1. 退出登陆时调用
	 * 2. 客户端关闭push开关时调用
	 * 
	 * @see LogoutController#logout(Integer)
	 * @see ConfigController#save(Integer, String, Object)
	 */
	@RequestMapping(value = "/unbind", method=RequestMethod.POST)
	public Object unbind() {
		throw new UnsupportedOperationException();
	}
	
	
}
