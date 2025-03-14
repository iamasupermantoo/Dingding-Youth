package com.youshi.zebra.view;

import com.youshi.zebra.audio.utils.AudioUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.image.utils.ImageUtils;
import com.youshi.zebra.media.constants.MediaType;
import com.youshi.zebra.media.model.MediaModel;
import com.youshi.zebra.video.utils.VideoUtils;

public class MediaView {
	private MediaModel delegate;
	private ZebraBuildContext context;

	/**
	 * @param delegate
	 * @param context
	 */
	public MediaView(MediaModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}

	public String getName() {
		return delegate.getName();
	}
	
	public String getDesc( ) {
		return delegate.getDesc();
	}
	
	public Integer getType() {
		return delegate.getType();
	}
	
	public String getMediaUrl() {
		MediaType type = MediaType.fromValue(delegate.getType());
		switch(type) {
		case Image:
			return ImageUtils.getImageUrl(context.getImage(delegate.getImageId()), 200, 200);
		case Audio:
			return AudioUtils.getUrl(context.getAudio(delegate.getAudioId()));
		case Video:
			return VideoUtils.getUrl(context.getVideo(delegate.getVideoId()));
		}
		return null;
	}
}