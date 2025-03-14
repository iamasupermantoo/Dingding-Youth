package com.youshi.zebra.user.config.util.codec;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.youshi.zebra.user.config.util.UserConfigCodec;

/**
 * boolean类型设置
 * @author wangsch
 *
 * @date 2016-09-12
 */
public enum BooleanConfigCodec implements UserConfigCodec<Boolean> {
    PushMessage("pm", Boolean.TRUE), //消息集合，除了聊天以外的所有push
    ;

    private final Boolean defaultValue;

    private final String nameSpace;

    private final boolean removeWhileDefaultValue;

    /**
     * @param nameSpace
     */
    BooleanConfigCodec(String nameSpace) {
        this.nameSpace = nameSpace;
        this.defaultValue = Boolean.FALSE;
        this.removeWhileDefaultValue = false;
    }

    /**
     * @param defaultValue
     * @param nameSpace
     */
    BooleanConfigCodec(String nameSpace, Boolean defaultValue) {
        this.defaultValue = defaultValue;
        this.nameSpace = nameSpace;
        this.removeWhileDefaultValue = false;
    }

    /**
     * @param defaultValue
     * @param nameSpace
     * @param removeWhileDefaultValue
     */
    BooleanConfigCodec(Boolean defaultValue, String nameSpace, boolean removeWhileDefaultValue) {
        this.defaultValue = defaultValue;
        this.nameSpace = nameSpace;
        this.removeWhileDefaultValue = removeWhileDefaultValue;
    }

    @Override
    public String getNamespace() {
        return nameSpace;
    }

    @Override
    public Boolean readValue(String node) {
        if (node == null) {
            return defaultValue();
        } else {
            return BooleanUtils.toBooleanObject(node);
        }
    }

    @Override
    public Boolean defaultValue() {
        return defaultValue;
    }

    @Override
    public String writeValue(Object value) {
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    @Override
    public boolean removeOnDefaultValue() {
        return removeWhileDefaultValue;
    }

    public static BooleanConfigCodec fromNameSpace(String nameSpace) {
        for (BooleanConfigCodec codec : values()) {
            if (StringUtils.equals(codec.getNamespace(), nameSpace)) {
                return codec;
            }
        }
        return null;
    }
}
