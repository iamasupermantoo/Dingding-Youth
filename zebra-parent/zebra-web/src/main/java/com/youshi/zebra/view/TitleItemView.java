package com.youshi.zebra.view;

import com.youshi.zebra.audio.utils.AudioUtils;
import com.youshi.zebra.exam.model.TitleItem;
import com.youshi.zebra.image.utils.ImageUtils;
import com.youshi.zebra.media.constants.MediaType;

public class TitleItemView {
	private TitleItem delegate;

	public TitleItemView(TitleItem delegate) {
		this.delegate = delegate;
	}

	public ImageView getImage() {
		Integer imageId = delegate.getImageId();
		if (imageId == null) {
			return null;
		}
		return new ImageView(ImageUtils.getImage(imageId));
	}

	public String getAudio() {
		Integer audioId = delegate.getAudioId();
		if (audioId == null) {
			return null;
		}
		return AudioUtils.getUrl(AudioUtils.getAudio(audioId));
	}

	public String getText() {
		return delegate.getText();
	}

	public int getType() {
		return delegate.getType();
	}
}
