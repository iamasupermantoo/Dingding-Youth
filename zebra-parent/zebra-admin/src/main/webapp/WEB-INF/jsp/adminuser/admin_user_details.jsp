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
			管理员编辑
		</small>
	</h1>
</div><!-- /.page-header -->

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<form class="form-horizontal" role="form">
			<!-- #section:elements.form -->
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="name-txt"> <font color="red">* </font>姓名 </label>
				<input type="hidden" id="aid-hid" value="${details.aid }" />
				<div class="col-sm-9">
					<input type="text" id="name-txt" value = "${details.name }" placeholder="Username" class="col-xs-10 col-sm-5" />
				</div>
			</div>

			<div class="space-4"></div>

			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="username-txt"> <font color="red">* </font>登录账号 </label>

				<div class="col-sm-9">
					<input readonly="" type="text" class="col-xs-10 col-sm-5" id="username-txt" value="${details.name }" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="email-txt"> email </label>

				<div class="col-sm-9">
					<input type="text" id="email-txt" placeholder="Email" value="${details.email }" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="mobile-txt"> 手机号 </label>

				<div class="col-sm-9">
					<input type="text" id="mobile-txt" placeholder="Mobile" value="${details.mobile }" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="remark-txt"> 备注 </label>

				<div class="col-sm-9">
					<textarea class="col-xs-10 col-sm-5 limited" id="remark-txt" maxlength="50">${details.remark }</textarea>
				</div>
			</div>

			<div class="clearfix form-actions">
				<div class="col-md-offset-3 col-md-9">
					<button id="update-btn" class="btn btn-info" type="button">
						<i class="ace-icon fa fa-check bigger-110"></i>
						修改
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

<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
  <script src="/assets/js/excanvas.min.js"></script>
<![endif]-->
<script type="text/javascript">
	var scripts = [null,"/assets/js/jquery-ui.custom.min.js","/assets/js/jquery.ui.touch-punch.min.js","/assets/js/chosen.jquery.min.js","/assets/js/fuelux/fuelux.spinner.min.js","/assets/js/date-time/bootstrap-datepicker.min.js","/assets/js/date-time/bootstrap-timepicker.min.js","/assets/js/date-time/moment.min.js","/assets/js/date-time/daterangepicker.min.js","/assets/js/date-time/bootstrap-datetimepicker.min.js","/assets/js/bootstrap-colorpicker.min.js","/assets/js/jquery.knob.min.js","/assets/js/jquery.autosize.min.js","/assets/js/jquery.inputlimiter.1.3.1.min.js","/assets/js/jquery.maskedinput.min.js","/assets/js/bootstrap-tag.min.js","/assets/js/typeahead.jquery.min.js", null]
	ace.load_ajax_scripts(scripts, function() {
		jQuery(function($) {
			$('textarea.limited').inputlimiter({
				remText : '还可输入%n个字',
				limitText : '最多允许%n字'
			});
		});
	});
	
	$('#update-btn').click(function() {
		var aid = $('#aid-hid').val();
		var name = $('#name-txt').val();
		var username = $('#username-txt').val();
		var email = $('#email-txt').val();
		var mobile = $('#mobile-txt').val();
		var remark = $('#remark-txt').val();
		
		$.ajax({
			url: '/adminuser/update',
			type: 'post',
			data: {'aid': aid, 'name':name, 'username': username,'email': email, 'mobile': mobile, 
				'remark': remark},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					alert('修改成功');
					location.href = '/#/adminuser/list';
				} else {
					alert(result.desc||result.errorInfo);
				}
			}
		});
		
		return false;
	});
</script>
