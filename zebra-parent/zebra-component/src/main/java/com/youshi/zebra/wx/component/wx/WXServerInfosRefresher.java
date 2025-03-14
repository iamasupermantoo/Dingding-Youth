package com.youshi.zebra.wx.component.wx;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.youshi.zebra.wx.component.wx.service.WXService;

/**
 * 
 * @author wangsch
 * @date 2017年4月22日
 */
@Component
public class WXServerInfosRefresher {
	private static final Logger logger = LoggerFactory.getLogger(WXServerInfosRefresher.class);
	private static final int corePoolSize = 2;
	
	private static final int WX_TICKET_REFRESH_INTERVAL = 115;
	
	private static ScheduledExecutorService scheduledExecutorService
	 	= Executors.newScheduledThreadPool(corePoolSize);
	
	@Autowired
	private WXService wxService;
	
	
	@PostConstruct
	public void start() {
		scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {					// 我把他踹住
					wxService.initServerInfo();
					logger.info("Refresh wx server info OK");
				} catch(Exception e) {
					logger.error("Ops when refresh wx server info.", e);
					doRetry();
				}
			}

			/**
			 * 重试三次
			 */
			private void doRetry() {
				int retry = 0;
				while(++retry <= 3) {
					logger.error("Retry {} times. ", retry);
					try {
						wxService.initServerInfo();
						logger.info("Retry refresh wx ticket OK");
						break;
					} catch(Exception e) {
						logger.error("Retry FAIL. ");
						try {
							Thread.sleep(TimeUnit.SECONDS.toMillis(5));
						} catch (InterruptedException e1) {
							logger.error("Ops sleep", e1);
						}
						continue;
					}
				}
			}
		}, 0, WX_TICKET_REFRESH_INTERVAL, TimeUnit.MINUTES);
	}
}
