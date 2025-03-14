package com.youshi.zebra.book.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

import com.dorado.framework.utils.DoradoMapperUtils;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.youshi.zebra.core.utils.ExcelUtils;
import com.youshi.zebra.exception.base.DoradoRuntimeException;
import com.youshi.zebra.media.service.MediaService;

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
public class BookImportService {
	private static final Logger logger = LoggerFactory.getLogger(BookImportService.class);
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private FrameConfigService frameConfigService;
	
	public BookImportResult importBook(String name, MultipartFile zipUploadFIle, MultipartFile excelUploadFile) {
		// 1. 准备数据：解析压缩包和excel文件
		BookData bookData = parseBookData(name, zipUploadFIle, excelUploadFile);
		
		// 2. 创建book->创建chapter->创建frames config
		BookImportResult result = createBook(bookData);
		
		logger.info("Import book succ. result: {}", DoradoMapperUtils.toJSON(result));
		
		return result;
	}
	
	public BookImportResult importBook(String name, File zipUploadFIle, File excelUploadFile) {
		// 1. 准备数据：解析压缩包和excel文件
		BookData bookData = parseBookData(name, zipUploadFIle, excelUploadFile);
		
		// 2. 创建book->创建chapter->创建frames config
		BookImportResult result = createBook(bookData);
		
		logger.info("Import book succ. result: {}", DoradoMapperUtils.toJSON(result));
		
		return result;
	}
	
	
	
	
	private BookImportResult createBook(BookData book) {
		Integer userId = WebRequestContext.getUserId();
		int bookId = bookService.create(book.getName(), book.getTotalCnt(), book.getDesc());
		
		int chapterCount = 0;
		int framesCount = 0;
		for (ChapterData chapter : book.getChapters()) {
			int chapterId = chapterService.create(bookId, chapter.getCnt(), chapter.getLabel(), 
					chapter.getDesc(), chapter.getHomeworkTitle(), chapter.getHomeworkContent());
			chapterCount++;
			
			List<ChapterFrameData> frames = chapter.getFrames();
			for (ChapterFrameData frame : frames) {
				Integer mediaId = mediaService.createMedia(userId, 
						StringUtils.EMPTY, StringUtils.EMPTY, frame.getMediaFile());
				frameConfigService.addFrame(chapterId, mediaId, frame.getParentIdx());
				framesCount++;
			}
		}
		
		BookImportResult result = new BookImportResult();
		result.setBookId(bookId);
		result.setChapterCount(chapterCount);
		result.setFramesCount(framesCount);
		
		return result;
	}
	
	private BookData parseBookData(String name, 
			MultipartFile zipUploadFile, MultipartFile excelUploadFile) {
		String uuid = UUID.randomUUID().toString();
		try {
			File zipFile = new File("/tmp/" + uuid + ".zip");
			zipUploadFile.transferTo(zipFile);
			
			File excelFile = new File("/tmp/" + uuid + "." + FilenameUtils.getExtension(excelUploadFile.getOriginalFilename()));
			FileUtils.writeByteArrayToFile(excelFile, excelUploadFile.getBytes());
			
			return parseBookData(name, zipFile, excelFile);
		} catch (Exception e) {
			throw new DoradoRuntimeException();
		}
	}
	
	
	private BookData parseBookData(String name, File zipFile, File excelFile) {
		// 压缩包
		String unzipDest = null;
		try {
			String timestamp = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss");
			unzipDest = "/tmp/book_import_" + zipFile.getName() + "_" + timestamp;
			
			unzip(zipFile.getAbsolutePath(), unzipDest, null);
		} catch (Exception e) {
			throw new DoradoRuntimeException(e);
		}
		
		unzipDest = unzipDest + "/" + zipFile.getName().replace(".zip", "");
		
		File unzipDestDir = new File(unzipDest);
		File[] mediaDirs = unzipDestDir.listFiles();
		Map<Integer, List<File>> lessonMedias = new HashMap<>(); 
		for (File mediaDir : mediaDirs) {
			boolean isDir = mediaDir.isDirectory();
			if(!isDir) {
				continue;
			}
			
			int lessonCnt = Integer.parseInt(mediaDir.getName().replace("lesson", ""));
			File[] mediaFiles = mediaDir.listFiles();
			Arrays.sort(mediaFiles, (o1, o2)->{
				return o1.getName().compareTo(o2.getName());
			});
			
			for (File mediaFile : mediaFiles) {
				List<File> medias = lessonMedias.computeIfAbsent(lessonCnt, ArrayList::new);
				medias.add(mediaFile);
			}
		}
		
		// excel
		List<ChapterData> chapters = new ArrayList<>();
		try {
			Workbook workbook = ExcelUtils.createWorkbook(FileUtils.readFileToByteArray(excelFile), 
					FilenameUtils.getExtension(excelFile.getName()));
			Sheet sheet = workbook.getSheetAt(0);
			ExcelUtils.shiftEmptyRows(sheet);
			
			/*
			 * 书名	    lesson	lesson名	    作业名	     作业内容
			   幼儿园1	1	    入门	        lesson1作业	 与爸爸妈妈说Hello
			   幼儿园1	2	    入门	        lesson2作业	 练习A和a，认识apple和ant
			 */
			Iterator<Row> it = sheet.rowIterator();
			it.next();
			
			while(it.hasNext()) {
				Row row = it.next();
				Cell cell = row.getCell(1);
				int lessonCnt = ExcelUtils.getInteger(cell);
				
				ChapterData chapter = new ChapterData();
				chapter.setCnt(lessonCnt);
				chapter.setLabel("Lesson-" + lessonCnt);
				
				cell = row.getCell(3);
				String homeworkTitle = ExcelUtils.getCellStr(cell);
				chapter.setHomeworkTitle(homeworkTitle);
				
				cell = row.getCell(4);
				String homeworkContent = ExcelUtils.getCellStr(cell);
				chapter.setHomeworkContent(homeworkContent);
				
//				chapter.setDesc(desc);
				
				List<ChapterFrameData> frames = new ArrayList<>();
				chapter.setFrames(frames);
				
				List<File> mediaFiles = lessonMedias.get(lessonCnt);
				for (File mediaFile : mediaFiles) {
					ChapterFrameData frame = new ChapterFrameData();
					frames.add(frame);
					frame.setMediaFile(mediaFile);
					frame.setParentIdx(null);
				}
				
				chapters.add(chapter);
			}
		} catch(Exception e) {
			throw new DoradoRuntimeException(e);
		}
		
		BookData book = new BookData();
		book.setName(name);
		book.setDesc("");
		book.setTotalCnt(chapters.size());
		book.setChapters(chapters);
		
		return book;
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
	
	public class BookData {
		private String name;
		private int totalCnt;
		private String desc;
		
		private List<ChapterData> chapters;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getTotalCnt() {
			return totalCnt;
		}

		public void setTotalCnt(int totalCnt) {
			this.totalCnt = totalCnt;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public List<ChapterData> getChapters() {
			return chapters;
		}

		public void setChapters(List<ChapterData> chapters) {
			this.chapters = chapters;
		}
	}
	
	public class ChapterData {
		private int cnt;
		private String label;
		/**
		 * 备注，可选
		 */
		private String desc;
		private String homeworkTitle;
		private String homeworkContent;
		
		private List<ChapterFrameData> frames;

		public int getCnt() {
			return cnt;
		}

		public void setCnt(int cnt) {
			this.cnt = cnt;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getHomeworkTitle() {
			return homeworkTitle;
		}

		public void setHomeworkTitle(String homeworkTitle) {
			this.homeworkTitle = homeworkTitle;
		}

		public String getHomeworkContent() {
			return homeworkContent;
		}

		public void setHomeworkContent(String homeworkContent) {
			this.homeworkContent = homeworkContent;
		}

		public List<ChapterFrameData> getFrames() {
			return frames;
		}

		public void setFrames(List<ChapterFrameData> frames) {
			this.frames = frames;
		}
	}
	
	public class ChapterFrameData {
		private File mediaFile;
		
		private Integer parentIdx;

		public File getMediaFile() {
			return mediaFile;
		}

		public void setMediaFile(File mediaFile) {
			this.mediaFile = mediaFile;
		}

		public Integer getParentIdx() {
			return parentIdx;
		}

		public void setParentIdx(Integer parentIdx) {
			this.parentIdx = parentIdx;
		}
	}
	
	public class BookImportResult {
		private int bookId;
		
		private int chapterCount;
		
		private int framesCount;

		public int getBookId() {
			return bookId;
		}

		public void setBookId(int bookId) {
			this.bookId = bookId;
		}

		public int getChapterCount() {
			return chapterCount;
		}

		public void setChapterCount(int chapterCount) {
			this.chapterCount = chapterCount;
		}

		public int getFramesCount() {
			return framesCount;
		}

		public void setFramesCount(int framesCount) {
			this.framesCount = framesCount;
		}
	}
	
	@Test
	public void test() {
		File[] mediaFiles = new File("/tmp/bookimp/book1/lesson14").listFiles();
		
		System.out.println("---------------------------- before ----------------------------");
		for (File file : mediaFiles) {
			System.out.println(file.getName());
		}
		
		Arrays.sort(mediaFiles, (o1, o2)->{
			return o1.getName().compareTo(o2.getName());
		});
		
		System.out.println("---------------------------- after ----------------------------");
		for (File file : mediaFiles) {
			System.out.println(file.getName());
		}
		
	}
}
