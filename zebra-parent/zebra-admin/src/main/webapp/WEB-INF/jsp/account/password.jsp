<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>

<title>修改密码</title>

<link rel="stylesheet" href="/assets/css/jquery-ui.custom.min.css" />
<link rel="stylesheet" href="/assets/css/chosen.css" />
<link rel="stylesheet" href="/assets/css/bootstrap-timepicker.css" />
<link rel="stylesheet" href="/assets/css/daterangepicker.css" />
<link rel="stylesheet" href="/assets/css/bootstrap-datetimepicker.css" />
<link rel="stylesheet" href="/assets/css/colorpicker.css" />
<script type="text/javascript" src="/assets/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="/assets/js/additional-methods.min.js"></script>

<style type="text/css">

.in-valid {
	margin-right: 20px;
}

</style>


<!-- ajax layout which only needs content area -->
<div class="page-header">
	<h1>
		管理员
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			修改密码
		</small>
	</h1>
</div><!-- /.page-header -->

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<form class="form-horizontal" id="update-form" role="form" action="/account/password" method="post">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right" for="password"> <font color="red">* </font> 旧密码: </label>

				<div class="col-sm-9">
					<input type="password" id="password" name="password" class="col-sm-5 in-valid" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right" for="newPassword"> <font color="red">* </font>新密码: </label>

				<div class="col-sm-9">
					<input type="password" id="newPassword" name="newPassword" class="col-sm-5 in-valid" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right" for="confirmPassword"> <font color="red">* </font>确认密码: </label>

				<div class="col-sm-9">
					<input type="password" id="confirmPassword" name="confirmPassword" class="col-sm-5 in-valid" />
				</div>
			</div>
			
			<div class="clearfix form-actions">
				<div class="col-md-offset-3 col-md-9">
					<button id="update-btn" class="btn btn-info" type="submit">
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
<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

// 表单校验
$('#update-btn').click(function() {
	$('#update-form').validate({
		errorElement: 'span',
		errorClass: 'help-block',
		focusInvalid: false,
		rules: {
			password: {
				required: true,
			},
			newPassword: {
				required: true,
			},
			confirmPassword: {
				required: true,
				equalTo: '#newPassword'
			}
		},

		messages: {
			password: '请输入旧密码',
			newPassword: '请输入新密码',
			confirmPassword: {
				required: '请输入确认密码',
				equalTo: '确认密码和新密码不一致'
			},
		},


		highlight: function (e) {
			$(e).closest('.form-group').removeClass('has-info').addClass('has-error');
		},

		success: function (e) {
			$(e).closest('.form-group').removeClass('has-error');//.addClass('has-info');
			$(e).remove();
		},

		errorPlacement: function (error, element) {
			if(element.is('input[type=checkbox]') || element.is('input[type=radio]')) {
				var controls = element.closest('div[class*="col-"]');
				if(controls.find(':checkbox,:radio').length > 1) controls.append(error);
				else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
			}
			else if(element.is('.select2')) {
				error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
			}
			else if(element.is('.chosen-select')) {
				error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
			}
			else error.insertAfter(element);
		},

		submitHandler: function (form) {
			$.ajax({
				url: '/account/password',
				type: 'post',
				data: $('#update-form').serialize(),
				success: function(result, status, xhr) {
					meta = result.meta;
					if(meta.code == 1) {
						alert("修改成功");
						location.reload();
					} else {
						alert(meta.desc + 
								(meta.errInfo ? '\n\n信息:\n'+ meta.errInfo : ''));
					}
				}
			});
			return false;
		},
		invalidHandler: function (form) {
		}
	});
});



</script>
