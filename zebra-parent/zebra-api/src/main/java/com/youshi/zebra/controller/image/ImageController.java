package com.youshi.zebra.controller.image;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.service.ImageService;
import com.youshi.zebra.view.ImageView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author wangsch
 * @date 2017年1月5日
 */
@RequestMapping("/image")
@RestController
@LoginRequired
public class ImageController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ImageController.class);
	
	@Autowired
	private ImageService imageService;
	
	@ApiOperation(value = "上传图片", tags=ZebraCommonApiTags.COMMON)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public Object upload(
			@ApiIgnore @Visitor Integer userId,
			@RequestParam("file") MultipartFile file
			) {
		Integer imageId = null;
		try {
			
			imageId = imageService.createImage(userId, file.getBytes());
		} catch (Exception e) {
			logger.error("Upload image FAIL, user_id: {}, contentType: {}, original file name: {}", 
					userId, file.getContentType(), file.getOriginalFilename());
			logger.error("", e);
			return ZebraMetaCode.ImageUploadError.toView();
		}
		ImageModel image = imageService.getById(imageId);
		return new JsonResultView()
				.addValue("id", UuidUtils.getUuid(ImageModel.class, imageId))
				.addValue("image", new ImageView(image))
				;
	}

}
