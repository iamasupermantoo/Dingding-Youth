package com.youshi.zebra.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.youshi.zebra.audio.utils.AudioUtils;
import com.youshi.zebra.exam.model.OptionItem;
import com.youshi.zebra.exam.model.OptionItemWrapper;
import com.youshi.zebra.image.utils.ImageUtils;

public class OptionItemView2 {
	private OptionItem delegate;

	public OptionItemView2(OptionItem delegate) {
		this.delegate = delegate;
	}

	public String getLabel() {
		return delegate.getLabel();
	}
	
	public List<OptionItemView> getItems() {
		return Arrays.asList(new OptionItemView());
	}

	public class OptionItemView {
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
}