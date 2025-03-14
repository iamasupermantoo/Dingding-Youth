package com.youshi.zebra.audio.exception;

import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.exception.base.DoradoMetaCodeException;

/**
 * 音频上传失败
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public class AudioUploadException extends DoradoMetaCodeException {

    public AudioUploadException() {
        super();
    }

    /**
     * @param cause
     */
    public AudioUploadException(Throwable cause) {
        super(cause);
    }

	@Override
	public MetaCode getMetaCode() {
		return ZebraMetaCode.AudioUploadError;
	}

}
