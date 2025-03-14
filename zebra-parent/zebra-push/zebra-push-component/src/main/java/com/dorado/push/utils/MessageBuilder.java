package com.dorado.push.utils;

import java.util.HashMap;
import java.util.Map;

import com.dorado.push.event.IPushMessage;
import com.dorado.push.event.IPushMessage.ExtraKey;
import com.dorado.push.event.IPushMessage.PushKey;
import com.dorado.push.model.SimpleMessage;
import com.google.common.collect.ImmutableMap;

/**
 * 构造一条push message（{@link SimpleMessage}）工具类
 * 
 * @author wangsch
 *
 * @see PushKey
 * @see SimpleMessage
 *
 */
public class MessageBuilder {

    private SimpleMessage message = null;

    private final Map<IPushMessage.ExtraKey, Object> extra = new HashMap<IPushMessage.ExtraKey, Object>();

    private MessageBuilder() {
        message = new SimpleMessage();
    }

    public static MessageBuilder newBuilder() {
        return new MessageBuilder();
    }

    public MessageBuilder setAction(IPushMessage.PushAction type) {
        message.set(PushKey.action, type);
        return this;
    }

    public MessageBuilder setMessage(String alertBody) {
        message.set(PushKey.alert, alertBody);
        return this;
    }
    
    public MessageBuilder setUser(Integer userId) {
    	message.set(PushKey.user, userId);
    	return this;
    }

    public MessageBuilder setGotoPage(Integer gotoPage, Map<String, Object> gotoPageParams) {
    	message.set(PushKey.gotoPage, ImmutableMap.<String, Object>builder()
    			.put("type", gotoPage)
    			.put("params", gotoPageParams)
    			.build());
    	
        return this;
    }

    public MessageBuilder addParam(ExtraKey key, String value) {
        if (value != null) {
            extra.put(key, value);
        }
        return this;
    }

    // QUES
//    public MessageBuilder setActive() {
//        extra.put(ParamKey.active, String.valueOf(true));
//        return this;
//    }

    public IPushMessage build() {
        message.set(PushKey.extra, extra);
        return message;
    }
}
