package com.youshi.zebra.core.web.taglib;

import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.image.utils.ImageUtils;

/**
 * 
 * @author wangsch
 * @date 2017年3月16日
 */
public class ImageFunction {
    public static String imageUrl(ImageModel image, int w, int h) {
        return ImageUtils.getImageUrl(image, w, h);
    }
}
