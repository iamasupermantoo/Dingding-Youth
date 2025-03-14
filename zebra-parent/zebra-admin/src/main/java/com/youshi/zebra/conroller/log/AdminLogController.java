package com.youshi.zebra.conroller.log;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.framework.tuple.TwoTuple;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.admin.log.constants.AdminLogType;
import com.youshi.zebra.admin.log.model.AdminLogModel;
import com.youshi.zebra.admin.log.service.AdminLogService;
import com.youshi.zebra.configuration.Swagger2Configuration.SwaggerTags;
import com.youshi.zebra.core.utils.DateTimeUtils;
import com.youshi.zebra.exception.common.ParamVerifyException;
import com.youshi.zebra.view.AdminLogView;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author wangsch
 * @date 2017年2月25日
 */
@RequestMapping(value = "/adminlog")
@Controller
public class AdminLogController {
	
	@Autowired
	private AdminLogService adminLogService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "查询操作日志", tags=SwaggerTags.ADMIN_LOG)
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "from", required=false) String fromStr,
			@RequestParam(value = "to", required=false) String toStr,
			@RequestParam(value = "aid", required=false) Integer adminId,
			@RequestParam(value = "types", required=false) List<AdminLogType> types,
			@RequestParam(value = "keyword", required=false) String keyword,
			@Uuid(value = "cursor", type=AdminLogModel.class, required=false) Long cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		TwoTuple<Long, Long> range = parseRante(fromStr, toStr);
		
		ModelAndView mav = new ModelAndView("adminlog/admin_log_list");
		PageView<AdminLogModel, HasUuid<Long>> page = adminLogService
				.getLog(range.first, range.second, types, adminId, keyword, cursor, limit);
		
		Map<String, Object> dataMap = viewBuilder.buildToMap(page, "logs", 
				ExplicitViewMapper.getInstance().setViewClass(AdminLogView.class));
		mav.addAllObjects(dataMap);
		
		// 查询条件
		mav.addObject("from", fromStr)
			.addObject("to", toStr)
			.addObject("keyword", keyword)
			.addObject("aid", adminId)
			.addObject("types", types)
			;
		
		return mav;
	}
	
	private TwoTuple<Long, Long> parseRante(String fromStr, String toStr) {
		Long from = null;
		Long to = null;
		try {
			if(StringUtils.isNotEmpty(fromStr)) {
				from = DateTimeUtils.timestamp(fromStr, "yyyy-MM-dd");
			}
			if(StringUtils.isNotEmpty(toStr)) {
				to = DateTimeUtils.timestamp(toStr, "yyyy-MM-dd");
			}
		}catch (ParseException e) {
			throw new ParamVerifyException("Invalid datetime. from: " + fromStr + ", to: " + toStr);
		}
		return new TwoTuple<Long, Long>(from, to);
	}
	
}
