package com.youshi.zebra.connect.exception;

import com.youshi.zebra.exception.base.DoradoException;

/**
 * 
* 
* Date: May 10, 2016
* 
 * @author wangsch
 *
 */
public class ImageNotExistsException extends DoradoException {

    private static final long serialVersionUID = -7753173292789310790L;

    private final String imageId;

    public ImageNotExistsException(String imageId) {
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }

}
