package com.youshi.zebra.user.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public enum UserRelationStatus {
    Unknown(0), //未知
    Request(1), //请求加好友中
    Success(2), //通过, 成为好友
    Denied(3), //骚年, 你被拒绝啦
    Delete(4), //走好, 不送
    Fav(5)//点赞
    ;

    private int value;

    UserRelationStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static final Map<Integer, UserRelationStatus> MAP = new HashMap<>();

    static {
        for (UserRelationStatus item : UserRelationStatus.values()) {
            MAP.put(item.value, item);
        }
    }

    public static final UserRelationStatus fromValue(Integer value) {
        return MAP.get(value);
    }
}
