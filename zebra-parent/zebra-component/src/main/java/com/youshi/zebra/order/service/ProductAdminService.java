package com.youshi.zebra.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.model.HasData;
import com.youshi.zebra.account.dao.AccountChargeItemDAO;
import com.youshi.zebra.account.dao.AccountChargeItemDAO.AccountChargeItemStatus;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.dao.ProductCostDAO;
import com.youshi.zebra.order.dao.ProductCostDAO.ProductCostStatus;
import com.youshi.zebra.order.dao.ProductPriceDAO;
import com.youshi.zebra.order.dao.ProductPriceDAO.ProductPriceStatus;

/**
 * 
 * @author wangsch
 * @date 2017年11月9日
 */
@Service
public class ProductAdminService {
	private static final Logger logger = LoggerFactory.getLogger(ProductAdminService.class);
	
	@Autowired
	private ProductPriceDAO productPriceDAO;
	
	@Autowired
	private ProductCostDAO productCostDAO;
	
	@Autowired
	private AccountChargeItemDAO accountChargeItemDAO;
	
	@Autowired
	private ProductService productService;
	
	public void createChargeItem(String appleProductId, 
			Integer price, Integer originalPrice, Integer plusAmount) {
		// charge item
		int chargeItemId = accountChargeItemDAO.insert(plusAmount, 
				HasData.EMPTY_DATA, AccountChargeItemStatus.Normal, System.currentTimeMillis());
		
		// 商品价格信息
		price = price == null ? originalPrice : price;
		Long updateTime = null;
		int id = productPriceDAO.insert(chargeItemId, appleProductId, 
				price, originalPrice, ProductType.CHARGE.getValue(), updateTime, 
				HasData.EMPTY_DATA, ProductPriceStatus.Normal, System.currentTimeMillis());
		logger.info("Create Charge Item succ. id: {}", id);
	}

	public void removeChargeItem(Integer userId, Integer chargeItemId) {
		int c = accountChargeItemDAO.setStatus(chargeItemId, AccountChargeItemStatus.AdminDel);
		DAOUtils.checkAffectRows(c);
		
		logger.info("Remove charge item succ. userId: {}, chargeItemId: {}", userId, chargeItemId);
	}
	
	public void createProductPrice(String productUuid, int type, String appleProductId, 
			Integer price, Integer originalPrice) {
		Long updateTime = null;
		ProductType productType = ProductType.fromValue(type);
		Integer productId = ProductService.parseProductId(productUuid, ProductType.fromValue(type));
		
		int id = productPriceDAO.insert(productId, appleProductId, 
				price, originalPrice, productType.getValue(), updateTime, 
				HasData.EMPTY_DATA, ProductPriceStatus.Normal, System.currentTimeMillis());
		logger.info("Create product price succ. id: {}", id);
	}
	
	
	public void createProductCost(
			Integer cost, Integer originalCost, String productUuid, int type) {
		ProductType productType = ProductType.fromValue(type);
		Integer productId = ProductService.parseProductId(productUuid, ProductType.fromValue(type));
		doCreateProductCost(cost, originalCost, productType, productId);
	}
	
	/**
	 * 创建商品价格，价格就是消耗的余额数（仅在余额支付时使用）
	 * 
	 * @param cost				实际消耗的余额数（打折时，会比{@code originalCost}小），打折价
	 * @param originalCost		应该消耗的余额数，原价
	 * @param productType		商品类型{@link ProductType}
	 * @param productId			商品主键id
	 */
	public void doCreateProductCost(Integer cost, Integer originalCost, ProductType productType,
			Integer productId) {
		Long updateTime = null;
		
		int id = productCostDAO.insert(productId, 
				cost, originalCost, productType.getValue(), updateTime, 
				HasData.EMPTY_DATA, ProductCostStatus.Normal, System.currentTimeMillis());
		logger.info("Create product cost succ. id: {}", id);
	}
	
	public void updateProductCost(Integer cost, Integer originalCost, String productUuid, int type) {
		ProductType productType = ProductType.fromValue(type);
		Integer productId = ProductService.parseProductId(productUuid, ProductType.fromValue(type));
		
		doUpdateProductCost(cost, originalCost, productType, productId);
	}
	
	/**
	 * 更新商品价格，价格就是消耗的余额数（仅在余额支付时使用）
	 * 
	 * @param cost				实际消耗的余额数（打折时，会比{@code originalCost}小），打折价
	 * @param originalCost		应该消耗的余额数，原价
	 * @param productType		商品类型{@link ProductType}
	 * @param productId			商品主键id
	 */
	public void doUpdateProductCost(Integer cost, Integer originalCost, ProductType productType,
			Integer productId) {
		Long updateTime = System.currentTimeMillis();
		
		int id = productCostDAO.update(productId, 
				cost, originalCost, productType.getValue(), updateTime);
		logger.info("Update product cost succ. id: {}", id);
	}
}
