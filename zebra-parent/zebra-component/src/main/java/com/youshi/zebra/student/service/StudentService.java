package com.youshi.zebra.student.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dorado.framework.crud.dao.WhereClause;
import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.model.PageView;
import com.youshi.zebra.student.model.StudentModel;
import com.youshi.zebra.student.model.StudentModel.StudentKeys;
import com.youshi.zebra.user.constant.UserStatus;
import com.youshi.zebra.user.constant.UserType;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.model.UserModel.UserKey;
import com.youshi.zebra.user.service.UserService;

/**
 * 
 * @author wangsch
 * @date 2017年2月13日
 */
@Service
public class StudentService extends AbstractService<Integer, StudentModel> {
	private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
	
	@Autowired
	private UserService userService;
	
	// TODO 根据具体查询条件，也许需要查询student表
	public PageView<UserModel, HasUuid<Integer>> queryStudents(Integer sid, UserStatus status,
			Integer cursor, Integer limit) {
		WhereClause params = WhereClause.create().and().eq(UserKey.type, UserType.Student.getValue());
		if(sid != null) {
			params.and().eq(StudentKeys.id, sid);
		}
		if(status != null) {
			params.and().eq(StudentKeys.status, status.getValue());
		}
		PageView<UserModel, HasUuid<Integer>> page = userService.getByCursor(cursor, limit, params);
		
		return page;
	}
	
	
	@Override
	protected AbstractDAO<Integer, StudentModel> dao() {
		throw new UnsupportedOperationException("Not implement yet");
	}
}
