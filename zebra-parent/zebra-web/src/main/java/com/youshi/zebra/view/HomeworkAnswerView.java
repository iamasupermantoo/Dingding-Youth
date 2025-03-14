package com.youshi.zebra.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.youshi.zebra.audio.utils.AudioUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.homework.constants.AnswerType;
import com.youshi.zebra.homework.model.HomeworkAnswerModel;
import com.youshi.zebra.image.model.ImageModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 一条答案View
 * 
 * @author wangsch
 * @date 2017年2月6日
 */
public class HomeworkAnswerView {
	/**
	 * 答案实体
	 */
	private HomeworkAnswerModel delegate;

	public HomeworkAnswerView(HomeworkAnswerModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		
	}

	public String getId() {
		return delegate.getUuid();
	}

	@ApiModelProperty(value = "类型，0:文本，1:图片，2:语音")
	public Integer getType() {
		return delegate.getType();
	}

	
	@ApiModelProperty(value = "文本，type时0时返回")
	public String getText() {
		if(getType() != AnswerType.text.getValue()) {
			return null;
		}
		return delegate.getContent();
	}
	
	public List<ImageView> getImages() {
		if(getType() != AnswerType.image.getValue()) {
			return null;
		}
		List<ImageModel> images = delegate.getImages();
		ArrayList<ImageView> imageViews = new ArrayList<>();
		for (ImageModel image : images) {
			imageViews.add(new ImageView(image));
		}
		
		return imageViews;
	}
	
	public String getAudio() {
		if(getType() != AnswerType.audio.getValue()) {
			return null;
		}
		return AudioUtils.getUrl(delegate.getAudio());
//		return "http://lamb-resource.oss-cn-shanghai.aliyuncs.com/18f016fc68834f16beec5e199fb8e5b6.mp3";
	}
	
	
	@ApiModelProperty(value = "音频的长度，单位秒，type是2时返回")
	public Integer getAudioLength() {
		if(getType() != AnswerType.audio.getValue()) {
			return null;
		}
		return delegate.getAudio().getLength();
	}

	@ApiModelProperty(value = "提交时间", required=true)
	public String getTime() {
		return DateFormatUtils.format(delegate.getCreateTime(), "MM-dd HH:mm");
	}

}
