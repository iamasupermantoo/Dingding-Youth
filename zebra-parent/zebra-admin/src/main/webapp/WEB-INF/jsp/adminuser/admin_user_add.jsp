<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>

<title>Form Elements - Ace Admin</title>

<link rel="stylesheet" href="/assets/css/jquery-ui.custom.min.css" />
<link rel="stylesheet" href="/assets/css/chosen.css" />
<link rel="stylesheet" href="/assets/css/datepicker.css" />
<link rel="stylesheet" href="/assets/css/bootstrap-timepicker.css" />
<link rel="stylesheet" href="/assets/css/daterangepicker.css" />
<link rel="stylesheet" href="/assets/css/bootstrap-datetimepicker.css" />
<link rel="stylesheet" href="/assets/css/colorpicker.css" />

<!-- ajax layout which only needs content area -->
<div class="page-header">
	<h1>
		管理员列表
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			管理员列表
		</small>
	</h1>
</div><!-- /.page-header -->

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<form class="form-horizontal" role="form">
			<!-- #section:elements.form -->
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="name-txt"><font color="red">* </font> 姓名 </label>

				<div class="col-sm-9">
					<input type="text" id="name-txt" placeholder="Username" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="name-txt"> <font color="red">* </font>密码 </label>

				<div class="col-sm-9">
					<input type="password" id="password-pwd" placeholder="Username" class="col-xs-10 col-sm-5" />
				</div>
			</div>

			<div class="space-4"></div>

			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="username-txt"> <font color="red">* </font>登录账号 </label>

				<div class="col-sm-9">
					<input type="text" id="username-txt" placeholder="Username" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> email </label>

				<div class="col-sm-9">
					<input type="text" id="form-field-1" placeholder="Username" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 手机号 </label>

				<div class="col-sm-9">
					<input type="text" id="form-field-1" placeholder="Username" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 备注 </label>

				<div class="col-sm-9">
					<textarea class="col-xs-10 col-sm-5 limited" id="form-field-9" maxlength="50"></textarea>
				</div>
			</div>

			<div class="clearfix form-actions">
				<div class="col-md-offset-3 col-md-9">
					<button id="add-btn" class="btn btn-info" type="button">
						<i class="ace-icon fa fa-check bigger-110"></i>
						添加
					</button>

					&nbsp; &nbsp; &nbsp;
					<button class="btn" type="reset">
						<i class="ace-icon fa fa-undo bigger-110"></i>
						清空
					</button>
				</div>
			</div><!-- /.row -->
		</form>

		<!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->
<script type="text/javascript">
	var scripts = []
	ace.load_ajax_scripts(scripts, function() {
		jQuery(function($) {
			$('textarea.limited').inputlimiter({
				remText : '还可输入%n个字',
				limitText : '最多允许%n字'
			});
		});
	});
	
	$('#add-btn').click(function() {
		var name = $('#name-txt').val();
		var password = $('#password-pwd').val();
		var username = $('#username-txt').val();
		var email = $('#email-txt').val();
		var mobile = $('#mobile-txt').val();
		var remark = $('#remark-txt').val();
		
		$.ajax({
			url: '/adminuser/add',
			type: 'post',
			data: {'name':name, 'username': username, 'password': password,'email': email, 'mobile': mobile, 'remark': remark},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					location.href = '/#/adminuser/list';
				} else {
					alert(result.desc||result.errorInfo);
				}
			}
		});
		
		return false;
	});
	
</script>
