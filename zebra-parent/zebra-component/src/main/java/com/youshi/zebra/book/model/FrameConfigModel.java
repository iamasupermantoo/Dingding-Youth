package com.youshi.zebra.book.model;

import com.dorado.framework.crud.model.impl.AbstractModel;

/**
 * 
 * 每个章节，对应“帧”配置。在这个章节中附带的教学材料，用图片+音频+视频组成了类似“幻灯片”播放的效果
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
public class FrameConfigModel extends AbstractModel<Integer>{
	public enum ChapterKeys {
		/** 来自chapter表主键id */
		id,
		
		/**  */
		frame_content,
	}
	
	private String frameContent;

	public FrameConfigModel(Integer id, String data, long createTime, int status,
			String frameContent) {
		super(id, data, createTime, status);
		this.frameContent = frameContent;
	}

	public String getFrameContent() {
		return frameContent;
	}
}
