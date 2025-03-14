package com.youshi.zebra.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.account.dao.AccountChargeDAO;
import com.youshi.zebra.account.model.AccountChargeModel;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class AccountChargeService extends AbstractService<Integer, AccountChargeModel>{
	private static final Logger logger = LoggerFactory.getLogger(AccountChargeService.class);
	
	@Autowired
	private AccountChargeDAO accountChargeDAO;

	@Override
	public AbstractDAO<Integer, AccountChargeModel> dao() {
		return accountChargeDAO;
	}
}