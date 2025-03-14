package ${basePackage}.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import ${basePackage}.dao.${dao};
import ${basePackage}.model.${model};


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class ${service} extends AbstractService<Integer, ${model}>{
	private static final Logger logger = LoggerFactory.getLogger(${service}.class);
	
	@Autowired
	private ${dao} ${dao?uncap_first};

	@Override
	public AbstractDAO<Integer, ${model}> dao() {
		return ${dao?uncap_first};
	}
}