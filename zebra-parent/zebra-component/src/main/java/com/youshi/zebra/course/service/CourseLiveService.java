package com.youshi.zebra.course.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.course.dao.LiveDAO;
import com.youshi.zebra.course.model.LiveModel;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class CourseLiveService extends AbstractService<Integer, LiveModel>{
	private static final Logger logger = LoggerFactory.getLogger(CourseLiveService.class);
	
	@Autowired
	private LiveDAO liveDAO;

	@Override
	public AbstractDAO<Integer, LiveModel> dao() {
		return liveDAO;
	}
	
	
	
	
}