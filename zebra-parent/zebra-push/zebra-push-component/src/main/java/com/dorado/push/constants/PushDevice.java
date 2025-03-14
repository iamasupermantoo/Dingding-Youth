package com.dorado.push.constants;

import java.util.EnumSet;

/**
 * 
 * @author wangsch
 * @date 2016年10月31日
 */
public enum PushDevice {
    IOS, Android, 
    ;

    public static final EnumSet<PushDevice> ALL = EnumSet.of(IOS, Android);

}
