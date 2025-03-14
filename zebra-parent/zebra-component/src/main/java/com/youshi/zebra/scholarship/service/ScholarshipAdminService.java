package com.youshi.zebra.scholarship.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.scholarship.dao.ScholarshipDAO;
import com.youshi.zebra.scholarship.dao.ScholarshipRetainRecordDAO;
import com.youshi.zebra.scholarship.dao.ScholarshipRetainRecordDAO.ScholarshipRetainRecordStatus;
import com.youshi.zebra.scholarship.model.ScholarshipModel;
import com.youshi.zebra.scholarship.model.ScholarshipRetainRecordModel;
import com.youshi.zebra.scholarship.model.ScholarshipRetainRecordModel.ScholarshipRetainRecordKeys;
import com.youshi.zebra.user.service.UserService;


/**
 * 
 * 
 * @author codegen
 * 
 */
@Service
public class ScholarshipAdminService {
	private static final Logger logger = LoggerFactory.getLogger(ScholarshipAdminService.class);
	
	@Autowired
	private ScholarshipDAO scholarshipDAO;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ScholarshipRetainRecordService ScholarshipRetainRecordService;
	
	@Autowired
	private ScholarshipRetainRecordDAO scholarshipRetainRecordDAO;
	
	public void retainPass(Integer recordId) {
		int c = scholarshipRetainRecordDAO.setStatus(recordId, ScholarshipRetainRecordStatus.SUCC);
		DAOUtils.checkAffectRows(c);
		logger.info("Retain pass succ. recordId: {}", recordId);
	}
	
	public void retainUnpass(Integer recordId) {
		int c = scholarshipRetainRecordDAO.setStatus(recordId, ScholarshipRetainRecordStatus.FAIL);
		DAOUtils.checkAffectRows(c);
		
		// 把奖学金加回来
		ScholarshipRetainRecordModel record = scholarshipRetainRecordDAO.getById(recordId);
		int userId = record.getUserId();
		int c2 = scholarshipDAO.incrTotalAmount(userId, record.getApplyAmount(), System.currentTimeMillis());
		DAOUtils.checkInsertDuplicate(c2);
		
		logger.info("Retain pass fail. userId: {}, amount: {}, recordId: {}", 
				userId, record.getApplyAmount(), recordId);
	}
	
	public PageView<ScholarshipRetainRecordModel, HasUuid<Integer>> getRecords(ScholarshipRetainRecordStatus status, 
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create()
				;
		if(status != null) {
			params.and().eq(ScholarshipRetainRecordKeys.status, status.getValue());
		}
		
		PageView<ScholarshipRetainRecordModel, HasUuid<Integer>> page = ScholarshipRetainRecordService
				.getByCursor(cursor, limit, params);
		
		// 注入ScholarshipModel，OPTI getByUserIds减少 DB请求次数
		for (ScholarshipRetainRecordModel record : page.getList()) {
			ScholarshipModel scholarship = scholarshipDAO.getByUserId(record.getUserId());
			record.setScholarship(scholarship);
		}
		
		return page;
	}
}