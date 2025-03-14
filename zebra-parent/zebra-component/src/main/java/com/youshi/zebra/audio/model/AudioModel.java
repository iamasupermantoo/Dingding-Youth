package com.youshi.zebra.audio.model;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;

/**
 * 
 * @author wangsch
 * @date 2017年2月7日
 */
public class AudioModel extends AbstractModel<Integer>{
	public enum AudioKeys {
		/** 文件格式，如：mp3 */
        format,
        
        /** bytes size */
        size,
        
        /** 长度，秒 */
        length,
        
        /** 作者 */
        author,
    }
    

	public AudioModel(Integer id, String data, long createTime, int status) {
		super(id, data, createTime, status);
	}

	public Integer getLength() {
        return ModelUtils.getInt(this, AudioKeys.length.name());
    }
	
	public String getFormat() {
		return ModelUtils.getString(this, AudioKeys.format);
	}
}
