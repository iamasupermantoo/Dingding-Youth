package com.youshi.zebra.conroller.exam;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.exam.dao.ExamQuestionDAO.OptionsMode;
import com.youshi.zebra.exam.model.ExamModel;
import com.youshi.zebra.exam.model.ExamQuestionModel;
import com.youshi.zebra.exam.service.ExamService;
import com.youshi.zebra.exam.service.ExamService.ExamLevel;
import com.youshi.zebra.media.constants.MediaType;
import com.youshi.zebra.view.ExamQuestionView;
import com.youshi.zebra.view.ExamView;

import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年8月23日
 */
@Controller
@RequestMapping(value = "/exam/admin")
public class ExamAdminController {
	
	@Autowired
	private ExamService examService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	// -------------------------------------------------- exam -------------------------------------------------------
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public ModelAndView list() {
		PageView<ExamModel, HasUuid<Integer>> page = examService.getExams(null, 20);
		
		Map<String, Object> dataMap = viewBuilder.buildToMap(page, "exams", 
				ExplicitViewMapper.getInstance().setViewClass(ExamView.class));
		
		return new ModelAndView("exam/exam_list").addAllObjects(dataMap);
	}
	
	@RequestMapping(value = "/create", method=RequestMethod.POST)
	@ResponseBody
	public Object create(
			@RequestParam(value = "name") String name,
			@RequestParam(value = "level") ExamLevel level
			) {
		examService.create(name, level);
		
		return JsonResultView.SUCCESS;
	}
	
	// -------------------------------------------------- question -------------------------------------------------------
	@RequestMapping(value = "/question/list", method=RequestMethod.GET)
	public ModelAndView questionList(
			@ApiIgnore @Uuid(value = "exam", type=ExamModel.class) Integer examId,
			@ApiIgnore @Uuid(value = "cursor", type=ExamQuestionModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<ExamQuestionModel, HasUuid<Integer>> page = examService.getQuestions(examId, cursor, limit);
		Map<String, Object> dataMap = viewBuilder.buildToMap(page, "questions", 
				ExplicitViewMapper.getInstance().setViewClass(ExamQuestionView.class));
		ModelAndView mav = new ModelAndView("exam/question_list")
				.addAllObjects(dataMap);
		return mav;
	}
	
	@RequestMapping(value = "/question/createUI", method=RequestMethod.GET)
	public ModelAndView createUI() {
		ModelAndView mav = new ModelAndView("exam/question_create");
		return mav;
	}
	
	@RequestMapping(value = "/question/title", method=RequestMethod.POST)
	@ResponseBody
	public Object appendTitle(
			@ApiIgnore @Uuid(value = "exam", type=ExamModel.class) Integer examId,
			@ApiIgnore @Uuid(value = "question", type=ExamQuestionModel.class, required=false) Integer questionId,
			@RequestParam(value = "type", required=false) MediaType type,
			@RequestParam(value = "text") String text,
			@RequestParam(value = "image", required=false) MultipartFile imageFile,
			@RequestParam(value = "audio", required=false) MultipartFile audioFile
			) {
		MultipartFile mediaFile = imageFile != null ? imageFile : audioFile;
		examService.appendTitle(examId, questionId, type, text, mediaFile, null);
		
		return JsonResultView.SUCCESS;
	}
	
	
	@RequestMapping(value = "/question/option", method=RequestMethod.POST)
	@ResponseBody
	public Object appendOption(
			@ApiIgnore @Uuid(value = "question", type=ExamQuestionModel.class) Integer questionId,
			@RequestParam(value = "label") String label,
			@RequestParam(value = "mode") OptionsMode mode,
			@RequestParam(value = "right") Boolean isRightAnswer,
			@RequestParam(value = "type") MediaType type,
			@RequestParam(value = "text", required=false) String text,
			@RequestParam(value = "image", required=false) MultipartFile imageFile,
			@RequestParam(value = "audio", required=false) MultipartFile audioFile
			) {
		MultipartFile mediaMultipartFile = imageFile != null ? imageFile : audioFile;
		examService.appendOption(questionId, label, mode, isRightAnswer, text, type, mediaMultipartFile, null);
		
		return JsonResultView.SUCCESS;
	}
	
	
	@RequestMapping(value = "/question/del", method=RequestMethod.POST)
	@ResponseBody
	public Object del(
			@ApiIgnore @Uuid(value = "question", type=ExamQuestionModel.class) Integer questionId
			) {
		examService.deleteQuestion(questionId);
		
		return JsonResultView.SUCCESS;
	}
}
