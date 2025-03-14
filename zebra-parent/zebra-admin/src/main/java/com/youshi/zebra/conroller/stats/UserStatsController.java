package com.youshi.zebra.conroller.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.youshi.zebra.stats.dao.UserStatsRedisDAO.StatsFlag;
import com.youshi.zebra.stats.dao.UserStatsRedisDAO.UserGeneralStats;
import com.youshi.zebra.stats.model.UserAcqByDayModel;
import com.youshi.zebra.stats.model.UserActByDayModel;
import com.youshi.zebra.stats.model.UserRetByDayModel;
import com.youshi.zebra.stats.service.UserStatsRedisService;
import com.youshi.zebra.stats.service.UserStatsService;

/**
 * 
 * 用户相关的统计，是以“天”为统计单位。如：当天注册、当天用户活跃、当天用户存留
 * 
 * @author wangsch
 * @date 2017年8月9日
 */
@RequestMapping(value = "/stats/user")
@Controller
public class UserStatsController {
	@Autowired
	private UserStatsService userStatsService;
	
	@Autowired
	private UserStatsRedisService userStatsRedisService;
	
	@RequestMapping(value = "/acquisition/list", method=RequestMethod.GET)
	public ModelAndView acquisition(
			@Uuid(value = "cursor", type=UserAcqByDayModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="30") Integer limit
			) {
		PageView<UserAcqByDayModel, HasUuid<Integer>> page = userStatsService.getAcqs(cursor, limit);
		UserGeneralStats stats = userStatsRedisService.getUserGeneralStats();
		
		return new ModelAndView("stats/user_acquisition_list")
				.addObject("stats", page)
				.addObject("userCnt", stats.getUserCnt())
				.addObject("payUserCnt", stats.getPayUserCnt());
	}
	
	@RequestMapping(value = "/activation/list", method=RequestMethod.GET)
	public ModelAndView activation(
			@Uuid(value = "cursor", type=UserActByDayModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="30") Integer limit
			) {
		PageView<UserActByDayModel, HasUuid<Integer>> page = userStatsService.getActs(cursor, limit);
		return new ModelAndView("stats/user_activation_list").addObject("stats", page);
	}
	
	@RequestMapping(value = "/retention/list", method=RequestMethod.GET)
	public ModelAndView retention(
			@RequestParam(value = "flag") StatsFlag flag,
			@Uuid(value = "cursor", type=UserRetByDayModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="30") Integer limit
			) {
		PageView<UserRetByDayModel, HasUuid<Integer>> page = userStatsService.getRets(cursor, limit);
		return new ModelAndView("stats/user_retention_list")
				.addObject("stats", page);
	}
}
