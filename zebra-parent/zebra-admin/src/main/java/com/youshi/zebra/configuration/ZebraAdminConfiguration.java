package com.youshi.zebra.configuration;

import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.jmx.JmxMetricWriter;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * @author wangsch
 * @date 2017年1月4日
 */
@Configuration
@ImportResource({ "classpath*:spring/*.xml" })
@Import({SpringMvcConfiguration.class, Swagger2Configuration.class})
public class ZebraAdminConfiguration {
	
	@Bean
	@ExportMetricWriter
	MetricWriter metricWriter(MBeanExporter exporter) {
		return new JmxMetricWriter(exporter);
	}
	
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
	    return new MethodValidationPostProcessor();
	}
}
