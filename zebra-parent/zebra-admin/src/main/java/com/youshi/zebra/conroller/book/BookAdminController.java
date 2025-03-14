package com.youshi.zebra.conroller.book;

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
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.book.model.BookModel;
import com.youshi.zebra.book.service.BookImportService;
import com.youshi.zebra.book.service.BookService;
import com.youshi.zebra.view.BookView;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
@RequestMapping(value = "/book/admin")
@Controller
public class BookAdminController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private BookImportService bookImportService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@RequestMapping(value = "/add", method=RequestMethod.POST)
	@ResponseBody
	public Object add(
			@RequestParam(value = "name") String name,
			@RequestParam(value = "totalCnt") Integer totalCnt,
			@RequestParam(value = "desc") String desc
			) {
		bookService.create(name, totalCnt, desc);
		
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/modify", method=RequestMethod.POST)
	@ResponseBody
	public Object modify(
			@RequestParam(value = "bid") Integer bookId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "totalCnt") Integer totalCnt,
			@RequestParam(value = "desc") String desc
			) {
		bookService.modify(bookId, name, totalCnt, desc);
		
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public Object list(
			@Uuid(value = "cursor", type=BookModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		
		PageView<BookModel, HasUuid<Integer>> page = bookService.getBooks(cursor, limit);
		
		Map<String, Object> resultMap = viewBuilder.buildToMap(page, "books", 
				ExplicitViewMapper.getInstance().setViewClass(BookView.class));
		
		ModelAndView mav = new ModelAndView("book/book_list");
		mav.addAllObjects(resultMap);
		return mav;
	}
	
	@RequestMapping(value = "/remove", method=RequestMethod.POST)
	@ResponseBody
	public Object remove(
			@Visitor Integer adminId,
			@RequestParam(value = "bid") Integer bookId
			) {
		bookService.delete(adminId, bookId);
		
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/importUI", method=RequestMethod.GET)
	public ModelAndView importUI(
			@Visitor Integer adminId
			) {
		return new ModelAndView("book/book_import");
	}
	
	
	@RequestMapping(value = "/import", method=RequestMethod.POST)
	@ResponseBody
	public Object imp(
			@Visitor Integer adminId,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "zip") MultipartFile zipUploadFIle,
			@RequestParam(value = "excel") MultipartFile excelUploadFile
			) {
		bookImportService.importBook(name, zipUploadFIle, excelUploadFile);
		return JsonResultView.SUCCESS;
	}
}
