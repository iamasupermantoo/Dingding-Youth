package com.youshi.zebra.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * 文本颜色
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class TextColor {

    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TextColor.class);

    private static Pattern colorPattern = Pattern.compile("^\\((\\d+),(\\d+),(\\d+),(\\d+)\\)$"); // ^\((\d+),(\d+),(\d+),(\d+)\)$

    private final int red;

    private final int green;

    private final int blue;

    private final int alpha;

    /**
     * @param red
     * @param green
     * @param blue
     * @param alpha
     */
    private TextColor(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public static TextColor of(int red, int green, int blue, int alpha) {
        if (checkRange(red) && checkRange(green) && checkRange(blue) && checkAlphaRange(alpha)) {
            return new TextColor(red, green, blue, alpha);
        } else {
            logger.warn("invalid text color:{},{},{},{}", red, green, blue, alpha);
            return null;
        }
    }

    public static TextColor of(String data) {
        if (data == null) {
            return null;
        }
        Matcher matcher = colorPattern.matcher(data);
        if (matcher.matches()) {
            int red = NumberUtils.toInt(matcher.group(1));
            int green = NumberUtils.toInt(matcher.group(2));
            int blue = NumberUtils.toInt(matcher.group(3));
            int alpha = NumberUtils.toInt(matcher.group(4));
            if (checkRange(red) && checkRange(green) && checkRange(blue) && checkAlphaRange(alpha)) {
                return new TextColor(red, green, blue, alpha);
            } else {
                logger.warn("invalid text color:{}", data);
                return null;
            }
        } else {
            return null;
        }
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }

    @Override
    public String toString() {
        return "(" + red + "," + green + "," + blue + "," + alpha + ")";
    }

    private static final boolean checkRange(int r) {
        return r >= 0 && r <= 255;
    }

    private static final boolean checkAlphaRange(int r) {
        return r >= 0 && r <= 255;
    }

}
