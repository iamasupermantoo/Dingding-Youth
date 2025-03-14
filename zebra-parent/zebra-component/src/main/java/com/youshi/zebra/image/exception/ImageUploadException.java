package com.youshi.zebra.image.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class ImageUploadException extends DoradoMetaCodeException {

    private static final long serialVersionUID = 3946139298241003183L;

    public ImageUploadException() {
        super();
    }

    /**
     * @param cause
     */
    public ImageUploadException(Throwable cause) {
        super(cause);
    }

	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.ImageUploadError;
	}

}
