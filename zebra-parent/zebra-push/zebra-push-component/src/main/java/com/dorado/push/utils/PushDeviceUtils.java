package com.dorado.push.utils;

import com.dorado.mvc.reqcontext.Platform;
import com.dorado.push.constants.PushDevice;

public class PushDeviceUtils {
	public static PushDevice getDevice(Platform platform) {
		PushDevice device = null;
		switch(platform) {
		case Android:
			device=PushDevice.Android;
			break;
		case IOS:
			device=PushDevice.IOS;
			break;
		default :
			throw new IllegalArgumentException("Unknown device: "+device);
		}
		return device;
	}
}
