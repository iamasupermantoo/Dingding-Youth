package com.youshi.zebra.view;

import com.dorado.framework.crud.model.util.ModelUtils;
import com.youshi.zebra.config.model.UserConfigModel;

public class UserConfigView {
	private UserConfigModel model;
	
	public UserConfigView(UserConfigModel model) {
		this.model = model;
	}
	
	public String getPush() {
		return ModelUtils.getString(model, "push");
	}
}
