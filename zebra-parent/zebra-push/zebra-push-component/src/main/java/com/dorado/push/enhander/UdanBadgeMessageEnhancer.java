package com.dorado.push.enhander;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.dorado.push.dao.BadgeStore;
import com.dorado.push.event.IPushMessage;
import com.dorado.push.event.IPushMessage.PushKey;

/**
 * 
 * 
 * @author wangsch
 * @date 2016年11月1日
 */
//@Service("udanBadgeMessageEnhancer")
public class UdanBadgeMessageEnhancer implements MessageEnhancer {

    @Autowired
    @Qualifier("udanBadgeStore")
    private BadgeStore badgeStore;
    
    @Override
    public void enhance(IPushMessage message) {
        message.set(PushKey.badge, badgeStore.getBadge(message.getUser()));
    }
}
