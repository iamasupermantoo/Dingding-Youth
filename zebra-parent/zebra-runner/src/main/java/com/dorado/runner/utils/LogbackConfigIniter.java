package com.dorado.runner.utils;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public final class LogbackConfigIniter {

    public static final void initLogHome(String logHomeDirectory) {
        System.setProperty("LOG_HOME", logHomeDirectory);
        JoranConfigurator joranConfigurator = new JoranConfigurator();
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        joranConfigurator.setContext(context);
        context.reset();
        try {
            joranConfigurator.doConfigure(LogbackConfigIniter.class.getClassLoader().getResource(
                    "logback/runner.xml"));
        } catch (JoranException e) {
            e.printStackTrace();
        }
    }
}
