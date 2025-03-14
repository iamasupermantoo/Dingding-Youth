package com.youshi.zebra;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import com.dorado.framework.constants.InProduction;
import com.youshi.zebra.core.constants.ZebraProfiles;
/**
 * Zebra admin服务启动类
 * 
 * @author wangsch
 * @date 2017年2月15日
 */
@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, JacksonAutoConfiguration.class})
public class ZebraAdminBootstrap extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ZebraAdminBootstrap.class);
	}
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ZebraAdminBootstrap.class);
		app.setAdditionalProfiles(InProduction.get() ? ZebraProfiles.PRODUCTION 
				: ZebraProfiles.DEVELOPMENT);
		
		app.run(args);
	}
}