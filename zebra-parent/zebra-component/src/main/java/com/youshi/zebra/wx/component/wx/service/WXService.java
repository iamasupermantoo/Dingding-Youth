package com.youshi.zebra.wx.component.wx.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.event.utils.PerfUtils;
import com.ecyrd.speed4j.StopWatch;
import com.youshi.zebra.wx.component.wx.constants.WXFWHConstants;
import com.youshi.zebra.wx.component.wx.dao.WXServerInfosDAO;
import com.youshi.zebra.wx.component.wx.model.WXServerInfos;
import com.youshi.zebra.wx.component.wx.utils.WXUtils;
import com.youshi.zebra.wx.component.wx.utils.WXUtils.WXJsConfig;

import weixin.popular.bean.menu.Button;
import weixin.popular.bean.menu.MenuButtons;
import weixin.popular.bean.ticket.Ticket;
import weixin.popular.bean.token.Token;

/**
 * 
 * 微信公众号相关的Service
 * 
 * @author wangsch
 * @date 2017年4月21日
 */
@Service("WXService")
public class WXService {
	private static final Logger logger = LoggerFactory.getLogger(WXService.class);
	
	@Autowired
	private WXServerInfosDAO wxServerInfosDAO;
	
	/**
	 * 获取配置微信jssdk，需要的信息
	 * 
	 * @param url	前端页面的url
	 * @return		{@link WXJsConfig}
	 */
	public WXJsConfig getWXJsConfig(String url) {
		StopWatch watcher = PerfUtils.getWatcher("WXService.getWXJsConfig");
		
		WXServerInfos config = getWXServerInfos();
		String jsapiTicket = config.getJsapiTicket();
		WXJsConfig wxConfig = WXUtils.getJsConfig(url, jsapiTicket);
		
		watcher.stop();
		return wxConfig;
	}
	
	/**
	 * 获取配置信息，公众号token、jssdk ticket
	 * 
	 * @return {@link WXServerInfos}
	 */
	public WXServerInfos getWXServerInfos() {
		StopWatch watcher = PerfUtils.getWatcher("WXService.getWXServerInfos");
		WXServerInfos config = wxServerInfosDAO.get(WXFWHConstants.APPID);
		config = ensureConfig(config);
		watcher.stop();
		return config;
	}

	/**
	 * 初始化配置信息，公众号token、jssdk ticket
	 */
	public void initServerInfo() {
		StopWatch watcher = PerfUtils.getWatcher("WXService.initServerInfo");
		Token token = WXUtils.getServiceToken();
		Ticket ticket = WXUtils.getJsTicket(token.getAccess_token());

		wxServerInfosDAO.insert(WXFWHConstants.APPID, token.getAccess_token(), token.getExpires_in(), ticket.getTicket(),
				ticket.getExpires_in(), System.currentTimeMillis());
		
		watcher.stop();
	}

	/**
	 * 初始化确保不为空
	 * 
	 * @param config 配置信息，可能为空
	 * @return 配置信息，不会为空
	 */
	private WXServerInfos ensureConfig(WXServerInfos config) {
		if(config != null) {
			return config;
		}
		logger.warn("Cound not find wx server infos");
		initServerInfo();
		return wxServerInfosDAO.get(WXFWHConstants.APPID);
	}

	// ------------------------------------ 公众号首页菜单 ------------------------------------

	/**
	 * 初始化微信公众号菜单
	 */
	public void initMenu() {
		StopWatch watcher = PerfUtils.getWatcher("WXService.initMenu");
		MenuButtons menuButtons = initButtons();
		WXUtils.initMenu(getWXServerInfos().getAccessToken(), menuButtons);
		watcher.stop();
	}
	
	/**
	 * 
	 * 生成微信公众号菜单
	 * 
	 * @return {@link weixin.popular.bean.menu.MenuButtons MenuButtons}
	 */
	public static MenuButtons initButtons() {
		List<Button> buttons = new ArrayList<Button>(3);		// 一级菜单
		Button b1 = new Button();
		b1.setType("view");
		b1.setName("待交作业");
//		b1.setUrl(WXFWHConstants.B1_HOMEWORKS_URL);
		buttons.add(b1);
		
		Button b3 = new Button();
		b3.setType("view");
		b3.setName("我的信息");
		ArrayList<Button> sb = new ArrayList<Button>(2);		// 二级菜单
		Button sb1 = new Button();
		sb1.setType("view");
		sb1.setName("我的课表");
//		sb1.setUrl(WXConstants.B3_SUB1_COURSES_URL);
		sb.add(sb1);
		
		b3.setSub_button(sb);
		buttons.add(b3);
		
		MenuButtons menuButtons = new MenuButtons();
		Button[] arr = new Button[3];
		menuButtons.setButton(buttons.toArray(arr));
		return menuButtons;
	}
	
//	public static void main(String[] args) {
//		Menu r = MenuAPI.menuGet("PhcoR7uq0Hk7Y_wMDaHIreqrPe2dewk9MJ17G-r1zIVjzg9e0zKo8TLezmtPFvsgCE9Sz2y2fyuE-fYgN4-bx8FxRToLRkhkahyhSu93v4mta-RJGesK9Z4lvlNGq-paYSOiAJAGAW");
//		System.out.println(r);
//	}
}
