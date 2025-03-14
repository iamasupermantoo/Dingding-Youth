package com.youshi.zebra.controller.mine;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.mine.service.MineService;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.service.UserService;
import com.youshi.zebra.view.MineInfoView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * “我的”相关接口
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@RestController
@RequestMapping("/mine")
@LoginRequired
public class MineController {
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MineService mineService;
	
	
	@ApiOperation(value = "我的资料", tags=ZebraCommonApiTags.MINE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/info", method=RequestMethod.GET)
	public Object info(
			@ApiIgnore @Visitor Integer userId
			) {
		UserModel user = userService.getById(userId);
		JsonResultView result = viewBuilder.buildSingle(user, "info", 
				ExplicitViewMapper.getInstance().setViewClass(MineInfoView.class));
		return result;
	}
	
	@ApiOperation(value = "更新我的资料", tags=ZebraCommonApiTags.MINE)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "头像图片id", name="headImg", dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/update", method=RequestMethod.POST)
	public Object update(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "headImg", type=ImageModel.class, required=false) Integer headImgId,
			@ApiParam(value = "姓名", required=true)
			@RequestParam(value = "name") String name,
			
			@ApiParam(value = "家乡-省份")
			@RequestParam(value = "province", defaultValue="") String province,
			
			@ApiParam(value = "家乡-城市")
			@RequestParam(value = "region", required=false) String region,
			
			@ApiParam(value = "性别")
			@RequestParam(value = "gender", required=false) Integer gender,
			
			@ApiParam(value = "个性签名")
			@RequestParam(value = "signature", required=false) String signature
			) {
		mineService.updateInfo(userId, headImgId, name, province, region, gender, signature, 
				StringUtils.EMPTY, StringUtils.EMPTY);
		
		return JsonResultView.SUCCESS;
	}
	
}
