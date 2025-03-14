package com.dorado.push.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dorado.framework.constants.InProduction;
import com.dorado.framework.utils.crypt.DES;
import com.dorado.push.event.AbsPushEvent;
import com.dorado.push.event.IPushMessage;
import com.dorado.push.event.IPushMessage.PushAction;
import com.dorado.push.event.IPushMessage.PushKey;
import com.dorado.push.event.PushEvent;
import com.dorado.push.model.SimpleMessage;
import com.notnoop.apns.PayloadBuilder;

/**
 * 
 * push工具类
 * 
 * @author wangsch
 * @date 2016年10月31日
 */
public class PushUtils {
	
	/**
	 * 适配方法，把{@link PushEvent}转换为{@link IPushMessage}实例
	 * 
	 * @param pushEvent	{@link PushEvent}实例
	 * @return					{@link IPushMessage}实例
	 */
    public static IPushMessage buildMessage(PushEvent pushEvent) {
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
    private static IPushMessage buildMessage(IPushMessage.PushAction action, String message, 
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
    
    public static String buildFinalMessage(SimpleMessage msg) {
    	return buildFinalMessage(null, msg);
    }
    
    /**
     * 最终的push消息，json字符串，apns格式
     * 
     * @param userId		用户id
     * @param msg
     * @return
     */
    public static String buildFinalMessage(Integer userId, SimpleMessage msg) {
		PayloadBuilder newPayload = PayloadBuilder.newPayload();
		// aps.badge
		newPayload.badge(msg.get(PushKey.badge) == null ? 0 : Integer.valueOf(msg.get(PushKey.badge).toString()));
		// aps.alert
		Object alert = msg.get(PushKey.alert);
		if (alert != null && alert.toString().trim().length() != 0) {
			String alertBody = alert.toString().trim();
			newPayload.alertBody(alertBody);
			if (newPayload.isTooLong()) {
				newPayload.shrinkBody();
			}
		}
		
		// action
		newPayload.customField(PushKey.action.toString(), ((PushAction)msg.get(PushKey.action)).getValue());
		// user
		if(userId != null) {
			newPayload.customField(PushKey.user.toString(), getUserUuid(userId));
		}
		
		// gotoPage
		Object gotoPage = msg.get(PushKey.gotoPage);
		if(gotoPage != null) {
			newPayload.customField(PushKey.gotoPage.toString(), gotoPage);
		}
		// extra TODO
		
		return newPayload.build();
	}

	// TODO 直接传递用户uuid
	private static final byte[] DES_KEY = InProduction.get() 
			? new byte[] { 16, 37, -42, 38, -123, -45, -99, -62 } : new byte[] { 17, 37, 42, 38, -23, -85, -99, -60 };
	private static final DES des = new DES(DES_KEY);
	public static String getUserUuid(Integer userId) {
		String userUuid = des.encodeBase64URLSafeStringLong(userId.longValue());
		return userUuid;
	}
    
    
    
}
