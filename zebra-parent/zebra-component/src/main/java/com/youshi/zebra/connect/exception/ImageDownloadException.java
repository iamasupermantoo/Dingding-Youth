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
public class ImageDownloadException extends DoradoException {

    private static final long serialVersionUID = -7753173292789310790L;

    private final String url;

    public ImageDownloadException(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
