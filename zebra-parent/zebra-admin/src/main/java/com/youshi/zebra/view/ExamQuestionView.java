package com.youshi.zebra.view;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.exam.dao.ExamQuestionDAO.OptionsMode;
import com.youshi.zebra.exam.model.ExamQuestionModel;
import com.youshi.zebra.exam.model.OptionItemWrapper;
import com.youshi.zebra.exam.model.TitleItem;

public class ExamQuestionView {
	private ExamQuestionModel delegate;
	private ZebraBuildContext context;

	public ExamQuestionView(ExamQuestionModel delegate, ZebraBuildContext context) {
		this.delegate = delegate;
		this.context = context;
	}

	public String getId() {
		return delegate.getUuid();
	}

	public OptionsMode getMode() {
		return OptionsMode.fromValue(delegate.getOptionsMode());
	}
	
	public String getRightAnswer() {
		return delegate.getRightAnswer();
	}

	public List<TitleItemView> getTitleItems() {
		List<TitleItem> items = delegate.getTitleItems();
		if (CollectionUtils.isEmpty(items)) {
			return null;
		}
		List<TitleItemView> result = items.stream().map(TitleItemView::new).collect(Collectors.toList());
		return result;
	}

//	public List<OptionItemView> getOptionItems() {
//		List<OptionItem> items = delegate.getOptionItems();
//		if (CollectionUtils.isEmpty(items)) {
//			return null;
//		}
//		List<OptionItemView> result = items.stream().map(OptionItemView::new).collect(Collectors.toList());
//		return result;
//	}
	
	public List<OptionItemWrapperView> getOptionItems() {
		List<OptionItemWrapper> items = delegate.getOptionItems();
		if (CollectionUtils.isEmpty(items)) {
			return null;
		}
		List<OptionItemWrapperView> result = items.stream()
				.map(OptionItemWrapperView::new)
				.collect(Collectors.toList());
		return result;
	}
}
