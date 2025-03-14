package com.youshi.zebra.homework.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dorado.framework.crud.model.HasData;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.audio.AudioService;
import com.youshi.zebra.audio.exception.AudioUploadException;
import com.youshi.zebra.audio.model.AudioModel;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.exception.common.ForbiddenException;
import com.youshi.zebra.homework.constants.AnswerType;
import com.youshi.zebra.homework.constants.HomeworkAnswerStatus;
import com.youshi.zebra.homework.constants.HomeworkStatus;
import com.youshi.zebra.homework.dao.HomeworkAnswerDAO;
import com.youshi.zebra.homework.exception.HomeworkAnswerModifyFailException;
import com.youshi.zebra.homework.model.HomeworkAnswerModel;
import com.youshi.zebra.homework.model.HomeworkModel;
import com.youshi.zebra.image.exception.ImageUploadException;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.service.ImageService;
import com.youshi.zebra.lesson.service.LessonService;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
@Service
public class HomeworkAnswerService {
	private static final Logger logger = LoggerFactory.getLogger(HomeworkAnswerService.class);
	
	@Autowired
	private HomeworkAnswerDAO homeworkAnswerDAO;
	
	@Autowired
	private HomeworkService homeworkService;
	
	@Autowired
	private AudioService audioService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private LessonService lessonService;
	
	
	/**
	 * 查询作业答案 list
	 * 
	 * @param homeworkId 作业id
	 * 
	 * @return {@link HomeworkAnswerModel} list
	 * 
	 */
	public List<HomeworkAnswerModel> getHomeworkAnswers(Integer homeworkId) {
		List<HomeworkAnswerModel> answers = homeworkAnswerDAO.getNormalAnswers(homeworkId);
		
		// 注入图片、语音
		injectMultimedia(answers);
		
		return answers;
	}
	
	public HomeworkAnswerModel getHomeworkAnswer(Integer homeworkId, Integer answerId) {
		HomeworkAnswerModel answer = homeworkAnswerDAO.getOneAnswer(homeworkId, answerId);
		injectMultimedia(answer);
		return answer;
		
	}
	
	/**
	 * 添加一条答案
	 * 
	 * @param studentId
	 * @param homeworkId
	 * @param type
	 * @param text
	 * @param images
	 * @param audio
	 * 
	 * @return
	 */
	public HomeworkAnswerModel addAnswer(Integer studentId, Integer homeworkId, Integer type, String text,
			MultipartFile[] images, MultipartFile audio) {
		HomeworkModel homework = homeworkService.getById(homeworkId);
		checkModifyAnswer(homework);
		
		if(homework.getStudentId() != studentId) {
			logger.error("It's not your homework. sid: {}, homework.id: {}, homework.sid: {}", 
					studentId, homework.getId(), homework.getStudentId());
			throw new ForbiddenException();
		}
		
		AnswerType answerType = AnswerType.fromValue(type);
		
		String content = null;
		switch(answerType) {
			case text:
				content = text;
				break;
			case audio:
			Integer audioId = uploadAudio(studentId, audio);
				content = String.valueOf(audioId);
				break;
			case image:
				List<Integer> imageIds = uploadImages(studentId, images);
				content = DoradoMapperUtils.toJSON(imageIds);
				break;
			default:
				throw new IllegalArgumentException();
		}
		
		long createTime = System.currentTimeMillis();
		String data = HasData.EMPTY_DATA;
		HomeworkAnswerStatus status = HomeworkAnswerStatus.Normal;
		int id = homeworkAnswerDAO.insert(homeworkId, content, answerType, data, status, createTime);
		HomeworkAnswerModel result = getHomeworkAnswer(homeworkId, id);
		
		homeworkService.setStatus(studentId, homeworkId, HomeworkStatus.WAIT_CORRECT);
		lessonService.updateHomeworkStatus(homework.getCourseId(), homework.getLessonId(), 
				HomeworkStatus.WAIT_CORRECT);
		
		
		logger.info("Student add answer succ. sid: {}, hid: {}, aid: {}", 
				studentId, homeworkId, id);
		
		return result;
	}
	
	public void removeAnswer(Integer studentId, Integer homeworkId, Integer answerId) {
		HomeworkModel homework = homeworkService.getById(homeworkId);
		checkModifyAnswer(homework);
		
		if(homework.getStudentId() != studentId) {
			throw new ForbiddenException();
		}
		
		int c = homeworkAnswerDAO.setStatus(answerId, HomeworkAnswerStatus.UserDel);
		DAOUtils.checkAffectRows(c);
	}
	
	
	// ------------------------------------- private methods -----------------------------------
	private void injectMultimedia(List<HomeworkAnswerModel> answers) {
		for (HomeworkAnswerModel answer : answers) {
			injectMultimedia(answer);
		}
	}

	/**
	 * @param answer
	 */
	private void injectMultimedia(HomeworkAnswerModel answer) {
		AnswerType type = AnswerType.fromValue(answer.getType());
		switch(type) {
		case image:
			List<ImageModel> images = imageService.getListByIds(answer.getImageIds());
			answer.setImages(images);
			break;
		case audio:
			AudioModel audio = audioService.getById(answer.getAudioId());
			answer.setAudio(audio);
		default:
			break;
		}
	}
	
	/**
	 * @param studentId
	 * @param audio
	 * @return
	 * @throws IOException
	 */
	private Integer uploadAudio(Integer studentId, MultipartFile audio) {
		if(audio == null) {
			throw new AudioUploadException();
		}
		try {
			String filename = audio.getOriginalFilename();
			String fileExt = FilenameUtils.getExtension(filename);
			Integer audioId = audioService.uploadMp3Audio(studentId, audio.getBytes(), fileExt);
			return audioId;
		} catch (IOException e) {
			logger.error("Fail upload audio.", e);
			throw new AudioUploadException();
		}
	}

	/**
	 * @param studentId
	 * @param images
	 * @return
	 */
	private List<Integer> uploadImages(Integer studentId, MultipartFile[] images) {
		if(images== null || images.length ==0) {
			throw new ImageUploadException();
		}
		List<Integer> imageIds = new ArrayList<>(images.length);
		for(MultipartFile image : images) {
			try {
				Integer id = imageService.createImage(studentId, image.getBytes());
				imageIds.add(id);
			} catch (Exception e) {
				throw new ImageUploadException();
			}
		}
		return imageIds;
	}
	


	/**
	 * @param homeworkId
	 * @return
	 */
	private void checkModifyAnswer(HomeworkModel homework) {
		if(HomeworkStatus.WAIT_COMMIT.getValue() != homework.getStatus()
				&& HomeworkStatus.WAIT_CORRECT.getValue() != homework.getStatus()) {
			throw new HomeworkAnswerModifyFailException();
		}
	}
	
}
