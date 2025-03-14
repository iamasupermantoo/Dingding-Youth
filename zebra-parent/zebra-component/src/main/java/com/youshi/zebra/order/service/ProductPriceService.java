package com.youshi.zebra.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.order.dao.ProductPriceDAO;
import com.youshi.zebra.order.model.ProductPriceModel;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class ProductPriceService extends AbstractService<Integer, ProductPriceModel>{
	private static final Logger logger = LoggerFactory.getLogger(ProductPriceService.class);
	
	@Autowired
	private ProductPriceDAO productPriceDAO;

	@Override
	public AbstractDAO<Integer, ProductPriceModel> dao() {
		return productPriceDAO;
	}
}