package com.youshi.zebra.configuration;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.druid.support.http.StatViewServlet;
import com.dorado.mvc.interceptors.AccessLogInterceptor;
import com.youshi.zebra.admin.web.interceptor.AdminLogInterceptor;
import com.youshi.zebra.core.web.interceptor.TicketInterceptor;
import com.youshi.zebra.user.constant.UserType;

/**
 * 
 * SpringMVC相关配置，如：拦截器、过滤器等<br />
 * 可以完全通过注解和代码的方式配置，但为了方便，还是通过{@link ImportResource}引入了一个xml，这个xml来自于之前传统的war工程，只是做了少量修改。
 * 
 * @author wangsch
 * @date 2017年1月10日
 */
@Configuration
@ImportResource("classpath*:admin-servlet.xml")
public class SpringMvcConfiguration extends WebMvcConfigurerAdapter {
	
	@Autowired
	public TicketInterceptor ticketInterceptor;
	
	@Autowired
	private AdminLogInterceptor adminLogInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		ticketInterceptor.setUserType(UserType.Admin);
		registry.addInterceptor(ticketInterceptor);
		
		registry.addInterceptor(new AccessLogInterceptor());
//		registry.addInterceptor(new ForceNoCacheInterceptor());
		registry.addInterceptor(adminLogInterceptor);
	}
	
//	@Bean
//    public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
//        return new EmbeddedServletContainerCustomizer() {
//
//            @Override
//            public void customize(ConfigurableEmbeddedServletContainer container) {
//                if (container instanceof TomcatEmbeddedServletContainerFactory) {
//                    customizeTomcat((TomcatEmbeddedServletContainerFactory)container); 
//                }
//            }
//
//            private void customizeTomcat(TomcatEmbeddedServletContainerFactory tomcatFactory) {
//                tomcatFactory.addContextCustomizers(new TomcatContextCustomizer() {
//
//                    @Override
//                    public void customize(Context context) {
//                        Container jsp = context.findChild("jsp");
//                        if (jsp instanceof Wrapper) {
//                            ((Wrapper)jsp).addInitParameter("development", "false");
//                        }
//
//                    }
//
//                });
//            }
//        };
//    }
	
	@Bean
	public ServletRegistrationBean druidServlet() {
		return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
	}
}
