package com.youshi.zebra.book.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.youshi.zebra.book.constants.ChapterStatus;
import com.youshi.zebra.book.dao.ChapterDAO;
import com.youshi.zebra.book.dao.FrameConfigDAO;
import com.youshi.zebra.book.exception.BookAlreadyUsedException;
import com.youshi.zebra.book.model.BookModel;
import com.youshi.zebra.book.model.ChapterModel;
import com.youshi.zebra.book.model.ChapterModel.ChapterKeys;
import com.youshi.zebra.core.utils.DAOUtils;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
@Service
public class ChapterService extends AbstractService<Integer, ChapterModel>{
	
	private static final Logger logger = LoggerFactory.getLogger(ChapterService.class);
	
	@Autowired
	private ChapterDAO chapterDAO;
	
	@Autowired
	private FrameConfigDAO frameConfigDAO;
	
	@Autowired
	private BookService bookService;
	
	public ChapterModel getChapter(Integer bookId, int cnt) {
		ChapterModel result = chapterDAO.getChapter(bookId, cnt);
		
		return result;
	}
	
	public PageView<ChapterModel, HasUuid<Integer>> getChapters(Integer bookId, 
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create()
				.and().eq(ChapterKeys.book_id, bookId);
		PageView<ChapterModel, HasUuid<Integer>> page = getByCursor(cursor, limit, params);
		
		return page;
	}
	
	public int create(Integer bookId, Integer cnt, String label, String desc, 
			String homeworkTitle, String homeworkContent) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(ChapterKeys.label.name(), label);
		dataMap.put(ChapterKeys.desc.name(), desc);
		dataMap.put(ChapterKeys.homework_title.name(), homeworkTitle);
		dataMap.put(ChapterKeys.homework_content.name(), homeworkContent);
		String data = DoradoMapperUtils.toJSON(dataMap);
		
		Number key = chapterDAO.insert(bookId, cnt, ChapterStatus.Normal, data, System.currentTimeMillis());
		
		int chapterId = key.intValue();
		logger.info("Chapter created succ. id: {}, bookId: {}", chapterId, bookId);
		
		return chapterId;
	}
	
	public void saveCnts(Integer bookId, List<String> rawCnts) {
		Map<Integer, Integer> cntsMap = parseCnts(rawCnts);
		for(Entry<Integer, Integer> entry : cntsMap.entrySet()) {
			int c = chapterDAO.updateCnt(bookId, entry.getKey(), entry.getValue());
			DAOUtils.checkAffectRows(c);
		}
		
		logger.info("Save cnts succ. bookId: {}, rawCnts: {}", bookId, rawCnts);
	}

	@Override
	protected AbstractDAO<Integer, ChapterModel> dao() {
		return chapterDAO;
	}

	/**
	 * @param bookId
	 * @param chapterId
	 */
	public void removeChapter(Integer bookId, Integer chapterId) {
		// 校验，是否被使用过了，重要
		BookModel book = bookService.getById(bookId);
		if(book == null || book.getStatus() != BookStatus.NOT_USED.getValue()) {
			throw new BookAlreadyUsedException();
		}
		
		int c = chapterDAO.deleteChapter(bookId, chapterId);
		DAOUtils.checkAffectRows(c);
		
		c = frameConfigDAO.deleteFrameConfig(chapterId);
		DAOUtils.checkAffectRows(c, Arrays.asList(0, 1));
		
		logger.info("remove chapter succ. bookId: {}, chapterId: {}", bookId, chapterId);
		
	}

	public void updateChapter(Integer bookId, Integer chapterId, Integer cnt, String label, String desc, 
			String homeworkTitle, String homeworkContent) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(ChapterKeys.label.name(), label);
		dataMap.put(ChapterKeys.desc.name(), desc);
		dataMap.put(ChapterKeys.homework_title.name(), homeworkTitle);
		dataMap.put(ChapterKeys.homework_content.name(), homeworkContent);
		String data = DoradoMapperUtils.toJSON(dataMap);
		int c = chapterDAO.update(bookId, chapterId, cnt, data);
		
		DAOUtils.checkAffectRows(c);
	}
	
	private Map<Integer, Integer> parseCnts(List<String> rawCnts) {
		Map<Integer, Integer> result = new HashMap<>();
		for(String rawCnt : rawCnts) {
			String[] parts = rawCnt.split("-");
			
			int chapterId = Integer.parseInt(parts[0]);
			int cnt = Integer.parseInt(parts[1]);
			result.put(chapterId, cnt);
		}
		
		return result;
	}
}
