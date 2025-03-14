package com.dorado.push.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dorado.framework.event.EventConsumer;
import com.dorado.framework.event.utils.DefaultEventTypeRegistry;
import com.dorado.framework.utils.DoradoBeanFactory;
import com.dorado.push.constants.PushEventType;
import com.dorado.push.server.listener.impl.push.AndroidPushListener;
import com.dorado.push.server.listener.impl.push.IOSPushListener;
import com.dorado.push.server.utils.SingleProcessCheck;
import com.dorado.push.server.utils.SingleProcessCheck.ProcessType;

/**
 * 
 * push服务，启动类
 * 
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class PushServerBootstrap {
	private static final Logger LOGGER = LoggerFactory.getLogger(PushServerBootstrap.class);
	
	public static void main(String[] args) {
		if (SingleProcessCheck.instance.isStarted(ProcessType.DoradoPushServer)) {
			System.out.println("ERR: Already another instance exist");
            System.exit(0);
        }
		
		DefaultEventTypeRegistry registry = initRegistry();
		String eventPkg = "com.dorado.push.event";
		
		try {
			EventConsumer.start(args, registry, eventPkg, "d_push");
		} catch(Exception e) {
			LOGGER.error("Ops, start EventConsumer fail. ", e);
			System.exit(-1);
		}
	}

	/**
	 * 初始化{@link EventRegistry}
	 * 
	 * @return	{@link DefaultEventTypeRegistry}
	 */
	private static DefaultEventTypeRegistry initRegistry() {
		DefaultEventTypeRegistry registry = DefaultEventTypeRegistry.getInstance();
		PushEventType.AndroidPush.register(registry);
		PushEventType.IOSPush.register(registry);
		
		AndroidPushListener androidPushListener = DoradoBeanFactory.getBean(AndroidPushListener.class);
		androidPushListener.register(registry);
		
		IOSPushListener iosPushListener = DoradoBeanFactory.getBean(IOSPushListener.class);
		iosPushListener.register(registry);
		
		return registry;
	}
	
}
