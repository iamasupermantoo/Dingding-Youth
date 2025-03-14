package com.youshi.zebra.account.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasData;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.account.dao.AccountChargeDAO;
import com.youshi.zebra.account.dao.AccountChargeDAO.AccountChargeStatus;
import com.youshi.zebra.account.dao.AccountChargeItemDAO.AccountChargeItemStatus;
import com.youshi.zebra.account.dao.AccountChargeItemDAO;
import com.youshi.zebra.account.dao.AccountCostDAO;
import com.youshi.zebra.account.dao.AccountCostDAO.AccountCostStatus;
import com.youshi.zebra.account.dao.AccountDAO;
import com.youshi.zebra.account.model.AccountChargeItemModel;
import com.youshi.zebra.account.model.AccountChargeItemModel.AccountChargeItemKeys;
import com.youshi.zebra.account.model.AccountModel;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.course.service.LiveMetaService;
import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.model.ProductCostModel;
import com.youshi.zebra.order.model.ProductPriceModel;
import com.youshi.zebra.order.service.OrderHandler;
import com.youshi.zebra.order.service.ProductService;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class AccountService extends AbstractService<Integer, AccountModel>{
	private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private AccountChargeDAO accountChargeDAO;
	
	@Autowired
	private AccountCostDAO accountCostDAO;
	
	@Autowired
	private AccountChargeItemDAO accountChargeItemDAO;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderHandler orderHandler;
	
	@Autowired
	private LiveMetaService liveMetaService;
	
	// ------------------------------------------------ 用户账户------------------------------------------------------------
	public Integer getUserTotalAmount(Integer userId) {
		AccountModel account = accountDAO.getByUserId(userId);
		if(account == null) {
			return 0;
		}
		
		return account.getTotalAmount();
	}
	
	// ------------------------------------------------ 充值项------------------------------------------------------------
	public List<AccountChargeItemModel> getChargeItems() {
		int limit = 100;
		WhereClause params = WhereClause.create()
				.and().eq(AccountChargeItemKeys.status, AccountChargeItemStatus.Normal.getValue())
				;
		List<AccountChargeItemModel> result = accountChargeItemDAO.getByCursor(null, limit, params);
		
		// 注入价格，OPTI 这块可以用localcache，而不是反回AccountChargeItemModel
		for (AccountChargeItemModel item : result) {
			ProductPriceModel price = productService.getProductPrice(item.getId(), ProductType.CHARGE);
			item.setPrice(price.getPrice());
			item.setProductPrice(price);
		}
		
		return result;
	}
	
	public AccountChargeItemModel getChargeItem(int chargeItemId) {
		return accountChargeItemDAO.getById(chargeItemId);
	}
	
	// ------------------------------------------------ 充值、减余额------------------------------------------------------------
	public void charge(int userId, int chargeAmount) {
		long currentTime = System.currentTimeMillis();
		
		int chargeId = accountChargeDAO.insert(userId, chargeAmount, 
				HasData.EMPTY_DATA, AccountChargeStatus.Normal, currentTime);
		int c = accountDAO.incrTotalAmount(userId, chargeAmount, currentTime);
		DAOUtils.checkInsertDuplicate(c);
		
		logger.info("Charge succ. userId: {}, chargeAmount: {}, chargeId: {}", 
				userId, chargeAmount, chargeId);
	}
	
	public void cost(Integer userId, String productUuid, Integer type) {
		ProductType productType = ProductType.fromValue(type);
		ProductCostModel productCost = productService.getProductCost(productUuid, productType);
		
		cost(userId, productCost.getCost());
		
		orderHandler.costSucc(userId, productUuid, type);
		logger.info("Deal cost succ. userId: {}", userId);
	}
	
	public void cost(int userId, int costAmount) {
		long currentTime = System.currentTimeMillis();
		
		int costId = accountCostDAO.insert(userId, costAmount, 
				HasData.EMPTY_DATA, AccountCostStatus.Normal, currentTime);
		int c = accountDAO.decrTotalAmount(userId, costAmount, currentTime);
		DAOUtils.checkAffectRows(c);
		
		logger.info("Cost succ. userId: {}, costAmount: {}, costId: {}", 
				userId, costAmount, costId);
	}
	
	@Override
	public AbstractDAO<Integer, AccountModel> dao() {
		return accountDAO;
	}
}