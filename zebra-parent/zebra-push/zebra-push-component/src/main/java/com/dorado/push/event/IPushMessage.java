package com.dorado.push.event;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.push.utils.MessageBuilder;

/**
 * 
 * push消息结构定义
 * 
 * @author wangsch
 *
 * @date 2016年10月31日
 */
public interface IPushMessage {

	/**
	 * 
	 * 消息包含以下字段
	 * 
	 * @author wangsch
	 * @date 2016年10月31日
	 * 
	 * {@link MessageBuilder}
	 * 
	 */
    public enum PushKey {
    	/**
    	 * 消息体，消息文本内容
    	 */
        alert,
        
        /**
         * badge
         */
        badge,
        /**
         * action {@link PushType}
         */
        action,
        /**
         * gotoPage结构
         */
        gotoPage,
        /**
         * 额外的参数，如果需要的话，json结构
         */
        extra,
        
        /**
         * 消息id
         */
        id,
        /**
         * 用户id，用于客户端和自己的user id判断，不等则忽略。
         */
        user;
    }

    /**
     * 当{@link PushKey#extra}不为空，extra内部的key
     * @author wangsch
     *
     */
    public enum ExtraKey {
    }
    
    

    /**
     * push消息的动作类型
     * 
     * @author wangsch
     * @date 2016年10月31日
     */
    public enum PushAction {
    	/**
    	 * 显示在head上，点击后gotoPage
    	 */
    	GotoPage(0, "gotoPage"),
    	/**
    	 * 只显示在手机的head上
    	 */
    	Show(1, "仅显示消息"),
    	/**
    	 * 默默滴清一下客户端缓存
    	 */
        ClearCache(2, "清除客户端缓存"),
        /**
         * 默默滴调用一个服务端接口，上传crash log
         */
        CrashLog(3, "上传CrashLog")
        ;
    	
    	private final int value;
    	private final String name;
    	PushAction(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }
        public String getName() {
        	return name;
        }

        private static final IntObjectMap<PushAction> map = new IntObjectOpenHashMap<>();
        static {
            for (PushAction e : PushAction.values()) {
                map.put(e.getValue(), e);
            }
        }

        public static final PushAction fromValue(Integer value) {
            return map.get(value);
        }
    }

    public Integer getUser();

    public void set(PushKey property, Object value);

    public Object get(PushKey property);
}
