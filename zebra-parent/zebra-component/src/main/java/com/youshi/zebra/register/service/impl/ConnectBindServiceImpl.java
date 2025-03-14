/**
 * 
 */
package com.youshi.zebra.register.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import com.dorado.framework.event.utils.PerfUtils;
import com.ecyrd.speed4j.StopWatch;
import com.youshi.zebra.connect.constant.ConnectType;
import com.youshi.zebra.connect.model.ConnectBindInfo;
import com.youshi.zebra.connect.model.ConnectUserDetail;
import com.youshi.zebra.connect.service.ConnectService;
import com.youshi.zebra.register.constant.ConnectBindStatus;
import com.youshi.zebra.register.exception.ConnectAlreadyBindedException;
import com.youshi.zebra.register.exception.ConnectBindedByOtherException;
import com.youshi.zebra.register.service.ConnectBindService;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
@Service
public class ConnectBindServiceImpl implements ConnectBindService, BeanPostProcessor{

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
    
    private Map<ConnectType, ConnectService> connectServices = new HashMap<>();

    @Override
    public ConnectService getConnectService(ConnectType type) {
    	return connectServices.get(type);
    }

    @Override
	public ConnectBindStatus getBindStatus(String externalUserId,
			ConnectType connectType) {
    	if (StringUtils.isEmpty(externalUserId) || connectType == null) {
    		throw new IllegalArgumentException();
    	}
    	
    	ConnectService connectService = getConnectService(connectType);
		if (connectType == ConnectType.Mobile) {
			boolean externalUserConnect = connectService.isExternalUserConnect(externalUserId);
			return externalUserConnect ? ConnectBindStatus.Binded: ConnectBindStatus.NeverBinded;
		}
		
		String accessToken = connectService.getAccessToken(externalUserId);
		if (accessToken == null) {
			return ConnectBindStatus.NeverBinded;
		} else {
			Integer userId = connectService.getUserIdByExternalUserId(externalUserId);
			if (userId == null || userId <= 0) {
				return ConnectBindStatus.Unbinded;
			} else {
				return ConnectBindStatus.Binded;
			}
		}
	}
    
	@Override
	public ConnectBindStatus getBindStatus(Integer userId, ConnectType connectType) {
		ConnectService connectService = getConnectService(connectType);
		String accessToken = connectService.getAccessToken(userId);
		if(accessToken == null) {
			return ConnectBindStatus.NeverBinded;
		} else {
			String externalUserId = connectService.getExternalUserIdByUserId(userId);
			if(StringUtils.isEmpty(externalUserId)) {
				return ConnectBindStatus.Unbinded;
			} else {
				return ConnectBindStatus.Binded;
			}
		}
	}

    @Override
    public void bindConnect(Integer userId, ConnectType type, 
    		String accessToken, String refreshToken, String externalUserId
    		) {
		StopWatch stopWatch = PerfUtils.getWatcher("ConnectBindService.bindConnect." + type);
		
		ConnectService connectService = getConnectService(type);
		
		// 是否被其他用户绑定了
		Integer otherUserId = connectService.getUserIdByExternalUserId(externalUserId);
		if (otherUserId != null) {
			if (otherUserId.equals(userId)) {
				logger.info("You already binded before, ignore. userId: {}, externalUserId: {}", 
						userId, externalUserId);
				return;
			} else {
				logger.error("Connect binded by other user. userId: {}, externalUserId: {}, otherUser: {}", 
						userId, externalUserId, otherUserId);
				throw new ConnectBindedByOtherException();
			}
		}
	
		// 用户是否已绑定了其他账号
		String beforeExternalUserId = connectService.getExternalUserIdByUserId(userId);
		if (beforeExternalUserId != null && !StringUtils.equals(externalUserId, beforeExternalUserId)) {
		    logger.error("Connect binded to other before. userId: {}, externalUserId: {}, beforeExternalUserId: {}", 
		    		userId, externalUserId, beforeExternalUserId);
		    throw new ConnectAlreadyBindedException();
		}
	
		connectService.createConnect(userId, accessToken, refreshToken, externalUserId);
		stopWatch.stop();
    }
    
    @Override
    public void unbindConnect(Integer userId, ConnectType type) {
    	ConnectService connectService = getConnectService(type);
    	if(connectService != null) {
    		connectService.removeConnect(userId);
    	}
    }

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if(bean instanceof ConnectService) {
			ConnectService service = (ConnectService) bean;
			ConnectType type = service.getConnectType();
			connectServices.put(type, service);
			logger.info("Add connect service: {}", service.getClass().getName());
		}
		return bean;
	}

	@Override
	public List<ConnectBindInfo> getBindList(Integer userId, List<ConnectType> types) {
		List<ConnectType> forTypes = CollectionUtils.isEmpty(types) ? Arrays.asList(ConnectType.values()) : types;
		
		List<ConnectBindInfo> list = new ArrayList<>();
		for(ConnectType type : forTypes) {
			ConnectBindStatus bindStatus = null;
			String accountName = null;
			
			ConnectService service = getConnectService(type);
			if(type == ConnectType.Mobile) {
				bindStatus = ConnectBindStatus.Binded;
				accountName = service.getExternalUserIdByUserId(userId);
			} else {
				bindStatus = getBindStatus(userId, type);
				if(bindStatus == ConnectBindStatus.Binded) {
					String externalUserId = service.getExternalUserIdByUserId(userId);
					ConnectUserDetail user = service.getConnectUserDetail(externalUserId);
					accountName = user.getName();
				}
			}
			
			list.add(new ConnectBindInfo(type, accountName, bindStatus));
		}
		
		return list;
	}
}
