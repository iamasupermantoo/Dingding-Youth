package com.youshi.zebra.admin.adminuser.constant;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 
 * 
 * @author wangsch
 * @date 2017年2月24日
 */
public enum AdminUserPassportStatus implements EntityStatus {
    	Normal(0, "正常"),
    	Blocked(2, ""),
//    	AdminDel(1, "已删除")
    	;
    	
    	private final int value;
    	private final String name;
    	AdminUserPassportStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }
        public String getName() {
        	return name;
        }

        private static final IntObjectMap<AdminUserPassportStatus> map = new IntObjectOpenHashMap<>();
        static {
            for (AdminUserPassportStatus e : AdminUserPassportStatus.values()) {
                map.put(e.getValue(), e);
            }
        }

        public static final AdminUserPassportStatus fromValue(Integer value) {
            return map.get(value);
        }
    }