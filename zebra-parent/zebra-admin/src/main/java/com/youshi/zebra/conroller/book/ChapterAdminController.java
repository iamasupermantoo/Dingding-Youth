package com.youshi.zebra.conroller.book;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.book.model.ChapterModel;
import com.youshi.zebra.book.service.ChapterService;
import com.youshi.zebra.view.ChapterView;

import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
@RequestMapping(value = "/chapter/admin")
@Controller
public class ChapterAdminController {
	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@RequestMapping(value = "/add", method=RequestMethod.POST)
	@ResponseBody
	public Object add(
			@RequestParam(value = "bid") Integer bookId,
			@RequestParam(value = "cnt") Integer cnt,
			@RequestParam(value = "label") String label,
			@RequestParam(value = "desc") String desc,
			@RequestParam(value = "homeworkTitle") String homeworkTitle,
			@RequestParam(value = "homeworkContent") String homeworkContent
			) {
		chapterService.create(bookId, cnt, label, desc, homeworkTitle, homeworkContent);
		
		return JsonResultView.SUCCESS;
	}
	
	
	@RequestMapping(value = "/saveCnts", method=RequestMethod.POST)
	@ResponseBody
	public Object saveOrder(
			@RequestParam(value = "bid") Integer bookId,
			@RequestParam(value = "cnts") List<String> rawCnts
			) {
		chapterService.saveCnts(bookId, rawCnts);
		
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/remove", method=RequestMethod.POST)
	@ResponseBody
	public Object remove(
			@RequestParam(value = "bid") Integer bookId,
			@RequestParam(value = "cid") Integer chapterId
			) {
		chapterService.removeChapter(bookId, chapterId);
		
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/update", method=RequestMethod.POST)
	@ResponseBody
	public Object update(
			@RequestParam(value = "bid") Integer bookId,
			@RequestParam(value = "cid") Integer chapterId,
			@RequestParam(value = "cnt") Integer cnt,
			@RequestParam(value = "label") String label,
			@RequestParam(value = "desc") String desc,
			@RequestParam(value = "homeworkTitle") String homeworkTitle,
			@RequestParam(value = "homeworkContent") String homeworkContent
			) {
		chapterService.updateChapter(bookId, chapterId, cnt, label, desc, homeworkTitle, homeworkContent);
		
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public ModelAndView list(
			@RequestParam(value = "bid") Integer bookId,
			@Uuid(value = "cursor", type=ChapterModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		
		PageView<ChapterModel, HasUuid<Integer>> page = chapterService
				.getChapters(bookId, cursor, limit);
		
		Map<String, Object> resultMap = viewBuilder.buildToMap(page, "chapters", 
				ExplicitViewMapper.getInstance().setViewClass(ChapterView.class));
		
		ModelAndView mav = new ModelAndView("book/chapter_list");
		return mav.addAllObjects(resultMap);
	}
}
