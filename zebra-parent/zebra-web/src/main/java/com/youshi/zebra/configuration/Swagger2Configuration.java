package com.youshi.zebra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.youshi.zebra.core.constants.ZebraProfiles;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author wangsch
 * @date 2017年1月19日
 */
@Configuration
@Profile(ZebraProfiles.DEVELOPMENT)
@EnableSwagger2
public class Swagger2Configuration {
	
	public static class SwaggerTags {
		public static final String HOMEWORK = "作业相关";
		public static final String LIVE = "直播相关";
		public static final String ORDER_PAY = "订单与支付相关";
		public static final String COURSE = "课程相关";
		public static final String REACTION = "评价相关";
	}
	
	
	
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.youshi"))
				.paths(PathSelectors.any())
				.build()
				.groupName("student")
				.useDefaultResponseMessages(false)
				;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Zebra WEB API文档（swagger2）")
				.description("Zebra WEB API文档")
				.termsOfServiceUrl("http://wangsch.net/")
				.contact("wangsch@youshi-inc.com")
				.version("1.0").build();
	}
}
