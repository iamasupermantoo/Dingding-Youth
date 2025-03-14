package com.youshi.zebra.scholarship.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.model.PageView;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.core.constants.config.IntConfigKey;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.scholarship.dao.ScholarshipDAO;
import com.youshi.zebra.scholarship.dao.ScholarshipRetainRecordDAO;
import com.youshi.zebra.scholarship.dao.ScholarshipRetainRecordDAO.ScholarshipRetainRecordStatus;
import com.youshi.zebra.scholarship.exception.RetainMinAmountException;
import com.youshi.zebra.scholarship.model.ScholarshipModel;
import com.youshi.zebra.scholarship.model.ScholarshipModel.ScholarshipKeys;
import com.youshi.zebra.scholarship.model.ScholarshipRetainRecordModel;
import com.youshi.zebra.scholarship.model.ScholarshipRetainRecordModel.ScholarshipRetainRecordKeys;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class ScholarshipService extends AbstractService<Integer, ScholarshipModel>{
	private static final Logger logger = LoggerFactory.getLogger(ScholarshipService.class);
	
	@Autowired
	private ScholarshipDAO scholarshipDAO;
	
	@Autowired
	private ScholarshipRetainRecordService ScholarshipRetainRecordService;
	
	@Autowired
	private ScholarshipRetainRecordDAO scholarshipRetainRecordDAO;
	
	public ScholarshipModel getScholarship(Integer userId) {
		ScholarshipModel result = scholarshipDAO.getByUserId(userId);
		
		return result;
	}
	
	public void incrTotalAmount(Integer userId, int amount) {
		int c = scholarshipDAO.incrTotalAmount(userId, amount, System.currentTimeMillis());
		DAOUtils.checkInsertDuplicate(c);
		logger.info("Add user scholarship total amount succ. userId: {}, amount: {}", userId, amount);
	}
	
	public void saveBankInfo(Integer userId, String bankUser, String bankUserMobile, String bankName, String bankCardNum) {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(ScholarshipKeys.bank_user.name(), bankUser);
		dataMap.put(ScholarshipKeys.bank_user_mobile.name(), bankUserMobile);
		dataMap.put(ScholarshipKeys.bank_name.name(), bankName);
		dataMap.put(ScholarshipKeys.bank_card_num.name(), bankCardNum);
		
		String data = DoradoMapperUtils.toJSON(dataMap);
		int c = scholarshipDAO.updateData(userId, data, System.currentTimeMillis());
		DAOUtils.checkInsertDuplicate(c);
		logger.info("User save bank info succ. userId: {}", userId);
	}
	
	public void retainApply(Integer userId) {
		ScholarshipModel scholarship = scholarshipDAO.getByUserId(userId);
		if(scholarship == null || scholarship.getTotalAmount() < IntConfigKey.RetainMinAmount.get()) {
			throw new RetainMinAmountException();
		}
		Integer applyAmount = scholarship.getTotalAmount();
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put(ScholarshipRetainRecordKeys.apply_amount.name(), applyAmount);
		String data = DoradoMapperUtils.toJSON(dataMap);
		
		long time = System.currentTimeMillis();
		int id = scholarshipRetainRecordDAO.insert(userId, data, 
				ScholarshipRetainRecordStatus.WAIT, time);
		
		int c = scholarshipDAO.decrTotalAmount(userId, applyAmount, time);
		DAOUtils.checkAffectRows(c);
		
		logger.info("User retain apply succ. userId: {}, applyAmount: {}, recordId: {}", userId, applyAmount, id);
	}
	
	public PageView<ScholarshipRetainRecordModel, HasUuid<Integer>> getRecords(Integer userId, Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create()
				.and().eq(ScholarshipRetainRecordKeys.user_id, userId)
				;
		PageView<ScholarshipRetainRecordModel, HasUuid<Integer>> page = ScholarshipRetainRecordService
				.getByCursor(cursor, limit, params);
		
		return page;
	}
	
	@Override
	public AbstractDAO<Integer, ScholarshipModel> dao() {
		return scholarshipDAO;
	}
}