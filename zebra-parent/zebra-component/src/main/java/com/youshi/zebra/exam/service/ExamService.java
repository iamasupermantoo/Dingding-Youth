package com.youshi.zebra.exam.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasData;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.model.PageView;
import com.dorado.framework.tuple.TwoTuple;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.google.common.base.Splitter;
import com.youshi.zebra.core.exception.EntityNotFoundException;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.core.utils.DateTimeUtils;
import com.youshi.zebra.exam.NotSuitableExamLevelException;
import com.youshi.zebra.exam.dao.ExamDAO;
import com.youshi.zebra.exam.dao.ExamDAO.ExamStatus;
import com.youshi.zebra.exam.dao.ExamQuestionDAO;
import com.youshi.zebra.exam.dao.ExamQuestionDAO.ExamQuestionStatus;
import com.youshi.zebra.exam.dao.ExamQuestionDAO.OptionsMode;
import com.youshi.zebra.exam.model.ExamModel;
import com.youshi.zebra.exam.model.ExamModel.ExamKeys;
import com.youshi.zebra.exam.model.ExamQuestionModel;
import com.youshi.zebra.exam.model.ExamQuestionModel.ExamQuestionKeys;
import com.youshi.zebra.exam.model.OptionItem;
import com.youshi.zebra.exam.model.OptionItemWrapper;
import com.youshi.zebra.exam.model.TitleItem;
import com.youshi.zebra.media.constants.MediaType;
import com.youshi.zebra.media.service.MediaService;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.model.UserModel.UserKey;
import com.youshi.zebra.user.service.UserService;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class ExamService extends AbstractService<Integer, ExamModel> {
	private static final Logger logger = LoggerFactory.getLogger(ExamService.class);
	
	private static final Integer num = 0;
	
	@Autowired
	private ExamDAO examDAO;
	
	@Autowired
	private ExamQuestionDAO examQuestionDAO;
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private ExamQuestionService examQuestionService;
	
	
	public PageView<ExamModel, HasUuid<Integer>> getExams(Integer cursor, int limit) {
		return getByCursor(cursor, limit, WhereClause.create());
	}
	
	
	public int create(String name, ExamLevel level) {
		int examId = examDAO.insert(level.getValue(), name, 
				HasData.EMPTY_DATA, ExamStatus.Normal, System.currentTimeMillis());
		
		logger.info("Create exam succ. examId: {}", examId);
		
		return examId;
	}
	
	// -------------------------------------------------- exam相关 -------------------------------------------------- 
	@Autowired
	private UserService userService;
	
	private static Map<Integer, ExamLevel> examLevelMap = new HashMap<>();
	static {
		examLevelMap.put(3, ExamLevel.level1);
		examLevelMap.put(4, ExamLevel.level1);
		examLevelMap.put(5, ExamLevel.level2);
		examLevelMap.put(6, ExamLevel.level2);
		examLevelMap.put(7, ExamLevel.level3);
		examLevelMap.put(8, ExamLevel.level4);
	}
	
	public ExamModel randomExam(ExamLevel level) {
		WhereClause params = WhereClause.create()
				.and().eq(ExamKeys.status, ExamStatus.Normal.getValue())
				.and().eq(ExamKeys.level, level.getValue());
		PageView<ExamModel, HasUuid<Integer>> page = getByCursor(null, 1000, params);
		if(page.isEmpty()) {
			return null;
		}
		List<ExamModel> exams = page.getList();
		return exams.get(RandomUtils.nextInt(0, exams.size()));
	}
	
	
	public TwoTuple<ExamModel, List<ExamQuestionModel>> getQuestions(Integer userId) {
		UserModel user = userService.getById(userId);
		
		String birthday = user.getBirthday();
		if(StringUtils.isEmpty(birthday)) {
			throw new NotSuitableExamLevelException("User birthday empty: " + birthday + ", user: " + userId);
		}
		
		int age = (int)((System.currentTimeMillis() - DateTimeUtils.parseDate(birthday)) / TimeUnit.DAYS.toMillis(365));
		
		ExamLevel level = examLevelMap.get(age);
		if(level == null) {
			throw new NotSuitableExamLevelException("Birthday: " + birthday + ", user: " + userId);
		}
		ExamModel exam = randomExam(level);
		if(exam == null) {
			throw new NotSuitableExamLevelException("Level: " + level);
		}
		return new TwoTuple<ExamModel, List<ExamQuestionModel>>(exam, getQuestions(exam.getId(), null, 100).getList());
	}
	
	public int commitExam(Integer userId, Integer examId, String answers) {
		UserModel user = userService.getById(userId);
		if(user == null) {
			throw new EntityNotFoundException();
		}
		
		ExamModel exam = getById(examId);
		
		List<String> rightAnswers = getRightAnswers(examId);
		
		int level = computeScore(answers, rightAnswers, exam);
		Map<String, Object> dataMap = user.resolvedData();
		dataMap.put(UserKey.exam_level.name(), level);
		dataMap.put(UserKey.exam_answers.name(), answers);
		
		String data = DoradoMapperUtils.toJSON(dataMap);
		userService.update(userId, user.getName(), user.getSignature(), data);
		logger.info("User exam succ. userId: {}, level: {}, answers: {}", userId, level, answers);
		
		return level;
	}
	
	private List<String> getRightAnswers(Integer examId) {
		List<ExamQuestionModel> answerList = getQuestions(examId, null, 100).getList();
		
		List<String> rightAnswers = answerList.stream()
				.map(ExamQuestionModel::getRightAnswer)
				.collect(Collectors.toList());
		
		return rightAnswers;
	}


	// ----------------------------------------- question ---------------------------------------------
	public PageView<ExamQuestionModel, HasUuid<Integer>> getQuestions(Integer examId, 
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create()
				.and().eq(ExamQuestionKeys.exam_id, examId);
		
		PageView<ExamQuestionModel, HasUuid<Integer>> page = examQuestionService.getByCursorAsc(cursor, limit, params);
		
		return page;
	}
	
	public Integer appendTitle(Integer examId, Integer questionId, 
			MediaType type, String text, MultipartFile mediaMultipartFile, File mediaFile) {
		if(questionId != null) {
			ExamQuestionModel question = examQuestionService.getById(questionId);
			String title = appendTitle(question.getTitle(), text, type, mediaMultipartFile, mediaFile);
			int c = examQuestionDAO.updateTitle(questionId, title);
			DAOUtils.checkAffectRows(c);
		}
		
		else {
			String title = appendTitle(null, text, type, mediaMultipartFile, mediaFile);
			questionId = examQuestionDAO.insert(examId, num, title, StringUtils.EMPTY, OptionsMode._0.getValue(), 
					HasData.EMPTY_DATA, ExamQuestionStatus.Normal, System.currentTimeMillis());
		}
		
		return questionId;
		
	}

	/**
	 * @param questionId
	 */
	public void deleteQuestion(Integer questionId) {
		int c = examQuestionDAO.delete(questionId);
		DAOUtils.checkAffectRows(c);
	}
	
	public void appendOption(Integer questionId, String label, OptionsMode mode, Boolean isRightAnswer,
			String text, MediaType type, MultipartFile mediaMultipartFile, File mediaFile) {
		ExamQuestionModel question = examQuestionService.getById(questionId);
		String options = appendOption(question.getOptions(), label, text, type, mediaMultipartFile, mediaFile);
		
		Map<String, Object> dataMap = question.resolvedData();
		if(isRightAnswer) {
			dataMap.put(ExamQuestionKeys.right_answer.name(), label);
		}
		
		int c = examQuestionDAO.updateOptions(questionId, options, mode, DoradoMapperUtils.toJSON(dataMap));
		DAOUtils.checkAffectRows(c);
	}
	
	private String appendTitle(String title, String text, MediaType type, 
			MultipartFile mediaMultipartFile, File mediaFile) {
		List<TitleItem> titleItems = null;
		if(StringUtils.isNotEmpty(title)) {
			titleItems = DoradoMapperUtils.fromJSON(title, ArrayList.class, TitleItem.class);
		}
		if(titleItems == null) {
			titleItems = new ArrayList<>();
		}
		TitleItem item = new TitleItem();
		item.setType(type.getValue());
		titleItems.add(item);
		
		if(type == MediaType.Text) {
			item.setText(text);
		} else {
			Integer uploadId = null;
			if(mediaMultipartFile != null) {
				uploadId = mediaService.doMediaUpload(WebRequestContext.getUserId(), type, mediaMultipartFile);
			} else {
				uploadId = mediaService.doMediaUpload(WebRequestContext.getUserId(), type, mediaFile);
			}
			
			if(type == MediaType.Audio) {
				item.setAudioId(uploadId);
			} else if(type == MediaType.Image) {
				item.setImageId(uploadId);
			}
		}
		
		return DoradoMapperUtils.toJSON(titleItems);
	}
	

	private String appendOption(String options, String label, String text, MediaType type,
			MultipartFile mediaMultipartFile, File mediaFile) {
		List<OptionItemWrapper> optionItems = null;
		if(StringUtils.isNotEmpty(options)) {
			optionItems = DoradoMapperUtils.fromJSON(options, 
					ArrayList.class, OptionItemWrapper.class);
		}
		if(optionItems == null) {
			optionItems = new ArrayList<>();
		}
		Map<String, OptionItemWrapper> wrappers = construct(optionItems);
		OptionItemWrapper wrapper = wrappers.get(label);
		if(wrapper == null) {
			wrapper = new OptionItemWrapper();
			wrapper.setLabel(label);
			wrapper.setItems(new ArrayList<>());
			optionItems.add(wrapper);
		}
		List<OptionItem> items = wrapper.getItems();
		
		OptionItem item = new OptionItem();
		item.setType(type.getValue());
		items.add(item);
		
		if(type == MediaType.Text) {
			item.setText(text);
		} else {
			Integer uploadId = null;
			if(mediaMultipartFile != null) {
				uploadId = mediaService.doMediaUpload(WebRequestContext.getUserId(), type, mediaMultipartFile);
			} else {
				uploadId = mediaService.doMediaUpload(WebRequestContext.getUserId(), type, mediaFile);
			}
			
			if(type == MediaType.Audio) {
				item.setAudioId(uploadId);
			} else if(type == MediaType.Image) {
				item.setImageId(uploadId);
			}
		}
		
		return DoradoMapperUtils.toJSON(optionItems);
	}
	
	private Map<String, OptionItemWrapper> construct(List<OptionItemWrapper> optionItems) {
		return optionItems.stream()
			.map(Function.identity())
			.collect(Collectors.toMap(ele->ele.getLabel(), Function.identity()));
	}
	
	private int computeScore(String answers, List<String> rightAnswers, ExamModel exam) {
		Iterable<String> it = Splitter.on(",").split(answers);
		int rightCnt = 0;
		int question = 0;
		for (String answer : it) {
			String rightAnswer = rightAnswers.get(question++);
			if(StringUtils.equalsIgnoreCase(answer, rightAnswer)) {
				rightCnt++;
			}
		}
		
		/*
		 *  首先四套试卷对应四个level：1，2，3，4
			如果是20道题，答对0—10道，则成绩在试卷的level基础上减一。答对11-16题，成绩为试卷的level。答对17-20题，成绩在试卷的level的基础上加一。
			如果是30道题，答对0-15道题，则成绩在试卷的level基础上减一。答16-25道题，则成绩为试卷的level。答对26-30道题，则成绩在试卷的level基础上加一。
			注：以后会有8套测试题，当做第8套测试题时，就算做的全对，等级也是level8。

		 */
		int level = 0;
		int examLevel = exam.getLevel();
		int questionCount = rightAnswers.size();
		if(questionCount == 20) {
			if(rightCnt >= 0 && rightCnt< 11) {
				level = examLevel - 1;
			} else if(rightCnt >= 11 && rightCnt < 17) {
				level = examLevel;
			} else if(rightCnt >= 17) {
				level = examLevel + 1;
			}
		} else if(questionCount == 30) {
			if(rightCnt >= 0 && rightCnt< 16) {
				level = examLevel - 1;
			} else if(rightCnt >= 16 && rightCnt < 26) {
				level = examLevel;
			} else if(rightCnt >= 26) {
				level = examLevel + 1;
			}
		}
		
		return level;
	}
	
	public enum ExamLevel {
		level1(1, "level1"),
		level2(2, "level2"),
		level3(3, "level3"),
		level4(4, "level4"),
		;
		private final int value;
		private final String name;
		ExamLevel(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }

	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }

	    private static final IntObjectMap<ExamLevel> map = new IntObjectOpenHashMap<>();
	    static {
	        for (ExamLevel e : ExamLevel.values()) {
	            map.put(e.getValue(), e);
	        }
	    }

	    public static final ExamLevel fromValue(Integer value) {
	        return map.get(value);
	    }
	}
	
	@Override
	public AbstractDAO<Integer, ExamModel> dao() {
		return examDAO;
	}
}