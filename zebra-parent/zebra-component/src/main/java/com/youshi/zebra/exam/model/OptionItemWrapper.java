package com.youshi.zebra.exam.model;

import java.util.List;

public class OptionItemWrapper {
	private String label;
	
	private List<OptionItem> items;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<OptionItem> getItems() {
		return items;
	}

	public void setItems(List<OptionItem> items) {
		this.items = items;
	}
}