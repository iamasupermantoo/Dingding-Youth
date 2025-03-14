package com.dorado.push.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.dorado.push.event.IPushMessage;
import com.dorado.push.utils.MessageBuilder;


/**
 * 
 * push消息实现类
 * 
 * @author wangsch
 * @date 2016年10月31日
 * 
 * @see MessageBuilder
 * 
 */
public class SimpleMessage implements IPushMessage {

    private final Map<PushKey, Object> msg;

    private Integer userId;

    public SimpleMessage() {
        msg = new HashMap<IPushMessage.PushKey, Object>();
    }

    private SimpleMessage(Map<PushKey, Object> msg) {
        this.msg = new HashMap<>();
        this.msg.putAll(msg);
    }

    /**
     * 用户id，必须set进来
     * 
     * @param userId		用户id
     */
    public void setUser(Integer userId) {
        this.userId = userId;
    }

    @Override
    public Integer getUser() {
        return userId;
    }

    @Override
    public void set(PushKey property, Object value) {
        msg.put(property, value);
    }

    @Override
    public Object get(PushKey property) {
        return msg.get(property);
    }

    @Override
    public SimpleMessage clone() {
        return new SimpleMessage(msg);
    }
}
