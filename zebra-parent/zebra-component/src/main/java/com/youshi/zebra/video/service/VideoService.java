package com.youshi.zebra.video.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.video.dao.VideoDAO;
import com.youshi.zebra.video.model.VideoModel;

/**
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
@Service
public class VideoService extends AbstractService<Integer, VideoModel>{
	@Autowired
	private VideoDAO videoDAO;
	
	@Override
	protected AbstractDAO<Integer, VideoModel> dao() {
		return videoDAO;
	}

	public Integer uploadMp4Video(Integer loginAdminId, byte[] bytes, String extension) {
		throw new UnsupportedOperationException("Not implements yet");
	}
	
}
