package com.youshi.zebra.view;

import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.youshi.zebra.admin.adminuser.constant.Privilege;

public class PrivilegeView {

	public enum PrivilegeStatus {
		NONE(0, "无权限"), NORMAL(1, "正常"), EXPIRED(2, "已过期");

		private final int value;
		private final String name;

		PrivilegeStatus(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public String getName() {
			return name;
		}

		private static final IntObjectMap<PrivilegeStatus> map = new IntObjectOpenHashMap<>();
		static {
			for (PrivilegeStatus e : PrivilegeStatus.values()) {
				map.put(e.getValue(), e);
			}
		}

		public static final PrivilegeStatus fromValue(Integer value) {
			return map.get(value);
		}
	}

	private int id;

	private boolean checked;

	private Privilege pri;

	private long expiretime;

	private PrivilegeStatus status;

	public PrivilegeView(boolean checked, Privilege pri, PrivilegeStatus status) {
		this.checked = checked;
		this.pri = pri;
		this.status = status;
	}

	public PrivilegeView(int id, boolean checked, Privilege pri, long expiretime, PrivilegeStatus status) {
		this.id = id;
		this.checked = checked;
		this.pri = pri;
		this.expiretime = expiretime;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public boolean isChecked() {
		return checked;
	}

	public Privilege getPri() {
		return pri;
	}

	public long getExpiretime() {
		return expiretime;
	}

	public PrivilegeStatus getStatus() {
		return status;
	}

}