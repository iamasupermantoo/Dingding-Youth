package com.youshi.zebra.wx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.dorado.framework.constants.InProduction;
import com.youshi.zebra.core.constants.ZebraProfiles;

/**
 * Sloth wx服务启动类
 * 
 * @author wangsch
 * @date 2017年4月20日
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, JacksonAutoConfiguration.class})
public class ZebraWxBootstrap extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ZebraWxBootstrap.class);
	}
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ZebraWxBootstrap.class);
		app.setAdditionalProfiles(InProduction.get() ? ZebraProfiles.PRODUCTION 
				: ZebraProfiles.DEVELOPMENT);
		app.run(args);
	}
	
	
	
}