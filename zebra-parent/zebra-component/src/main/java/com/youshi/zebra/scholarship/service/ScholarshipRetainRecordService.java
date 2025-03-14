package com.youshi.zebra.scholarship.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.scholarship.dao.ScholarshipRetainRecordDAO;
import com.youshi.zebra.scholarship.model.ScholarshipRetainRecordModel;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class ScholarshipRetainRecordService extends AbstractService<Integer, ScholarshipRetainRecordModel>{
	private static final Logger logger = LoggerFactory.getLogger(ScholarshipRetainRecordService.class);
	
	@Autowired
	private ScholarshipRetainRecordDAO scholarshipRetainRecordDAO;

	@Override
	public AbstractDAO<Integer, ScholarshipRetainRecordModel> dao() {
		return scholarshipRetainRecordDAO;
	}
}