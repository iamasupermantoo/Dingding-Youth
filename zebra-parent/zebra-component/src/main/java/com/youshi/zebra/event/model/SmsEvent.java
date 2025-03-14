package com.youshi.zebra.event.model;

import java.util.Map;

import com.dorado.framework.crud.model.util.ModelUtils;
import com.dorado.framework.event.model.AbsEvent;
import com.google.common.collect.ImmutableMap;
import com.youshi.zebra.event.constant.ZebraEventType;
import com.youshi.zebra.mobile.constants.SmsSendType;

/**
 * 
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class SmsEvent extends AbsEvent {

	public enum SmsEventKey {
		createTime,
		msg,
		mobile,
		smsType;
	}
	
    public SmsEvent(String message, String mobile, SmsSendType type, long createTime) {
        super(ImmutableMap.<String, Object> builder().put(SmsEventKey.msg.name(), message)
                .put(SmsEventKey.createTime.name(), createTime).put(SmsEventKey.mobile.name(), mobile)
                .put(SmsEventKey.smsType.name(), type.getValue()).build());
    }

    public SmsEvent(Map<String, Object> resolvedData) {
        super(resolvedData);
    }

    @Override
    public ZebraEventType getType() {
        return ZebraEventType.Sms;
    }

    public String getMessage() {
        return ModelUtils.getString(this, SmsEventKey.msg);
    }

    public String getMobile() {
        return ModelUtils.getString(this, SmsEventKey.mobile);
    }

    public SmsSendType getSmsType() {
        return ModelUtils.getEnum(this, SmsEventKey.smsType, SmsSendType::fromOf);
    }
}
