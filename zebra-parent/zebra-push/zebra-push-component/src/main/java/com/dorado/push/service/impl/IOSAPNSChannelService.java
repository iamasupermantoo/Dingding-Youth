package com.dorado.push.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.push.dao.DeviceTokenDAO;
import com.dorado.push.service.AbstractPushChannelService;
import com.ecyrd.speed4j.StopWatch;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.ReconnectPolicy;

/**
 * ios apns push服务
 * 
 * @author wangsch
 * @date 2016年11月1日
 */
@Service("iosAPNSChannel")
public class IOSAPNSChannelService extends AbstractPushChannelService<String> {
	private Logger logger = LoggerFactory.getLogger(IOSAPNSChannelService.class);
	
	private ApnsService apnsService;
	
	@Autowired
    @Qualifier("iosAPNSDeviceTokenDAO")
    private DeviceTokenDAO<String> deviceTokenDAO;
    
    @PostConstruct
    public void init() {
    	setDeviceTokenDAO(deviceTokenDAO);
    	
    	// 初始化apns证书
		ApnsServiceBuilder serviceBuilder = null;
		if(InProduction.get()) {
			String productionCertFile = "/production/udan.p12";
			InputStream certIS = getClass().getResourceAsStream(productionCertFile);
			if(certIS == null) {
				throw new IllegalArgumentException("Could'd find production cert file: " + productionCertFile);
			}
			serviceBuilder = APNS.newService()
					.withCert(certIS, "youdan")
					.withProductionDestination()
					.withReconnectPolicy(ReconnectPolicy.Provided.EVERY_HALF_HOUR)
					.asPool(1024);
			
			logger.info("Config [PRODUCTION] ApnsServiceBuilder OK");
			
		} else {
			String developmentCertFile = "/development/udan.p12";
			InputStream certIS = getClass().getResourceAsStream(developmentCertFile);
			if(certIS == null) {
				throw new IllegalArgumentException("Could'd find development cert file: " + developmentCertFile);
			}
			serviceBuilder = APNS.newService()
					.withCert(certIS, "udan123")
					.withSandboxDestination()
					.withReconnectPolicy(ReconnectPolicy.Provided.EVERY_NOTIFICATION);
			logger.info("Config [DEVELOPMENT] ApnsServiceBuilder OK");
		}
		
		apnsService = serviceBuilder.build();
    }
    
	@Override
	public void servicePush(Collection<String> tokens, String message) {
		try {
			StopWatch stopWatch = PerfUtils.getWatcher("push.apns");
			try {
				logger.info("ios-push|tokens:" + new ArrayList<>(tokens) + "|msg:" + message);
				apnsService.push(tokens, message);
			} catch (Exception e) {
				logger.error("Ops ios-push", e);
			} finally {
				stopWatch.stop();
			}

		} catch (Exception e) {
			logger.error("Ops push", e);
		}
	}

	@Override
	public boolean badgeInc() {
		return true;
	}

	@Override
	public void servicePushAll(String message) {
		throw new UnsupportedOperationException();
	}
	
	// public void initCleaner() {
	// ScheduledExecutorService cleaner =
	// Executors.newSingleThreadScheduledExecutor();
	// cleaner.scheduleWithFixedDelay(new Runnable() {
	//
	// @Override
	// public void run() {
	// logger.info("Try to clean.....");
	// try {
	// for (Entry<App, ApnsService> entry : serviceMap.entrySet()) {
	// DeviceTokenDAO<String> deviceTokenStore = tokenStoreMap.get(entry
	// .getKey());
	// Map<String, Date> inactiveDevices =
	// entry.getValue().getInactiveDevices();
	// for (String inactiveDevice : inactiveDevices.keySet()) {
	// try {
	// deviceTokenStore.unbind(inactiveDevice);
	// // 返回的数据里都是大写的，小写再清一次
	// deviceTokenStore.unbind(inactiveDevice.toLowerCase());
	// logger.info("inactiveDevice:" + inactiveDevice);
	// } catch (Exception e) {
	// logger.debug("Ops.", e);
	// }
	// }
	// }
	// } catch (Exception e) {
	// logger.debug("Ops.", e);
	// }
	// }
	// }, 0, CLEANER_INTERVAL_IN_SECOND, TimeUnit.SECONDS);
	// }
}
