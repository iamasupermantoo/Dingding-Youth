package com.youshi.zebra.user.config.util.codec;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.user.config.util.UserConfigCodec;
import com.youshi.zebra.user.utils.MatchFriendConfigUtils;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public enum LongConfigCodec implements UserConfigCodec<Long> {
    matchType("mt", MatchFriendConfigUtils.ALL)//
    ;

    private final Long defaultValue;

    private final String nameSpace;

    private final boolean removeWhileDefaultValue;

    LongConfigCodec(String nameSpace, Long defaultValue) {
        this.nameSpace = nameSpace;
        this.defaultValue = defaultValue;
        this.removeWhileDefaultValue = false;
    }

    @Override
    public String getNamespace() {
        return nameSpace;
    }

    @Override
    public Long readValue(String node) {
        if (node == null) {
            return defaultValue();
        } else {
            return NumberUtils.toLong(node);
        }
    }

    @Override
    public Long defaultValue() {
        return defaultValue;
    }

    @Override
    public String writeValue(Object value) {
        if (value == null) {
            return null;
        } else {
            return DoradoMapperUtils.toJSON(value);
        }
    }

    @Override
    public boolean removeOnDefaultValue() {
        return removeWhileDefaultValue;
    }

    public static LongConfigCodec fromNameSpace(String nameSpace) {
        for (LongConfigCodec codec : values()) {
            if (StringUtils.equals(codec.getNamespace(), nameSpace)) {
                return codec;
            }
        }
        return null;
    }
}
