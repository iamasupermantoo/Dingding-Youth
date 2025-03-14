package com.youshi.zebra.view;

import java.util.Map;

public class GotoPageView {
	
	private Integer type;
	
	private Map<String, Object> params;
	
	public GotoPageView(Integer type, Map<String, Object> params) {
		this.type = type;
		this.params = params;
	}
	
    public Integer getType() {
    	return type;
    }
    
    public Map<String, Object> getParams() {
    	return params;
    }

}