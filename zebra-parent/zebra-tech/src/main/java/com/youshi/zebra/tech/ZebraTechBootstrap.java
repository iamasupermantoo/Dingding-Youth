package com.youshi.zebra.tech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.dorado.framework.constants.InProduction;

/**
 * 启动类
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, JacksonAutoConfiguration.class})
public class ZebraTechBootstrap {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ZebraTechBootstrap.class);
		app.setAdditionalProfiles(InProduction.get() ? ZebraProfiles.PRODUCTION 
				: ZebraProfiles.DEVELOPMENT);
		app.run(args);
	}
}
