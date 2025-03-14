<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>信息编辑</title>
<link rel="stylesheet" href="/assets/css/datepicker.css" />
<link rel="stylesheet" href="/assets/css/bootstrap-timepicker.css " />
<script type="text/javascript" src="/assets/js/date-time/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="/assets/js/date-time/bootstrap-timepicker.min.js"></script>

<div class="page-header">
	<h1>
		基本信息
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			信息编辑
		</small>
	</h1>
</div><!-- /.page-header -->

<div class="col-xs-12 col-sm-15" style="padding-left: 0px;padding-right: 0px; margin-bottom: 20px">
	<div class="widget-box">
		<div class="widget-header" style="background-image: none; background: #dedede repeat-x; color: #181819">
			<h4 class="widget-title">账号信息</h4>
		</div>
		<div class="widget-body">
			<div class="widget-main" style="font-size: 14px; ">
				<table border="0" style="margin-left: 20px;">
					<tr>
						<td style="width: 200px; padding-left: 50px;">登录账号</td>
						<td style="width: 200px;">root&nbsp;&nbsp;&nbsp;&nbsp;<a href="#/account/password" style="font-size: 12px;">修改密码</a></td>
						<td style="width: 200px; padding-left: 50px;">角色类型</td>
						<td style="width: 200px;">超级管理员</td>
					</tr>
					
					<tr>
						<td style="width: 200px; padding-left: 50px;">创建时间</td>
						<td style="width: 200px;">${zweb:datetime(details.createTime) }</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>

<div class="col-xs-12 col-sm-15" style="padding-left: 0px;padding-right: 0px; margin-bottom: 20px">
	<div class="widget-box">
		<div class="widget-header" style="background-image: none; background: #dedede repeat-x; color: #181819">
			<h4 class="widget-title">基本信息</h4>
			<a href="javascript:void(0);" id="save-a"  style="float:right;margin-right: 20px;margin-top: 10px;">保存</a>
		</div>
		<div class="widget-body">
			<div class="widget-main" style="font-size: 14px; ">
				<form action="" id="update-form">
					<table border="0" style="margin-left: 20px;">
						<tr>
							<td style="width: 200px; padding-left: 50px;">姓名</td>
							<td style="width: 200px;"><input type="text" name="name" value="${details.name }" style="height: 25px;" /> </td>
							
							<td style="width: 200px; padding-left: 50px;">手机号</td>
							<td style="width: 200px;"><input type="text" value="${details.mobile }" name="mobile" style="height: 25px;" /></td>
						</tr>
						
						<tr>
							<td style="width: 200px; padding-left: 50px;">email</td>
							<td style="width: 200px;">
							<input type="text" value="${details.email }" name="email" style="height: 25px; margin-top: 5px;" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

$(document).ready(function() {
	$('#save-a').click(function() {
		$.ajax({
			url: '/account/update',
			type: 'post',
			data: $('#update-form').serialize(),
			success: function(result, status, xhr) {
				meta = result.meta;
				if(meta.code == 1) {
					alert('修改成功');
					location.href = '#/account/info';
				} else {
					alert(meta.desc + 
							(meta.errInfo ? '\n\n信息:\n'+ meta.errInfo : ''));
				}
			}
		});
	});
});

</script>
