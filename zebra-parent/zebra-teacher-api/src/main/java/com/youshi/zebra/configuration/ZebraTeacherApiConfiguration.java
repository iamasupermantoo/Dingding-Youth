package com.youshi.zebra.configuration;

import javax.sql.DataSource;

import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.jmx.JmxMetricWriter;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.dorado.framework.datasource.DataSourceByZooKeeper;
import com.youshi.zebra.core.constants.db.ZebraDB;

/**
 * @author wangsch
 * @date 2017年1月4日
 */
@Configuration
@ImportResource({ "classpath*:spring/*.xml" })
@Import({SpringMvcConfiguration.class})
public class ZebraTeacherApiConfiguration {
	
	@Bean
	@ExportMetricWriter
	MetricWriter metricWriter(MBeanExporter exporter) {
		return new JmxMetricWriter(exporter);
	}
	
	
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
	    return new MethodValidationPostProcessor();
	}
	
	@Bean("connectDataSource")
	public DataSource connect() {
		return DataSourceByZooKeeper.of(ZebraDB.connect.getZKName());
	}
	
//	@Bean("testDataSource")
//	public BasicDataSource mobile() {
//		BasicDataSource ds = DataSourceByZooKeeper.of(ZebraDB.mobile.getZKName()).getResource();
//		return ds;
//	}
	
	
	
	
}
