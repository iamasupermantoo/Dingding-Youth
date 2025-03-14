package com.youshi.zebra.mobile.constants;

import java.util.HashMap;
import java.util.Map;

public enum SmsSendType {
    /** 注册验证码短信 */
    Register(1),
    /** 重置手机号密码 */
    ResetPassword(2),
    ;

    private int value;

    private static Map<Integer, SmsSendType> map = new HashMap<>(SmsSendType.values().length);
    static {
        for (SmsSendType sendType : SmsSendType.values()) {
            map.put(sendType.getValue(), sendType);
        }
    }

    private SmsSendType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SmsSendType fromOf(int value) {
        SmsSendType smsSendType = map.get(value);
        if (smsSendType == null) {
            throw new IllegalArgumentException("value:" + value + " is not found!");
        }
        return smsSendType;
    }
}
