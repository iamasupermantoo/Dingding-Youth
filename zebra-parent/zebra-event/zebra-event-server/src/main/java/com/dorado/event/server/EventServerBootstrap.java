package com.dorado.event.server;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.dorado.event.server.utils.SingleProcessCheck;
import com.dorado.event.server.utils.SingleProcessCheck.ProcessType;
import com.dorado.framework.constants.InProduction;
import com.dorado.framework.event.EventConsumer;
import com.dorado.framework.event.listener.EventListener;
import com.dorado.framework.event.utils.DefaultEventTypeRegistry;
import com.dorado.framework.utils.DoradoBeanFactory;
import com.youshi.zebra.core.constants.ZebraProfiles;
import com.youshi.zebra.event.constant.ZebraEventType;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, JacksonAutoConfiguration.class})
public class EventServerBootstrap {
	private static final Logger logger = LoggerFactory.getLogger(EventServerBootstrap.class);
	
	public static void main(String[] args) {
		if (SingleProcessCheck.instance.isStarted(ProcessType.DoradoEventConsumer)) {
			System.out.println("ERR: Already another instance exist");
            System.exit(0);
        }
		
//		SpringApplication app = new SpringApplication(EventServerBootstrap.class);
//		app.setAdditionalProfiles(InProduction.get() ? ZebraProfiles.PRODUCTION 
//				: ZebraProfiles.DEVELOPMENT);
//		app.run(args);
//		
//		System.out.println("OK here ");
		
		DefaultEventTypeRegistry registry = initRegistry();
		String eventPkg = "com.dorado.event.model.impl";
		
		try {
			EventConsumer.start(args, registry, eventPkg, "d_event");
		} catch(Exception e) {
			logger.error("Fail start EventCousumer", e);
			System.exit(-1);
		}
	}

	/**
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static DefaultEventTypeRegistry initRegistry() {
		DefaultEventTypeRegistry registry = DefaultEventTypeRegistry.getInstance();
		ZebraEventType[] eventTypes = ZebraEventType.values();
		if(eventTypes.length == 0) {
			System.out.println("ERR: No event type found in " + ZebraEventType.class.getName());
            System.exit(0);
		}
		for (ZebraEventType eventType : eventTypes) {
			eventType.register(registry);
		}
		
		Map<String, EventListener> listeners = DoradoBeanFactory.getBeansOfType(EventListener.class);
		if(listeners.size() == 0) {
			System.out.println("ERR: No event listener found of type " + EventListener.class.getName());
            System.exit(0);
		}
		for (EventListener listener : listeners.values()) {
			listener.register(registry);
		}
		return registry;
	}
	
}
