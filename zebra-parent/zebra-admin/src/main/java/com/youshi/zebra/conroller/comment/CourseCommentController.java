package com.youshi.zebra.conroller.comment;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.mvc.model.JsonResultView;

/**
 * 
 * @author wangsch
 * @date 2017年3月4日
 */
@RequestMapping(value = "/course/comment")
@RestController
public class CourseCommentController {
	
	
	public Object list(
			@RequestParam(value = "") Integer teacherId,
			@RequestParam(value = "") Integer courseMetaId
			) {
		return JsonResultView.SUCCESS;
	}
	
	
	
	
	
}
