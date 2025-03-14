package com.youshi.zebra.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.youshi.zebra.order.dao.ProductCostDAO;
import com.youshi.zebra.order.model.ProductCostModel;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class ProductCostService extends AbstractService<Integer, ProductCostModel>{
	private static final Logger logger = LoggerFactory.getLogger(ProductCostService.class);
	
	@Autowired
	private ProductCostDAO productCostDAO;

	@Override
	public AbstractDAO<Integer, ProductCostModel> dao() {
		return productCostDAO;
	}
}