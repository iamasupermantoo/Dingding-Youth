package com.dorado.push.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dorado.framework.event.utils.PerfUtils;
import com.dorado.push.dao.DeviceTokenDAO;
import com.dorado.push.service.AbstractPushChannelService;
import com.dorado.push.utils.EmojiConverter;
import com.ecyrd.speed4j.StopWatch;
import com.gexin.rp.sdk.base.IIGtPush;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

/**
 * 安卓个推，push推送
 * 
 * @author wangsch
 * @date 2016年11月1日
 */
@Service("androidGetuiPushChannel")
public class AndroidGetuiPushChannelService extends AbstractPushChannelService<String> {

    private static final String APPID = "kqsI8pj8ke7QN11PR7KCa";

    private static final String APPKEY = "pIe0R3bwenAVtPAloj0tF2";

    private static final String MASTERSECRET = "7XtEgpFHWp69SnuAkjLR03";

    private static final String API = "http://sdk.open.api.igexin.com/apiex.htm";

    private static final long OFFLINE_EXPIRE_TIME = TimeUnit.HOURS.toMillis(72); //72小时已经是最大值了

    @Autowired
    @Qualifier("androidGetuiDeviceTokenDAO")
    private DeviceTokenDAO<String> deviceTokenDAO;
    
    @PostConstruct
    public void init() {
    	setDeviceTokenDAO(deviceTokenDAO);
    }
    
    
    @Override
    public void servicePush(Collection<String> tokens, String message) {
        if (CollectionUtils.isEmpty(tokens)) {
            return;
        }
        String filteredMessageString = EmojiConverter.getUnicodeToEmpty().convert(message);
        IIGtPush push = new IGtPush(API, APPKEY, MASTERSECRET);
        StopWatch stopWatch = PerfUtils.getWatcher("push.getui");
        try {
            if (tokens.size() == 1) {
                pushToSingle(tokens, filteredMessageString, push);
            } else {
                pushToList(tokens, filteredMessageString, push);
            }
        } catch (Throwable e) {
            logger.error("fail to send push.", e);
        } finally {
            stopWatch.stop();
        }
    }

    @Override
    public void servicePushAll(String message) {
        String filteredMessageString = EmojiConverter.getUnicodeToEmpty().convert(message);
        IGtPush push = new IGtPush(API, APPKEY, MASTERSECRET);
        StopWatch stopWatch = PerfUtils.getWatcher("push.getui.all");
        try {  
        	pushToAll(filteredMessageString, push);
    		logger.info("----[online]start getui push all:" + message);
        } catch (Throwable e) {
            logger.error("fail to send push.", e);
        } finally {
            stopWatch.stop();
        }
    }

    
    private void pushToList(Collection<String> tokens, String filteredMessageString, IIGtPush push) {
        ListMessage msg = new ListMessage();
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(APPID);
        template.setAppkey(APPKEY);
        template.setTransmissionContent(filteredMessageString);
        template.setTransmissionType(2);
        msg.setData(template);
        msg.setOffline(true);
        msg.setOfflineExpireTime(OFFLINE_EXPIRE_TIME);
        List<Target> targets = new ArrayList<>();
        for (String clientId : tokens) {
            Target target = new Target();
            target.setAppId(APPID);
            target.setClientId(clientId);
            targets.add(target);
        }
        String contentId = push.getContentId(msg);
        IPushResult ret = push.pushMessageToList(contentId, targets);
        logger.info("android-push|tokens:" + tokens + "|msg:" + filteredMessageString + "|ret:"
                + ret.getResponse());
    }

    private void pushToSingle(Collection<String> tokens, String filteredMessageString, IIGtPush push) {
        String clientId = tokens.iterator().next();
        SingleMessage msg = new SingleMessage();
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(APPID);
        template.setAppkey(APPKEY);
        template.setTransmissionContent(filteredMessageString);
        template.setTransmissionType(2);
        msg.setData(template);
        msg.setOffline(true);
        msg.setOfflineExpireTime(OFFLINE_EXPIRE_TIME);
        Target target = new Target();
        target.setAppId(APPID);
        target.setClientId(clientId);
        IPushResult ret = push.pushMessageToSingle(msg, target);
        logger.info("android-push|tokens:" + clientId + "|msg:" + filteredMessageString + "|ret:"
                + ret.getResponse());
    }

    private void pushToAll(String filteredMessageString, IIGtPush push) {
    	AppMessage msg = new AppMessage();
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(APPID);
        template.setAppkey(APPKEY);
        template.setTransmissionContent(filteredMessageString);
        template.setTransmissionType(2);
        msg.setData(template);
        msg.setOffline(true);
        msg.setOfflineExpireTime(OFFLINE_EXPIRE_TIME);
        List<String> appIdList = new ArrayList<String>();
        appIdList.add(APPID);
        msg.setAppIdList(appIdList);
        IPushResult ret = push.pushMessageToApp(msg);
        logger.info("android-pushAll|msg:" + filteredMessageString + "|ret:"+ ret);
    }

    
    @Override
    public boolean badgeInc() {
        return false;
    }
}
