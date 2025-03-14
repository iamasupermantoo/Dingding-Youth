package com.youshi.zebra.counts.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dorado.framework.crud.dao.impl.AbstractDAO;
import com.dorado.framework.crud.service.AbstractService;
import com.dorado.framework.crud.service.RetrieveById;
import com.dorado.framework.utils.DoradoBeanFactory;
import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.core.utils.DAOUtils;
import com.youshi.zebra.counts.dao.UserCountsDAO;
import com.youshi.zebra.counts.model.UserCountsModel;

/**
 * 
 * 用户相关计数，帖子数、阅读数等
 * 
 * @author wangsch
 * @date 		2016-11-06
 * 
 * @see CountsService
 * 
 */
@Controller
public class UserCountsService extends AbstractService<Integer, UserCountsModel>
	implements RetrieveById<Integer, UserCountsModel> {
	
	@Autowired
	private UserCountsDAO userCountsDAO;
	
	public void addCourseCount(Integer userId, int incr) {
		int count = userCountsDAO.insertOrIncreseCourseCount(userId, incr, System.currentTimeMillis());
		DAOUtils.checkAffectRows(count, Arrays.asList(1, 2));
	}

	public void addFinishedCount(Integer userId, int incr) {
		int count = userCountsDAO.insertOrIncreseFinishedCount(userId, incr, System.currentTimeMillis());
		DAOUtils.checkAffectRows(count, Arrays.asList(1, 2));
	}
	
	public void addDuration(Integer userId, int incr) {
		int count = userCountsDAO.insertOrIncreseDuration(userId, incr, System.currentTimeMillis());
		DAOUtils.checkAffectRows(count, Arrays.asList(1, 2));
	}
	
	@Override
	public Map<Integer, UserCountsModel> getByIds(Collection<Integer> ids) {
		Map<Integer, UserCountsModel> result = userCountsDAO.getByIds(ids);
		return result;
	}

	@Override
	protected AbstractDAO<Integer, UserCountsModel> dao() {
		throw new UnsupportedOperationException();
	}
}
