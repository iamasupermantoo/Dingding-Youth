package com.youshi.zebra.book.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.dorado.framework.crud.model.EntityStatus;

/**
 * 教材状态
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
public enum BookStatus implements EntityStatus{
	NOT_USED(0, "未被使用"),
	IN_USE(1, "正常使用"),
	AdminDel(3, "管理员删除"),
	;
	
	private final int value;
	private final String name;
	BookStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    public String getName() {
    	return name;
    }

    private static final IntObjectMap<BookStatus> map = new IntObjectOpenHashMap<>();
    static {
        for (BookStatus e : BookStatus.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final BookStatus fromValue(Integer value) {
        return map.get(value);
    }
}