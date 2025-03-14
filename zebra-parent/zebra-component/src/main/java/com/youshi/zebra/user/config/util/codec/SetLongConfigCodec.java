package com.youshi.zebra.user.config.util.codec;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.dorado.framework.utils.DoradoMapperUtils;
import com.youshi.zebra.user.config.util.UserConfigCodec;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public enum SetLongConfigCodec implements UserConfigCodec<Set<Long>> {
    IgnoreGroupIds("igi."), //
    ;

    private final String nameSpace;

    private final Set<Long> defaultValue;

    private final boolean removeWhileDefaultValue;

    SetLongConfigCodec(String nameSpace) {
        this.nameSpace = nameSpace;
        this.defaultValue = Collections.emptySet();
        this.removeWhileDefaultValue = false;
    }

    SetLongConfigCodec(String nameSpace, Set<Long> defaultValue) {
        this.nameSpace = nameSpace;
        this.defaultValue = defaultValue;
        this.removeWhileDefaultValue = false;
    }

    /**
     * @param nameSpace
     * @param defaultValue
     * @param removeWhileDefaultValue
     */
    SetLongConfigCodec(String nameSpace, Set<Long> defaultValue, boolean removeWhileDefaultValue) {
        this.nameSpace = nameSpace;
        this.defaultValue = defaultValue;
        this.removeWhileDefaultValue = removeWhileDefaultValue;
    }

    @Override
    public String getNamespace() {
        return nameSpace;
    }

    @Override
    public Set<Long> readValue(String node) {
        if (node == null) {
            return defaultValue();
        } else {
            return DoradoMapperUtils.fromJSON(node, Set.class, Long.class);
        }
    }

    @Override
    public Set<Long> defaultValue() {
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

    public static SetLongConfigCodec fromNameSpace(String nameSpace) {
        for (SetLongConfigCodec codec : values()) {
            if (StringUtils.equals(codec.getNamespace(), nameSpace)) {
                return codec;
            }
        }
        return null;
    }
}
