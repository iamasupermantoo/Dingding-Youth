package com.dorado.push.server.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dorado.push.event.AbsPushEvent;
import com.dorado.push.event.IPushMessage;
import com.dorado.push.event.IPushMessage.PushAction;
import com.dorado.push.event.PushEvent;
import com.dorado.push.utils.MessageBuilder;

/**
 * 
 * push工具类
 * 
 * @author wangsch
 * @date 2016年10月31日
 */
@Service
public class PushUtils {
	
	/**
	 * 适配方法，把{@link PushEvent}转换为{@link IPushMessage}实例
	 * 
	 * @param pushEvent	{@link PushEvent}实例
	 * @return					{@link IPushMessage}实例
	 */
    public IPushMessage buildMessage(PushEvent pushEvent) {
    	if(!(pushEvent instanceof AbsPushEvent)) {
    		throw new IllegalArgumentException(pushEvent.toString());
    	}
    	AbsPushEvent push = (AbsPushEvent)pushEvent;
    	IPushMessage message = null;
    	message = buildMessage(push.getPushAction(), push.getMessage(), push.getGotoPage(), push.getGotoPageParams());
    	return message;
    }

    /**
     * 
     * 根据参数，通过{@link MessageBuilder}构造{@link IPushMessage}实例
     * 
     * @param action								{@link IPushMessage.PushAction}
     * @param message						消息内容
     * @param gotoPage						gotoPage类型，可以为null。
     * @param gotoPageParams			gotoPage参数
     * @return										构造好的{@link IPushMessage}实例
     */
    private IPushMessage buildMessage(IPushMessage.PushAction action, String message, 
    		Integer gotoPage, Map<String, Object> gotoPageParams
    		) {
        MessageBuilder builder = MessageBuilder.newBuilder();
        if (StringUtils.isNotBlank(message)) {
            builder.setMessage(message);
        }
        builder.setAction(action);
        if(action==PushAction.GotoPage) {
        	builder.setGotoPage(gotoPage, gotoPageParams);
        }
        
        return builder.build();
    }
}
