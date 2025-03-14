package com.dorado.push.service;

import java.util.Collection;
import java.util.EnumSet;

import com.dorado.push.constants.PushDevice;
import com.dorado.push.event.IPushMessage;

/**
 * 
 * push服务，根据设备调用具体push通道发送通知。
 * 
 * @author wangsch
 * @date 2016-11-04
 * 
 * @see 
 * 
 */
public interface PushService {

    /**
     * 给登陆用户发送push
     * 
     * @param pushDevice		推送的设备类型
     * @param userIds				接收者用户id集合
     * @param message			消息{@link IPushMessage}实例
     */
    public void push(EnumSet<PushDevice> pushDevice, Collection<Integer> userIds, IPushMessage message);

    /**
     * 根据tokens，给未登录用户发送push
     * 
     * @param	pushDevice		推送的设备类型
     * @param	tokens				设备tokens
     */
    public void pushUnlogin(EnumSet<PushDevice> pushDevice, Collection<String> tokens, IPushMessage message);
}
