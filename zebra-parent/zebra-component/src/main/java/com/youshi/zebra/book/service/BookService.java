package com.youshi.zebra.book.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.model.PageView;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.book.constants.BookStatus;
import com.youshi.zebra.book.dao.BookDAO;
import com.youshi.zebra.book.exception.BookAlreadyUsedException;
import com.youshi.zebra.book.model.BookModel;
import com.youshi.zebra.book.model.BookModel.BookKeys;
import com.youshi.zebra.book.model.BookOption;
import com.youshi.zebra.core.exception.EntityNotFoundException;
import com.youshi.zebra.core.utils.DAOUtils;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
@Service
public class BookService extends AbstractService<Integer, BookModel> {
	
	private static final Logger logger = LoggerFactory.getLogger(BookService.class);
	
	@Autowired
	private BookDAO bookDAO;
	
	public PageView<BookModel, HasUuid<Integer>> getBooks(Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create();
		PageView<BookModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		
		return page;
	}
	
	// OPTI localcache
	public List<BookOption> getBookOptions() {
		int limit = 100000;
		PageView<BookModel, HasUuid<Integer>> page = getByCursor(null, limit, WhereClause.create());
		if(page.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<BookOption> options = page.getList().stream()
			.map(ele->new BookOption(ele.getId(), ele.getName()))
			.collect(Collectors.toList());
		return options;
	}
	
	public int create(String name, Integer totalCnt, String desc) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(BookKeys.name.name(), name);
		dataMap.put(BookKeys.total_cnt.name(), totalCnt);
		dataMap.put(BookKeys.desc.name(), desc);
		String data = DoradoMapperUtils.toJSON(dataMap);
		
		Number key = bookDAO.insert(data, BookStatus.NOT_USED, System.currentTimeMillis());
		
		int bookId = key.intValue();
		logger.info("Book created succ. id: {}", bookId);
		
		return bookId;
	}
	
	public void modify(Integer bookId, String name, Integer totalCnt, String desc) {
		BookModel book = getById(bookId);
		if(book == null) {
			throw new EntityNotFoundException();
		}
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(BookKeys.name.name(), name);
		dataMap.put(BookKeys.total_cnt.name(), totalCnt);
		dataMap.put(BookKeys.desc.name(), desc);
		String data = DoradoMapperUtils.toJSON(dataMap);
		
		int c = bookDAO.updateData(bookId, data, book.getData());
		DAOUtils.checkAffectRows(c);
		
		logger.info("Book modify succ. id: {}", bookId);
	}
	
	public void updateStatus(Integer bookId, BookStatus status) {
		int c = bookDAO.setStatus(bookId, status);
		DAOUtils.checkAffectRows(c);
		
		logger.info("Update book status to: {}", status);
	}
	
	public void updateBookStatus(Integer bookId, BookStatus status) {
		int c = bookDAO.setStatus(bookId, status);
		DAOUtils.checkAffectRows(c);
		logger.info("Book status to USED. bookId: {}", bookId);
	}

	/**
	 * @param bookId
	 */
	public void delete(Integer adminId, Integer bookId) {
		BookModel book = getById(bookId);
		if(book == null || book.getStatus() != BookStatus.NOT_USED.getValue()) {
			throw new BookAlreadyUsedException();
		}
		
		int c = bookDAO.delete(bookId);
		DAOUtils.checkAffectRows(c);
		logger.info("Book is deleted. adminId: {}, bookId: {}", adminId, bookId);
	}
	
	@Override
	protected AbstractDAO<Integer, BookModel> dao() {
		return bookDAO;
	}
}
