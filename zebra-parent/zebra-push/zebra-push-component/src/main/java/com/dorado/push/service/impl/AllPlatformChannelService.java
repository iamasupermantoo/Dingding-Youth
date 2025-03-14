package com.dorado.push.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.dorado.push.event.IPushMessage;
import com.dorado.push.service.PushChannelService;

/**
 * 
 * 不去分客户端平台，都推送。将推送任务，委托给其他push通道。
 * 
 * @author wangsch
 * @date 2016年11月1日
 */
@Service("allPlatformChannel")
public class AllPlatformChannelService implements PushChannelService, ApplicationContextAware {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    private Set<PushChannelService> delegates;

    @Override
    public void push(Collection<Integer> users, IPushMessage message) {
        for (PushChannelService delegate : delegates) {
            try {
                delegate.push(users, message);
            } catch (Exception e) {
                logger.debug("Ops.", e);
            }
        }
    }

    @Override
    public void push(Integer user, IPushMessage message) {
        for (PushChannelService delegate : delegates) {
            try {
                delegate.push(user, message);
            } catch (Exception e) {
                logger.debug("Ops.", e);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Collection<PushChannelService> values = applicationContext.getBeansOfType(PushChannelService.class)
                .values();
        delegates = new HashSet<>(values);
        delegates.remove(this);
    }
}
