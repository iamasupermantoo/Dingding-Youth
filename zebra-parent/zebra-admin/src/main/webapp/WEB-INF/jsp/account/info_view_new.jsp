<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>


<title>个人基本信息</title>
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
		<small>
			基本信息
			<i class="ace-icon fa fa-angle-double-right"></i>
			个人基本信息
		</small>
	</h1>
</div><!-- /.page-header -->

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<form class="form-horizontal" role="form">
			<!-- #section:elements.form -->
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="username-txt"> 登录名 </label>

				<div class="col-sm-9">
					<input type="text" disabled="disabled" class="col-xs-10 col-sm-5" id="username-txt" value="${details.username }" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="username-txt"> 角色 </label>

				<div class="col-sm-9">
					<input type="text" disabled="disabled"  class="col-xs-10 col-sm-5" id="username-txt" value="<c:out value="${zweb:hasPrivilege('Godlike') ? '超级管理员':'普通管理员' }"></c:out>" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="name-txt"> 创建日期 </label>
				<input type="hidden" id="aid-hid" value="${details.aid }" />
				<div class="col-sm-9">
					<input type="text" disabled="disabled"  class="col-xs-10 col-sm-5" id="username-txt" value="${zweb:datetime(details.createTime) }" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="name-txt"> 姓名 </label>
				<input type="hidden" id="aid-hid" value="${details.aid }" />
				<div class="col-sm-9">
					<input type="text" id="name-txt" value = "${details.name }" placeholder="姓名" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="email-txt"> 邮箱 </label>

				<div class="col-sm-9">
					<input type="text" id="email-txt" placeholder="请输入邮箱" value="${details.email }" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="mobile-txt"> 手机号 </label>

				<div class="col-sm-9">
					<input type="text" id="mobile-txt" placeholder="请输入手机号" value="${details.mobile }" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="mobile-txt"> 座机 </label>

				<div class="col-sm-9">
					<input type="text" id="mobile-txt" value="010-10101919" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="remark-txt"> 职务 </label>

				<div class="col-sm-9">
					<input type="text" id="mobile-txt" value="打杂的" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			<%-- <div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="remark-txt"> 省份 </label>

				<div class="col-sm-9">
					<input type="text" id="mobile-txt" value="${details.school.province }" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="remark-txt"> 学校名称 </label>

				<div class="col-sm-9">
					<input type="text" disabled="disabled" id="mobile-txt" value="${details.school.name }" class="col-xs-10 col-sm-5" />
				</div>
			</div> --%>
			
			<div class="clearfix form-actions">
				<div class="col-md-offset-3 col-md-9">
					<button class="btn" type="reset">
						<i class="ace-icon fa fa-undo bigger-110"></i>
						清空
					</button>

					&nbsp; &nbsp; &nbsp;
					<button id="update-btn" class="btn btn-info" type="button">
						<i class="ace-icon fa fa-check bigger-110"></i>
						修改
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
	var scripts = [null,"/assets/js/jquery.inputlimiter.1.3.1.min.js"]
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
