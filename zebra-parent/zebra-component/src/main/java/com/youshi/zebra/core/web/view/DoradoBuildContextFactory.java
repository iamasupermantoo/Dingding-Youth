package com.youshi.zebra.core.web.view;

import java.util.function.Supplier;


/**
 * @author wangsch
 * @date 2016-09-13
 */
public class DoradoBuildContextFactory implements Supplier<ZebraBuildContext>{

	@Override
	public ZebraBuildContext get() {
		return new ZebraBuildContext();
	}
	
}
