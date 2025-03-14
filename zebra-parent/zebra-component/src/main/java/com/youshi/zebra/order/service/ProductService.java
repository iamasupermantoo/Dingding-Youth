package com.youshi.zebra.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.youshi.zebra.account.model.AccountChargeItemModel;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.order.constants.ProductType;
import com.youshi.zebra.order.dao.ProductCostDAO;
import com.youshi.zebra.order.dao.ProductPriceDAO;
import com.youshi.zebra.order.model.ProductCostModel;
import com.youshi.zebra.order.model.ProductPriceModel;

/**
 * 
 * @author wangsch
 * @date 2017年11月8日
 */
@Service
public class ProductService {
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	
	@Autowired
	private ProductPriceDAO productPriceDAO;
	
	@Autowired
	private ProductCostDAO productCostDAO;
	
	public ProductPriceModel getProductPrice(String productUuid, ProductType productType) {
		Integer productId = parseProductId(productUuid, productType);
		return productPriceDAO.getProductPrice(productId, productType);
	}
	
	public ProductCostModel getProductCost(String productUuid, ProductType productType) {
		Integer productId = parseProductId(productUuid, productType);
		return productCostDAO.getProductCost(productId, productType);
	}
	
	
	public ProductPriceModel getProductPrice(Integer productId, ProductType productType) {
		return productPriceDAO.getProductPrice(productId, productType);
	}

	/**
	 * @param productUuid
	 * @param productType
	 * @return
	 */
	public static Integer parseProductId(String productUuid, ProductType productType) {
		Integer productId = null;
		switch (productType) {
		case COURSE:
			productId = UuidUtils.toIntId(CourseMetaModel.class, productUuid);
			break;
		case CHARGE:
			productId = UuidUtils.toIntId(AccountChargeItemModel.class, productUuid);
			break;
		
		case OPEN_COURSE:
			productId = UuidUtils.toIntId(LiveMetaModel.class, productUuid);
			break;
			
		default:
			throw new IllegalArgumentException("Unknown productType: " + productType);
		}
		return productId;
	}
}
