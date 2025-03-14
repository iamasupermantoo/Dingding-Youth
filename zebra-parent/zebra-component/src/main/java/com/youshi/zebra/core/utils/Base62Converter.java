package com.youshi.zebra.core.utils;

/**
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public class Base62Converter {

    private static final String ELEMENTS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int BASE = 62;

    public static String encode(long decimalNumber) {
        if (decimalNumber == 0) {
            return "0";
        }
        String result = "";
        long mod = 0;

        while (decimalNumber != 0) {
            mod = decimalNumber % BASE;
            result = ELEMENTS.substring((int) mod, (int) mod + 1) + result;
            decimalNumber = (decimalNumber - mod) / BASE;
        }

        return result;
    }

    public static long decode(String value) {
        long result = 0;
        for (int i = 0; i < value.length(); i++) {
            int num = ELEMENTS.indexOf(value.charAt(i));
            result += num * Math.pow(BASE, value.length() - (i + 1));
        }
        return result;
    }
}
