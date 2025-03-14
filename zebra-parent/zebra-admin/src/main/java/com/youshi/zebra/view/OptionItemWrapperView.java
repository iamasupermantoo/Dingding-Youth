package com.youshi.zebra.view;

import java.util.List;
import java.util.stream.Collectors;

import com.youshi.zebra.audio.utils.AudioUtils;
import com.youshi.zebra.exam.model.OptionItem;
import com.youshi.zebra.exam.model.OptionItemWrapper;
import com.youshi.zebra.image.utils.ImageUtils;
import com.youshi.zebra.media.constants.MediaType;

public class OptionItemWrapperView {
	private OptionItemWrapper delegate;

	public OptionItemWrapperView(OptionItemWrapper delegate) {
		this.delegate = delegate;
	}

	public String getLabel() {
		return delegate.getLabel();
	}
	
	public List<OptionItemView> getItems() {
		return delegate.getItems().stream().map(OptionItemView::new).collect(Collectors.toList());
	}

	public class OptionItemView {
		private OptionItem delegate;
		
		public OptionItemView(OptionItem delegate) {
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
		
		public MediaType getType() {
			return MediaType.fromValue(delegate.getType());
		}
	}
}