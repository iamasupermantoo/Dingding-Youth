package com.youshi.zebra.controller.misc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.view.ConstantsView;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 说明：这个接口是启动app，第一个请求的接口，请一定确保接口稳定性！
 * 
 * @author wangsch
 * @date 2017年4月6日
 */
@RequestMapping(value = "/constants")
@RestController
public class ConstantController {
	@ApiOperation(value = "一些常量信息", tags=ZebraCommonApiTags.FINAL, response=ConstantsView.class)
	@RequestMapping(value = "", method=RequestMethod.GET)
	public Object all(
			@ApiIgnore @Visitor Integer userId
			) {
		return new JsonResultView()
				.addValue("constants", new ConstantsView());
	}
}
