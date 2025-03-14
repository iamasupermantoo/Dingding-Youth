package com.youshi.zebra.wx.configuration;
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
 * @date 2017年4月20日
 */
@Configuration
@Profile(ZebraProfiles.DEVELOPMENT)
@EnableSwagger2
public class Swagger2Configuration {
	
	public static class SwaggerTags {
		public static final String SHARE = "分享相关";
		public static final String ORDER_PAY = "订单与支付相关";
	}
	
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.youshi"))
				.paths(PathSelectors.any())
				.build()
				.groupName("wx")
				.useDefaultResponseMessages(false)
				;
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Sloth WX 文档（swagger2）")
				.description("Sloth WX 工程api文档，主要用于测试接口")
				.contact("wangsch@youshi-inc.com")
				.version("1.0").build();
	}
}