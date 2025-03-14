package com.youshi.zebra.view;

import com.youshi.zebra.audio.utils.AudioUtils;
import com.youshi.zebra.book.model.FrameItem;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.image.model.ImageModel;
import com.youshi.zebra.media.constants.MediaType;
import com.youshi.zebra.media.model.MediaModel;
import com.youshi.zebra.video.utils.VideoUtils;

public class FrameItemView {
	private FrameItem delegate;
	private ZebraBuildContext context;
	private MediaModel media;

	public FrameItemView(FrameItem delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
		this.media = context.getMedia(delegate.getMediaId());
	}
	
	public Integer getType() {
		return delegate.getType();
	}

	public String getVideo() {
		if (media.getType() == MediaType.Video.getValue()) {
			return VideoUtils.getUrl(context.getVideo(media.getVideoId()));
		}
		return null;
	}

	public ImageModel getImage() {
		if (media.getType() == MediaType.Image.getValue()) {
			return context.getImage(media.getImageId());
		}
		return null;
	}

	public String getAudio() {
		if (media.getType() == MediaType.Audio.getValue()) {
			return AudioUtils.getUrl(context.getAudio(media.getAudioId()));
		}
		return null;
	}
	
	public boolean getIsChild() {
		return delegate.getIsChild();
	}
	
	public int getIdx() {
		return delegate.getIdx();
	}
	
}