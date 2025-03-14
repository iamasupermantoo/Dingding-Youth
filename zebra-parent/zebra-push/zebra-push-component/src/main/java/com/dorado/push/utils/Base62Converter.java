package com.dorado.push.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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

    public static long decodeSinaMid(String value) {
        String result = "";
        while (StringUtils.isNotBlank(value)) {
            // 取右边四个解码 4->7
            String part = StringUtils.right(value, 4);
            value = StringUtils.substring(value, 0, StringUtils.lastIndexOf(value, part));
            String num = Long.toString(decode(part));
            if (part.length() == 4 && num.length() < 7) {
                num = StringUtils.leftPad(num, 7, "0");
            }
            //System.err.println(num);
            result = num + result;
        }
        //System.out.println(result);
        return NumberUtils.toLong(result);
    }

    public static String encodeSinaMID(String id) {
        String result = "";
        while (StringUtils.isNotBlank(id)) {
            // 右边取7个来编码 7->4
            String part = StringUtils.right(id, 7);
            id = StringUtils.substring(id, 0, StringUtils.lastIndexOf(id, part));
            String num = encode(NumberUtils.toLong(part));
            // 如果part有很多0
            if (part.length() == 7 && num.length() < 4) {
                // 最后开头的部分不足7位的就不用管了
                num = StringUtils.leftPad(num, 4, "0");
            }
            result = num + result;
        }
        return result;
    }
}
