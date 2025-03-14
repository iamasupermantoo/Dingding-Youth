package com.dorado.gotopage.constant;

import java.util.Map;

import com.dorado.mvc.reqcontext.AppVer;
import com.dorado.mvc.reqcontext.Platform;

/**
 * 
 * GotoPage类型
 * 
 * @author wangsch
 * @date 2016-10-12
 */
public enum GotoPage {
	COURSE(0, "1v1终端页"),
	OPEN_COURSE(1, "公开课终端页"),
	URL(2, "跳转到URL"),
	FEEDBACK(3, "feedback页面"),
	
	NONE(99, "NONE"),
	;
	
//    Message(1) {
//
//        @Override
//        public boolean support(Platform platform, AppVer appVer) {
//            return GotoPage.support(platform, appVer, ImmutableMap.<Platform, AppVer> builder()
//                    .put(Platform.iOS, AppVer.VER_INIT).put(Platform.Android, AppVer.VER_INIT).build());
//        }
//    }, // 消息列表
    ;

    private final int value;
    
    private final String name;

    /**
     * @param value
     */
    GotoPage(int value, String name) {
        this.value = value;
        this.name= name;
    }

    public int getValue() {
        return value;
    }
    
    public String getName() {
		return name;
	}

    private static final java.util.Map<Integer, GotoPage> map = new java.util.HashMap<>();

    static {
        for (GotoPage e : GotoPage.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final GotoPage fromValue(int status) {
        return map.get(status);
    }

    
    public static boolean support(Platform platform, AppVer appVer, Map<Platform, AppVer> pairs) {
        AppVer supportVersion = pairs.get(platform);
        if (supportVersion == null) {
            return false;
        }
        if (appVer == null) {
            return false;
        }
        return appVer.compareTo(supportVersion) >= 0;
    }

}
