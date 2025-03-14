package com.youshi.zebra.exam.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dorado.framework.tuple.ThreeTuple;
import com.dorado.framework.utils.DoradoBeanFactory;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.core.utils.ExcelUtils;
import com.youshi.zebra.exam.dao.ExamQuestionDAO.OptionsMode;
import com.youshi.zebra.exam.service.ExamService.ExamLevel;
import com.youshi.zebra.exception.base.DoradoRuntimeException;
import com.youshi.zebra.media.constants.MediaType;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * 
 * 
 * 教材导入Service
 * 
 * @author wangsch
 * @date 2017年8月21日
 */
@Service
public class ExamImportService {
	private static final Logger logger = LoggerFactory.getLogger(ExamImportService.class);
	
	@Autowired
	private ExamService examService;
	
	public ExamImportResult importExam(String name, ExamLevel level, 
			MultipartFile zipUploadFIle, MultipartFile excelUploadFile) {
		// 1. 准备数据：解析压缩包和excel文件
		ExamData examData = parseExamData(name, level, zipUploadFIle, excelUploadFile);
		
		// 2. 创建exam->创建question
		ExamImportResult result = createExam(examData);
		
		logger.info("Import exam succ. result: {}", DoradoMapperUtils.toJSON(result));
		
		return result;
	}
	
	public ExamImportResult importExam(String name, ExamLevel level, File zipUploadFIle, File excelUploadFile) {
		// 1. 准备数据：解析压缩包和excel文件
		ExamData examData = parseExamData(name, level, zipUploadFIle, excelUploadFile);
		
		// 2. 创建exam->创建question
		ExamImportResult result = createExam(examData);
		
		logger.info("Import exam succ. result: {}", DoradoMapperUtils.toJSON(result));
		
		return result;
	}
	
	private ExamImportResult createExam(ExamData exam) {
		int examId = examService.create(exam.getName(), exam.getLevel());
		
		Integer questionId = null;
		OptionsMode mode = null;
		String rightAnswer = null;
		int questionCnt = 0;
		for (ExcelOneLine line : exam.getLines()) {
			// 题号
			Integer num = line.getQuestionNum();
			
			// title
			String title = line.getTitle();
			if(StringUtils.isNotEmpty(title)) {
				ThreeTuple<MediaType, String, File> three = parseMedia(title, exam.getMediaDir());
				if(num != null) {
					questionId = null;
					rightAnswer = line.getRightOption();
					mode = line.getMode();
					
					questionId = examService.appendTitle(examId, questionId, three.first, three.second, null, three.third);
					questionCnt++;
				} else {
					examService.appendTitle(examId, questionId, three.first, three.second, null, three.third);
				}
			}
			
			
			// options
			appendOption(questionId, mode, rightAnswer, line.getOptionA(), "A", exam.getMediaDir());
			appendOption(questionId, mode, rightAnswer, line.getOptionB(), "B", exam.getMediaDir());
			appendOption(questionId, mode, rightAnswer, line.getOptionC(), "C", exam.getMediaDir());
			appendOption(questionId, mode, rightAnswer, line.getOptionD(), "D", exam.getMediaDir());
		}
		
		ExamImportResult result = new ExamImportResult();
		result.setQuestionCnt(questionCnt);
		return result;
	}
	
	private void appendOption(Integer questionId, OptionsMode mode, String rightAnswer, String cellValue, String label,
			String mediaDir) {
		if(StringUtils.isEmpty(cellValue)) {
			return;
		}
		ThreeTuple<MediaType, String, File> three = parseMedia(cellValue, mediaDir);
		examService.appendOption(questionId, label, mode, label.equals(rightAnswer), 
				three.second, three.first, null, three.third);
	}
	
	
	public ThreeTuple<MediaType, String, File> parseMedia(String cellValue, String mediaDir) {
		MediaType type = mediaType(cellValue);
		String text = null;
		if(type == MediaType.Text) {
			text = cellValue;
		}
		File mediaFile = null;
		if(type == MediaType.Audio) {
			mediaFile = new File(mediaDir, "mp3/" + cellValue);
		}
		if(type == MediaType.Image) {
			mediaFile = new File(mediaDir, "png/" + cellValue);
		}
		
		return new ThreeTuple<MediaType, String, File>(type, text, mediaFile);
	}
	
	
	private ExamData parseExamData(String name, ExamLevel level, 
			MultipartFile zipUploadFile, MultipartFile excelUploadFile) {
		String uuid = UUID.randomUUID().toString();
		try {
			File zipFile = new File("/tmp/" + uuid + ".zip");
			zipUploadFile.transferTo(zipFile);
			
			File excelFile = new File("/tmp/" + uuid + "." + FilenameUtils.getExtension(excelUploadFile.getOriginalFilename()));
			FileUtils.writeByteArrayToFile(excelFile, excelUploadFile.getBytes());
			
			return parseExamData(name, level, zipFile, excelFile);
		} catch (Exception e) {
			throw new DoradoRuntimeException();
		}
	}
	
	
	private ExamData parseExamData(String name, ExamLevel level, File zipFile, File excelFile) {
		// excel
		List<ExcelOneLine> lines = new ArrayList<>();
		
		try {
			Workbook workbook = ExcelUtils.createWorkbook(FileUtils.readFileToByteArray(excelFile), 
					FilenameUtils.getExtension(excelFile.getName()));
			Sheet sheet = workbook.getSheetAt(0);
			ExcelUtils.shiftEmptyRows(sheet);
			
			// 一个题，可能由多行组成，一个新的题号，代表一个新的题的开始
			/*
			 *  题号	题干					选项A			选项B		选项C			选项D		正确选项		选项排列
				1	和下面图片类似的是？	1-A.jpg													A			竖排
					1-1.jpg				
				
				2	2-1.mp3				Taylor Swift	Lady Gaga	Beyonce	
					这首歌的作者是？				
			 */
			Iterator<Row> it = sheet.rowIterator();
			it.next();
			while(it.hasNext()) {
				Row row = it.next();
				Cell cell = row.getCell(0);
				Integer questionNum = ExcelUtils.getInteger(cell);
				
				ExcelOneLine line = new ExcelOneLine();
				line.setQuestionNum(questionNum);
				line.setTitle(ExcelUtils.getCellStr(row.getCell(1)));
				
				line.setOptionA(ExcelUtils.getCellStr(row.getCell(2)));
				line.setOptionB(ExcelUtils.getCellStr(row.getCell(3)));
				line.setOptionC(ExcelUtils.getCellStr(row.getCell(4)));
				line.setOptionD(ExcelUtils.getCellStr(row.getCell(5)));
				line.setRightOption(ExcelUtils.getCellStr(row.getCell(6)));
				line.setOptionsMode(ExcelUtils.getCellStr(row.getCell(7)));
				
				lines.add(line);
			}
		} catch(Exception e) {
			throw new DoradoRuntimeException(e);
		}
		
		// 压缩包
		String unzipDest = null;
		try {
			String timestamp = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss");
			unzipDest = "/tmp/exam_import_" + zipFile.getName() + "_" + timestamp;
			
			unzip(zipFile.getAbsolutePath(), unzipDest, null);
		} catch (Exception e) {
			throw new DoradoRuntimeException(e);
		}
		
//		unzipDest = unzipDest + "/" + zipFile.getName().replace(".zip", "");
		
		File unzipDestDir = new File(unzipDest);
		
		// validate
		for (ExcelOneLine line : lines) {
			Integer num = line.getQuestionNum();
			
			// 题干
			String title = line.getTitle();
			boolean exist = mediaFileExist(unzipDestDir, title);
			if(!exist) {
				throw new IllegalArgumentException(line.toString());
			}
			
			// 选项
			String optionA = line.getOptionA();
			exist = mediaFileExist(unzipDestDir, optionA);
			if(!exist) {
				throw new IllegalArgumentException(line.toString());
			}
			String optionB = line.getOptionB();
			exist = mediaFileExist(unzipDestDir, optionB);
			if(!exist) {
				throw new IllegalArgumentException(line.toString());
			}
			String optionC = line.getOptionC();
			exist = mediaFileExist(unzipDestDir, optionC);
			if(!exist) {
				throw new IllegalArgumentException(line.toString());
			}
			String optionD = line.getOptionD();
			exist = mediaFileExist(unzipDestDir, optionD);
			if(!exist) {
				throw new IllegalArgumentException(line.toString());
			}
			
			// 正确答案和选项排列校验
			if(num != null) {
				String rightOption = line.getRightOption();
				String optionsMode = line.getOptionsMode();
				if(StringUtils.isEmpty(rightOption) || StringUtils.isEmpty(optionsMode)) {
					throw new IllegalArgumentException(line.toString());
				}
				if("横排".equals(optionsMode)) {
					line.setMode(OptionsMode._1);
				} else if("竖排".equals(optionsMode)) {
					line.setMode(OptionsMode._2);
				} else {
					throw new IllegalArgumentException(line.toString());
				}
			}
		}
		
		// 返回
		ExamData exam = new ExamData();
		exam.setName(name);
		exam.setLevel(level);
		exam.setLines(lines);
		exam.setMediaDir(unzipDest);
		
		return exam;
	}
	
	public static void unzip(String source, String destination, String password) throws ZipException{
	    try {
	         ZipFile zipFile = new ZipFile(source);
	         if (zipFile.isEncrypted()) {
	            zipFile.setPassword(password);
	         }
	         zipFile.extractAll(destination);
	    } catch (ZipException e) {
	        throw e;
	    }
	}
	
	private boolean mediaFileExist(File unzipDestDir, String filename) {
		if(StringUtils.isEmpty(filename)) {
			return true;
		}
		MediaType mediaType = mediaType(filename);
		if(mediaType == MediaType.Text) {
			return true;
		}
		
		String dir = mediaType == MediaType.Image ? "png/" : "mp3/";
		filename = dir + filename;
		if(FileUtils.getFile(unzipDestDir, filename).exists()) {
			return true;
		}
		
		return false;
	}
	
	private static final List<String> IMAGE_SUFFIXES = Arrays.asList("jpg", "png");
	
	private static final List<String> AUDIO_SUFFIXES = Arrays.asList("mp3");
	
	public static MediaType mediaType(String cellValue) {
		for (String suffix : IMAGE_SUFFIXES) {
			if(cellValue.toLowerCase().endsWith(suffix)) {
				return MediaType.Image;
			}
		}
		
		for (String suffix : AUDIO_SUFFIXES) {
			if(cellValue.toLowerCase().endsWith(suffix)) {
				return MediaType.Audio;
			}
		}
		
		return MediaType.Text;
	}
	
	public class ExamData {
		private String name;
		private ExamLevel level;
		private List<ExcelOneLine> lines;
		
		private String mediaDir;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public ExamLevel getLevel() {
			return level;
		}
		public void setLevel(ExamLevel level) {
			this.level = level;
		}
		public List<ExcelOneLine> getLines() {
			return lines;
		}
		public void setLines(List<ExcelOneLine> lines) {
			this.lines = lines;
		}
		
		public String getMediaDir() {
			return mediaDir;
		}
		public void setMediaDir(String mediaDir) {
			this.mediaDir = mediaDir;
		}
		
	}
	
	public class ExcelOneLine {
		private Integer questionNum;
		private String title;
		private String optionA;
		private String optionB;
		private String optionC;
		private String optionD;
		private String rightOption;
		private String optionsMode;
		
		private OptionsMode mode;

		public Integer getQuestionNum() {
			return questionNum;
		}

		public void setQuestionNum(Integer questionNum) {
			this.questionNum = questionNum;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getOptionA() {
			return optionA;
		}

		public void setOptionA(String optionA) {
			this.optionA = optionA;
		}

		public String getOptionB() {
			return optionB;
		}

		public void setOptionB(String optionB) {
			this.optionB = optionB;
		}

		public String getOptionC() {
			return optionC;
		}

		public void setOptionC(String optionC) {
			this.optionC = optionC;
		}

		public String getOptionD() {
			return optionD;
		}

		public void setOptionD(String optionD) {
			this.optionD = optionD;
		}

		public String getRightOption() {
			return rightOption;
		}

		public void setRightOption(String rightOption) {
			this.rightOption = rightOption;
		}
		
		public String getOptionsMode() {
			return optionsMode;
		}
		
		public void setOptionsMode(String optionsMode) {
			this.optionsMode = optionsMode;
		}
		
		public OptionsMode getMode() {
			return mode;
		}
		
		public void setMode(OptionsMode mode) {
			this.mode = mode;
		}
		
		@Override
		public String toString() {
			return DoradoMapperUtils.toJSON(this);
		}
		
	}
	
	
	public class ExamImportResult {
		int examId;
		int questionCnt;

		public int getExamId() {
			return examId;
		}
		
		public void setExamId(int examId) {
			this.examId = examId;
		}
		
		public int getQuestionCnt() {
			return questionCnt;
		}

		public void setQuestionCnt(int questionCnt) {
			this.questionCnt = questionCnt;
		}
	}
	
	@Test
	public void test() {
		ExamImportService bean = DoradoBeanFactory.getBean(ExamImportService.class);
		bean.importExam("测试导入试卷", ExamLevel.level1, new File("/data/exam-import/初级测试试卷-level1.zip"), new File("/data/exam-import/初级测试试卷-level1.xlsx"));
		
	}
}
