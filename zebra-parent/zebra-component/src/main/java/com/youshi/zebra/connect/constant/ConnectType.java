/**
 * 
 */
package com.youshi.zebra.connect.constant;

import java.util.HashMap;
import java.util.Map;

//import com.dorado.register.constant.RegisterSource;

/**
 * 站外关系类型。元素和{@link RegisterSource}基本相同，不过这个用于绑定相关。
* 
* Date: May 10, 2016
* 
 * @author wangsch
 *
 */
public enum ConnectType {
    Mobile(0), 
    QQ(1), 
    WeiXin(2)
    ;

    private final int value;

    private ConnectType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static final Map<Integer, ConnectType> map = new HashMap<>();
    static {
        for (ConnectType e : ConnectType.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final ConnectType fromValue(int type) {
        return map.get(type);
    }

}
