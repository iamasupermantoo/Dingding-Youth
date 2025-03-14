package com.youshi.zebra.reaction.constants;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public enum Acceptance {
		Acceptance_1(1, "20% - 40%"),
		Acceptance_2(2, "40% - 60%"),
		Acceptance_3(3, "60% - 80%"),
		Acceptance_4(4, "80% - 100%")
		;
		
		private final int value;
		private final String name;
		Acceptance(int value, String name) {
	        this.value = value;
	        this.name = name;
	    }

	    public int getValue() {
	        return value;
	    }
	    public String getName() {
	    	return name;
	    }

	    private static final IntObjectMap<Acceptance> map = new IntObjectOpenHashMap<>();
	    static {
	        for (Acceptance e : Acceptance.values()) {
	            map.put(e.getValue(), e);
	        }
	    }

	    public static final Acceptance fromValue(Integer value) {
	        return map.get(value);
	    }
	}