package com.youshi.zebra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import de.codecentric.boot.admin.config.EnableAdminServer;

@Configuration
@EnableAutoConfiguration
@EnableAdminServer
public class ZebraBootAdminApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ZebraBootAdminApplication.class, args);
	}
}
