package com.dorado.push.service;

import java.util.Collection;

import com.dorado.push.event.IPushMessage;


/**
 * 
 * 向客户端发push消息，最终执行发送动作的服务，调用具体push通道。
 * 
 * @author wangsch
 * @date 2016年10月31日
 */
public interface PushChannelService {
	
	/**
	 * 向多个用户发push
	 * 
	 * @param userIds				用户id集合
	 * @param message			{@link IPushMessage}消息对象
	 */
    public void push(Collection<Integer> userIds, IPushMessage message);
    
    /**
	 * 向单个用户发push
	 * 
	 * @param userId				用户id
	 * @param message			{@link IPushMessage}消息对象
	 */
    public void push(Integer userId, IPushMessage message);

}
