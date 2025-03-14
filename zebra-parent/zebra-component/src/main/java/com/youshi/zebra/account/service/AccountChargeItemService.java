package com.youshi.zebra.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.account.dao.AccountChargeItemDAO;
import com.youshi.zebra.account.model.AccountChargeItemModel;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class AccountChargeItemService extends AbstractService<Integer, AccountChargeItemModel>{
	private static final Logger logger = LoggerFactory.getLogger(AccountChargeItemService.class);
	
	@Autowired
	private AccountChargeItemDAO accountChargeItemDAO;

	@Override
	public AbstractDAO<Integer, AccountChargeItemModel> dao() {
		return accountChargeItemDAO;
	}
}