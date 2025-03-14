package com.youshi.zebra.user.constant;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public enum Gender {
    male(0, "男"), //
    female(1, "女"), //
    ;

    private final int value;
    
    private final String name;

    /**
     * @param value
     */
    Gender(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    
    public String getName() {
    	return name;
    }

    private static final java.util.Map<Integer, Gender> map = new java.util.HashMap<>();
    static {
        for (Gender e : Gender.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final Gender fromValue(Integer value) {
        return map.get(value);
    }
}
