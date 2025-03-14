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
		
		public static final String BOOK_ADMIN = "教材管理";
		
		public static final String USER_ADMIN = "用户管理";
		
		public static final String TEACHER_ADMIN = "教师管理";
		
		public static final String HOME = "后台首页相关";
		
		public static final String COURSE_ADMIN = "课程管理相关";
		
		public static final String MEDIA_ADMIN = "媒体管理相关";
		
		public static final String LESSON_PLAN = "排课相关";
		
		public static final String ADMIN_USER = "后台用户与权限管理";
		
		public static final String ADMIN_LOG = "操作日志管理";
		
		public static final String HOMEWORK_ADMIN = "作业管理";
		
		public static final String HOMEWORK = "作业相关";
		
		public static final String PAY = "支付相关（beta）";
		
		public static final String COURSE = "课程相关";
		
		public static final String REACTION = "反馈相关";
		
	}
	
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.youshi"))
				.paths(PathSelectors.any())
				.build()
				.groupName("admin")
				.useDefaultResponseMessages(false)
				;
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Zebra Admin文档（swagger2）")
				.description("Zebra Admin工程api文档，主要用于测试接口")
				.contact("wangsch@youshi-inc.com")
				.version("1.0").build();
	}
}