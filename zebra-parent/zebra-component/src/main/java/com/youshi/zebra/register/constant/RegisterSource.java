/**
 * 
 */
package com.youshi.zebra.register.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 
* 用户注册来源
* Date: May 10, 2016
* 
 * @author wangsch
 *
 */
public enum RegisterSource {
    Admin(0),	 // 管理员注册
    Mobile(1), // 手机号注册
    QQ(2), 		// QQ注册
    Weixin(3),	// 微信注册
    ;

    private final int value;

    private RegisterSource(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static final Map<Integer, RegisterSource> map = new HashMap<>();
    static {
        for (RegisterSource e : RegisterSource.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final RegisterSource fromValue(int status) {
        return map.get(status);
    }

}
