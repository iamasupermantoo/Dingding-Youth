package com.youshi.zebra.homework.model;

import java.util.ArrayList;
import java.util.List;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.audio.model.AudioModel;
import com.youshi.zebra.homework.constants.AnswerType;
import com.youshi.zebra.image.model.ImageModel;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public class HomeworkAnswerModel extends AbstractModel<Integer> {
	public enum HomeworkAnswerKeys {
		homework_id,
		type,
		
		author,
		audio_len,
	}
	
	private int homeworkId;
	private int type;
	private String content;

	public HomeworkAnswerModel(int id, String data, long createTime, int status, 
			int homeworkId, int type, String content) {
		super(id, data, createTime, status);
		this.type = type;
		this.homeworkId = homeworkId;
		this.content = content;
	}
	
	public int getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(int homeworkId) {
		this.homeworkId = homeworkId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Integer getAudioId() {
		if(getType() == AnswerType.audio.getValue()) {
			return Integer.parseInt(getContent());
		}
		return null;
	}
	
	public List<Integer> getImageIds() {
		if(getType() == AnswerType.image.getValue()) {
			return DoradoMapperUtils.fromJSON(getContent(), ArrayList.class, Integer.class);
		}
		return null;
	}
	
	// ---------------------------------------- audio and images ----------------------------------------
	// notes：要求上层调用者，调用setter注入audio和images属性
	private AudioModel audio;
	
	private List<ImageModel> images;

	public AudioModel getAudio() {
		return audio;
	}

	public void setAudio(AudioModel audio) {
		this.audio = audio;
	}

	public List<ImageModel> getImages() {
		return images;
	}

	public void setImages(List<ImageModel> images) {
		this.images = images;
	}
}
