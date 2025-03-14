package com.youshi.zebra.admin.log.constants;

/**
 * 
 * 操作日志的类型，每一项代表管理员的一个特定操作，用于日志记录。
 * 
 * @author wangsch
 * @date 2016-09-12
 */
public enum AdminLogType {
	AdminLogin(0, "登录"),
	
	UserEnable(2, "解除封禁"),
	UserDisable(3, "封禁用户"),
    
    ConstantUpdate(11, "更新常量"),
    
    AdminUserAdd(15, "添加管理员"),
    AdminUserUpdate(16, "修改管理员信息"),
    AdminUserBlock(17, "封禁用户"),
    AdminUserUnblock(17, "解除封禁用户"),
    AdminPrivilegeUpdate(19, "更新权限"),
    
    SpammerDictAdd(39, "添加spam"),
    SpammerDictDelete(40, "删除spam"),
    
    ;

    private final int value;
    
    private final String name;

    AdminLogType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    
    public String getName() {
		return name;
	}

    private static final java.util.Map<Integer, AdminLogType> map = new java.util.HashMap<>();
    static {
        for (AdminLogType e : AdminLogType.values()) {
            map.put(e.getValue(), e);
        }
    }

    public static final AdminLogType fromValue(int status) {
        return map.get(status);
    }
}
