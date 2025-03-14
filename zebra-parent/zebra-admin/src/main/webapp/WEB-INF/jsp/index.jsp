<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>鼎鼎少年管理平台</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
<link rel="stylesheet" href="/assets/css/bootstrap.min.css" />
<link rel="stylesheet" href="/assets/css/font-awesome.min.css" />
<link rel="stylesheet" href="/assets/css/ace-fonts.css" />
<link rel="stylesheet" href="/assets/css/ace.min.css" id="main-ace-style" />
<link rel="stylesheet" href="/assets/css/ace-skins.min.css" />
<link rel="stylesheet" href="/assets/css/ace-rtl.min.css" />
<link rel="stylesheet" href="/assets/css/jquery-ui.min.css" />
<link rel="stylesheet" href="/common/common.css" />

<script src="/assets/js/jquery.min.js"></script>
<script src="/assets/js/jquery-ui.min.js"></script>
<script src="/assets/js/bootstrap.min.js"></script>
<script src="/assets/js/jquery.dataTables.min.js"></script>
<script src="/assets/js/jquery.dataTables.bootstrap.js"></script>
<script src="/assets/js/ace-extra.min.js"></script>
<script src="/assets/js/ace-elements.min.js"></script>
<script src="/assets/js/ace.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	ace.enable_ajax_content(jQuery, {
	  content_url: function(url) {
		return url;
	  }
	});
	
	$('#logout-a').click(function() {
    	$.ajax({
			url: '/logout',
			type: 'post',
			success: function(result, status, xhr) {
				meta = result.meta;
				if(meta.code == 1) {
					location.href = '/';
				} else {
					alert(meta.desc + 
							(meta.errInfo ? '\n\n信息:\n'+ meta.errInfo : ''));
				}
			}
		});
    	
    	return false;
    });
	
	$('#dashboard-li').click();
	
	// 菜单
    $('.menu-1').each(function(idx, ele) {
    	var menu1 = $(this);
    	var submenus = menu1.find('.submenu li').size();
    	if(submenus == 0) {
    		menu1.hide();
    	}
    });
	
});
</script>

<style type="text/css">
.table>tbody>tr>td, .table>tbody>tr>th, .table>tfoot>tr>td, .table>tfoot>tr>th, .table>thead>tr>td, .table>thead>tr>th {
	vertical-align: middle;
}

</style>

</head>

<body class="no-skin">
<!-- 1. 导航栏 -->
<div id="navbar" class="navbar navbar-default">
	<script type="text/javascript">
		try{ace.settings.check('navbar' , 'fixed')}catch(e){}
	</script>

	<div class="navbar-container" id="navbar-container">
		<button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler">
			<span class="sr-only">Toggle sidebar</span>

			<span class="icon-bar"></span>

			<span class="icon-bar"></span>

			<span class="icon-bar"></span>
		</button>

		<div class="navbar-header pull-left">
			<a href="#" class="navbar-brand">
				<small>
					<!-- <i class="fa fa-leaf"></i> -->
					鼎鼎少年管理平台
				</small>
			</a>
		</div>
		<!-- 导航栏 -> 当前登陆者头像&下拉框 -->
		<div class="navbar-buttons navbar-header pull-right" role="navigation">
			<ul class="nav ace-nav">
				<li class="light-blue">
					<a data-toggle="dropdown" href="#" class="dropdown-toggle">
						<span class="user-info">
							${passport.username }
						</span>

						<i class="ace-icon fa fa-caret-down"></i>
					</a>

					<ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
						<li>
							<a href="javascript:void(0);" id="logout-a">
								<i class="ace-icon fa fa-power-off"></i>
								安全退出
							</a>
						</li>
						
						<li>
							<a href="#/account/password">
                                <i class="ace-icon fa fa-asterisk"></i>
                                个人密码修改
                            </a>
						</li>
					</ul>
				</li>
			</ul>
		</div><!-- END 导航栏 -> 当前登陆者头像&下拉框 -->
	</div>
</div><!-- END 导航栏 -->

<div class="main-container" id="main-container">
	<script type="text/javascript">
		try{ace.settings.check('main-container' , 'fixed')}catch(e){}
	</script>
	
	<div id="sidebar" class="sidebar responsive">
		<script type="text/javascript">
			try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
		</script>
		
		<!-- 操作菜单，根据用户权限决定是否展示 -->
		<ul class="nav nav-list">
			<li class="menu-1">
				<a href="#" class="dropdown-toggle">
					<i class="menu-icon ace-icon glyphicon glyphicon-user"></i>
					<span class="menu-text"> 用户管理 </span>

					<b class="arrow fa fa-angle-down"></b>
				</a>

				<b class="arrow"></b>

				<ul class="submenu">
					<li class="">
						<a href="#/teacher/admin/list" data-url="/teacher/admin/list">
							<i class="menu-icon fa fa-caret-right"></i>
							教师管理
						</a>

						<b class="arrow"></b>
					</li>
					<li class="">
						<a href="#/student/admin/list" data-url="/student/admin/list">
							<i class="menu-icon fa fa-caret-right"></i>
							用户管理
						</a>

						<b class="arrow"></b>
					</li>
					<li class="">
						<a href="#/scholarship/admin/retain/record/list" data-url="/scholarship/admin/retain/record/list">
							<i class="menu-icon fa fa-caret-right"></i>
							提现申请
						</a>

						<b class="arrow"></b>
					</li>
					
					<!-- <li class="">
						<a href="#/homework/admin/list" data-url="/homework/admin/list">
							<i class="menu-icon fa fa-caret-right"></i>
							作业管理
						</a>

						<b class="arrow"></b>
					</li> -->
				</ul>
			</li>
			
			
			<li class="menu-1">
				<a href="#" class="dropdown-toggle">
					<i class="menu-icon fa fa-heart"></i>
					<span class="menu-text"> 推荐管理 </span>
					<b class="arrow fa fa-angle-down"></b>
				</a>
				
				<b class="arrow"></b>
				<ul class="submenu">
					<li class="">
						<a href="#/recommend/admin/trials?type=TRY_COURSE" data-url="/recommend/admin/query/feed?type=TRY_COURSE">
							<i class="menu-icon fa fa-caret-right"></i>
							首页内容管理
						</a>
						<b class="arrow"></b>
					</li>
					
					<li class="">
						<a href="#/recommend/admin/query/banner?type=APP" data-url="/recommend/admin/query/banner?type=APP">
							<i class="menu-icon fa fa-caret-right"></i>
							首页焦点图
						</a>

						<b class="arrow"></b>
					</li>
				</ul>
			</li>
			
			<li class="menu-1">
				<a href="#" class="dropdown-toggle">
					<i class="menu-icon fa fa-book"></i>
					<span class="menu-text"> 课程管理 </span>

					<b class="arrow fa fa-angle-down"></b>
				</a>

				<ul class="submenu">
					<li class="">
						<a href="#/book/admin/list" data-url="/book/admin/list">
							<i class="menu-icon fa fa-caret-right"></i>
							教材管理
						</a>

						<b class="arrow"></b>
					</li>
					<li class="">
						<a href="#/course/meta/admin/list" data-url="/course/meta/admin/list">
							<i class="menu-icon fa fa-caret-right"></i>
							课程管理
						</a>

						<b class="arrow"></b>
					</li>
					
					<li class="">
						<a href="#/opencourse/meta/admin/list" data-url="/opencourse/meta/admin/list">
							<i class="menu-icon fa fa-caret-right"></i>
							公开课管理
						</a>

						<b class="arrow"></b>
					</li>
					
					
				</ul>
			</li>
			
			
			<li class="menu-1">
				<a href="#" class="dropdown-toggle">
					<i class="menu-icon fa fa-th"></i>
					<span class="menu-text"> 上课管理 </span>

					<b class="arrow fa fa-angle-down"></b>
				</a>

				<b class="arrow"></b>
				
				<ul class="submenu">
					<li class="">
						<a href="#/course/admin/list" data-url="/course/admin/list">
							<i class="menu-icon fa fa-caret-right"></i>
							排课管理
						</a>

						<b class="arrow"></b>
					</li>
					
					<li class="">
						<a href="#/lesson/admin/monitor/list" data-url="/lesson/admin/monitor/list">
							<i class="menu-icon fa fa-caret-right"></i>
							上课监控
						</a>

						<b class="arrow"></b>
					</li>
					
					<li class="">
						<a href="#/try/course/admin/list" data-url="/try/course/admin/list">
							<i class="menu-icon fa fa-caret-right"></i>
							试听课管理
						</a>

						<b class="arrow"></b>
					</li>
					
					<li class="">
						<a href="#/opencourse/meta/admin/paidList" data-url="/opencourse/meta/admin/paidList">
							<i class="menu-icon fa fa-caret-right"></i>
							已购直播
						</a>

						<b class="arrow"></b>
					</li>
					
				</ul>
				
			</li>
			
			
			
			<li class="menu-1">
				<a href="#" class="dropdown-toggle">
					<i class="menu-icon fa fa-wordpress"></i>
					<span class="menu-text"> 课后管理 </span>

					<b class="arrow fa fa-angle-down"></b>
				</a>

				<b class="arrow"></b>
				
				<ul class="submenu">
					
					<li class="">
						<a href="#/reaction/admin/list/byteacher" data-url="/reaction/admin/list/byteacher">
							<i class="menu-icon fa fa-caret-right"></i>
							教师反馈
						</a>

						<b class="arrow"></b>
					</li>
					
					<li class="">
						<a href="#/reaction/admin/list/bystudent" data-url="/reaction/admin/list/bystudent">
							<i class="menu-icon fa fa-caret-right"></i>
							学生评价
						</a>

						<b class="arrow"></b>
					</li>
					
					<li class="">
						<a href="#/exam/admin/list" data-url="/exam/admin/list">
							<i class="menu-icon fa fa-caret-right"></i>
							测评管理
						</a>

						<b class="arrow"></b>
					</li>
					
				</ul>
				
			</li>
			
			
			
			<li class="menu-1">
				<a href="#" class="dropdown-toggle">
					<i class="menu-icon fa fa-bell"></i>
					<span class="menu-text"> 订单管理 </span>

					<b class="arrow fa fa-angle-down"></b>
				</a>

				<b class="arrow"></b>

				<ul class="submenu">
					<li class="">
						<a href="#/order/admin/list?status=USER_COMMITED" data-url="/order/admin/list?status=USER_COMMITED">
							<i class="menu-icon fa fa-caret-right"></i>
							预约单管理
						</a>

						<b class="arrow"></b>
					</li>
					
					<li class="">
						<a href="#/order/admin/query" data-url="/order/admin/query">
							<i class="menu-icon fa fa-caret-right"></i>
							订单管理
						</a>

						<b class="arrow"></b>
					</li>
					<li class="">
						<a href="#/product/admin/charge/item/list" data-url="/product/admin/charge/item/list">
							<i class="menu-icon fa fa-caret-right"></i>
							充值项管理
						</a>

						<b class="arrow"></b>
					</li>
					<li class="">
						<a href="#/finance/admin/query" data-url="/finance/admin/query">
							<i class="menu-icon fa fa-caret-right"></i>
							财务管理
						</a>

						<b class="arrow"></b>
					</li>
					
					
				</ul>
			</li>
			
			<li class="menu-1">
				<a href="#" class="dropdown-toggle">
					<i class="menu-icon fa fa-database"></i>
					<span class="menu-text"> 数据统计 </span>
					<b class="arrow fa fa-angle-down"></b>
				</a>
				<b class="arrow"></b>
				<ul class="submenu">
						<li class="">
							<a href="#/stats/user/acquisition/list" data-url="/stats/user/acquisition/list">
								<i class="menu-icon fa fa-caret-right"></i>
								用户注册
							</a>
	
							<b class="arrow"></b>
						</li>
					
						<li class="">
							<a href="#/stats/user/activation/list" data-url="/stats/user/activation/list">
								<i class="menu-icon fa fa-caret-right"></i>
								用户活跃
							</a>
	
							<b class="arrow"></b>
						</li>
					
						<li class="">
							<a href="#/stats/user/retention/list?flag=normal" data-url="/stats/user/retention/list?flag=normal">
								<i class="menu-icon fa fa-caret-right"></i>
								用户存留
							</a>
	
							<b class="arrow"></b>
						</li>
				</ul>
			</li>
			
			<li class="menu-1">
				<a href="#" class="dropdown-toggle">
					<i class="menu-icon fa fa-cog"></i>
					<span class="menu-text"> 系统管理 </span>

					<b class="arrow fa fa-angle-down"></b>
				</a>

				<b class="arrow"></b>

				<ul class="submenu">
					<li class="">
						<a href="#/adminuser/list" data-url="/adminuser/list">
							<i class="menu-icon fa fa-caret-right"></i>
							管理员列表
						</a>

						<b class="arrow"></b>
					</li>

					<li class="">
						<a href="#/adminlog/list" data-url="/adminlog/list">
							<i class="menu-icon fa fa-caret-right"></i>
							操作日志
						</a>

						<b class="arrow"></b>
					</li>
				</ul>
			</li>
			
			
			
			
			
		</ul><!-- END 操作菜单 -->

		<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
			<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" 
				data-icon2="ace-icon fa fa-angle-double-right"></i>
		</div>
		<script type="text/javascript">
			try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
		</script>
	</div>
	
	<!-- 主内容区域 -->
	<div class="main-content">
		<div class="page-content">
			<div class="page-content-area"></div>
		</div>
	</div><!-- END 主内容区域 -->
	
	<!-- 底部footer -->
	<div class="footer">
		<div class="footer-inner">
			<!-- #section:basics/footer -->
			<div class="footer-content">
				<span class="bigger-120">
					<span class="blue bolder">鼎鼎少年管理平台</span>
					&copy; 2016-2017
				</span>
			</div>

			<!-- /section:basics/footer -->
		</div>
	</div><!-- END 底部footer -->

	<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
		<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
	</a>
</div>
</body>
</html>
