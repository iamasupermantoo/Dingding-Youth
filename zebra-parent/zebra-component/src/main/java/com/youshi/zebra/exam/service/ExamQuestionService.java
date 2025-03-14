package com.youshi.zebra.exam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.exam.dao.ExamQuestionDAO;
import com.youshi.zebra.exam.model.ExamQuestionModel;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class ExamQuestionService extends AbstractService<Integer, ExamQuestionModel>{
	private static final Logger logger = LoggerFactory.getLogger(ExamQuestionService.class);
	
	@Autowired
	private ExamQuestionDAO examQuestionDAO;

	@Override
	public AbstractDAO<Integer, ExamQuestionModel> dao() {
		return examQuestionDAO;
	}
}