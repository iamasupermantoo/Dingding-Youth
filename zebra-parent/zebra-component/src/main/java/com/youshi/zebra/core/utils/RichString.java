package com.youshi.zebra.core.utils;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class RichString {

    public static final RichString EMPTY = new RichString("");

    private final String value;

    /**
     * @param value
     */
    private RichString(String value) {
        this.value = value;
    }

    public static RichString wrap(String string) {
        return new RichString(string);
    }

    public String getRaw() {
        return value;
    }

    public String getHtml() {
        return toString();
    }

    @Override
    public String toString() {
        if (value == null) {
            return "";
        } else {
            return EmojiConverter.convertHtml(value);
        }
    }

}
