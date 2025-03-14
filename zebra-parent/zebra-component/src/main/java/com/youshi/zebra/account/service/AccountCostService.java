package com.youshi.zebra.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.account.dao.AccountCostDAO;
import com.youshi.zebra.account.model.AccountCostModel;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class AccountCostService extends AbstractService<Integer, AccountCostModel>{
	private static final Logger logger = LoggerFactory.getLogger(AccountCostService.class);
	
	@Autowired
	private AccountCostDAO accountCostDAO;

	@Override
	public AbstractDAO<Integer, AccountCostModel> dao() {
		return accountCostDAO;
	}
}