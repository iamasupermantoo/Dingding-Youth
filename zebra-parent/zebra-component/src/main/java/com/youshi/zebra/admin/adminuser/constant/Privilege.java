package com.youshi.zebra.admin.adminuser.constant;

import com.youshi.zebra.admin.adminuser.annotation.PrivilegeRequired;

/**
 * 每一项，代表一个权限。将“后台用户”和“权限”关联，然后再将权限和“接口”关联，就可以对用户做精确的访问控制，即：有相应的权限，才能访问特定的接口
 * 
 * Note：
 * 粒度尽量精确到每个细小的操作，如：查询用户列表、封禁用户、修改用户信息。但是，为了方便，可以将多个小操作合并为一个，
 * 如：把“封禁用户”和“修改用户信息”作为一个权限（写操作），而把“查询用户列表”作为另外一个权限（读操作）。
 * 
 * @author wangsch
 * @date 2016-09-12
 * 
 * @see PrivilegeRequired
 */
public enum Privilege {
	/**
	 * 是一个特殊的、最高的权限：用户有了这个权限，则访问任何接口不受限制。
	 * 相应的，接口标记为这个权限，则要求用户必须拥有{@link Privilege#Godlike}才能执行这个操作，通常是权限要求非常高的接口，如：添加后台管理员。
	 */
	Godlike("最高权限", "拥有这个权限，将能够执行系统内任何操作"),
	
	AdminUserAdmin("后台用户管理", "可以对后台用户进行CRUD操作、权限操作"),
	AdminUserDisable("后台用户封禁", "可以对后台用户进行CRUD操作"),
	AdminUserEnable("后台用户解除封禁", "可以对后台用户进行CRUD操作"),
	
	UserAdmin("用户管理", "可以对用户进行CRUD操作"),
	UserDisable("封禁用户", "可以对用户进行“封禁”操作"),
	UserEnable("解除封禁", "可以对用户进行“解除封禁”操作"),
	
	AdminLogAdmin("后台操作日志管理"),
	
	// 课程管理
	BookAdmin("教材管理"),
	CourseMetaAdmin("课程管理"),
	TeacherReaction("教师反馈"),
	StudentReaction("学生评价"),
	ExamAdmin("评测管理"),
	
	// 订单管理
	PreOrderAdmin("预约单管理"),
	OrderAdmin("订单管理"),
	FundAdmin("财务管理"),
	CourseAdmin("上课管理"),
	CourseMonitor("上课管理"),
	TryCourse("试听课管理"),
	
	// 统计
	ACQAdmin("用户注册"),
	ACTAdmin("用户活跃"),
	RETAdmin("用户存留"),
	;
	private String name;
	private String description;

	Privilege(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	Privilege(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
}
