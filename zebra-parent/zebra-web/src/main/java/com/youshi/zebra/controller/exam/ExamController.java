package com.youshi.zebra.controller.exam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.tuple.TwoTuple;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.core.web.annotation.LoginRequired;
import com.youshi.zebra.exam.model.ExamModel;
import com.youshi.zebra.exam.model.ExamQuestionModel;
import com.youshi.zebra.exam.service.ExamService;
import com.youshi.zebra.exam.service.ExamService.ExamLevel;
import com.youshi.zebra.view.ExamQuestionView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年8月1日
 */
@RequestMapping(value = "/exam")
@RestController
@LoginRequired
public class ExamController {
	
	@Autowired
	private ExamService examService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "保存评测结果。提交后，返回评测结果级别（data.level），数字。1: 一级，2：二级，3：三级", 
			tags=ZebraCommonApiTags.EXAM)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "试卷id", name="exam", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/answers", method=RequestMethod.POST)
	public Object saveExam(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "exam", type=ExamModel.class) Integer examId,
			@ApiParam(value = "答案，逗号分隔的字符串，大写，如：A,B,A,C", required=true)
			@RequestParam(value = "answers") String answers
			) {
		int level = examService.commitExam(userId, examId, answers);
		return new JsonResultView().addValue("level", level);
	}
	
	@ApiOperation(value = "随机一个评测试卷，获取n多个题目", tags=ZebraCommonApiTags.EXAM)
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query")
	})
	@RequestMapping(value = "/questions", method=RequestMethod.GET)
	public Object exam(
			@ApiIgnore @Visitor Integer userId
			) {
		TwoTuple<ExamModel, List<ExamQuestionModel>> res = examService.getQuestions(userId);
		ExamModel exam = res.getFirst();
		List<ExamQuestionModel> questions = res.getSecond();
		
		List<Object> list = viewBuilder.buildToList(questions,
				ExplicitViewMapper.getInstance().setViewClass(ExamQuestionView.class));
		return new JsonResultView().addValue("questions", list)
				.addValue("exam", exam.getUuid());
	}
}
