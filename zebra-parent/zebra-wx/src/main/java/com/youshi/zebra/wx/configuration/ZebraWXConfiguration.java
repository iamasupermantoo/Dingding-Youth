package com.youshi.zebra.wx.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.jmx.JmxMetricWriter;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.youshi.zebra.core.web.interceptor.TicketInterceptor;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年4月20日
 */
@Configuration
@ImportResource({ "classpath*:spring/*.xml" })
public class ZebraWXConfiguration {
	
	@Autowired
	public TicketInterceptor ticketInterceptor;
	
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
