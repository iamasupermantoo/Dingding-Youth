<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/taglib.inc" %>
<title>分配权限</title>
<link rel="stylesheet" href="/assets/css/datepicker.css" />
<link rel="stylesheet" href="/assets/css/bootstrap-timepicker.css " />
<script type="text/javascript" src="/assets/js/date-time/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="/assets/js/date-time/bootstrap-timepicker.min.js"></script>
<!-- <script type="text/javascript" src="/static/privilege/privilege.js"></script> -->

<div class="page-header">
	<h1>
		<small>
			管理员管理
			<i class="ace-icon fa fa-angle-double-right"></i>
			分配权限
		</small>
	</h1>
</div><!-- /.page-header -->

<div class="col-xs-12 col-sm-15" style="padding-left: 0px;padding-right: 0px; margin-bottom: 20px">
	<div class="widget-box">
		<div class="widget-header">
			<h4 class="widget-title">用户信息</h4>
			<span class="widget-toolbar">
				<a href="#" data-action="collapse">
					<i class="ace-icon fa fa-chevron-up"></i>
				</a>
			</span>
		</div>

		<div class="widget-body">
			<div class="widget-main" style="font-size: 15px; ">
				<div style="width: 200px; height: 30px; display:inline-block;">
					<span style="">用户名：</span>
					<span>${user.name }</span>
				</div>
				<div style="width: 200px; height: 30px; display:inline-block;"><span>当前日期：</span><span>2016-11-11</span></div>
			</div>
		</div>
	</div>
</div><!-- /.span -->

<p>
	<button class="btn btn-white btn-default btn-round" id="save-btn">
		<a class="blue">保存权限</a>
	</button>
</p>

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<form id="privilege-form">
					<table id="privilege-plan-table" class="table table-striped table-bordered table-hover" style="margin-bottom: 10px;">
						<thead>
							<tr>
								<th>代号</th>
								<th>权限</th>
								<th>描述</th>
								<th style="width: 57px">有效期</th>
								<th style="width: 150px;">状态</th>
							</tr>
						</thead>
	
						<tbody>
								<input  type="hidden" name="aid" id="aid-hid" value="${user.aid }" />
								<c:forEach var="privilege" items="${privileges}">
								<tr>
									<td>
										${privilege.pri.name() }
										<input type="hidden" name="privileges" value="${privilege.pri.name() }" />
									</td>
									<td>
										<a class="label-a" href="#">${privilege.pri.name}</a>
									</td>
									<td>${privilege.pri.description }</td>
									<td>
										<input type="text" class="date-txt" name="expireTimes" value="${zweb:date(privilege.expiretime) }"/>
									</td>
									<td>
									<c:choose>
										<c:when test="${privilege.status.value == 0 }">
											<span class="label label-sm label-inverse arrowed-in">未获取</span>
										</c:when>
										<c:when test="${privilege.status.value == 1 }">
											<span class="label label-sm label-success">已获取</span>
										</c:when>
										<c:when test="${privilege.status.value == 2 }">
											<span class="label label-sm label-warning">已过期</span>
										</c:when>
									</c:choose>
									</td>
								</tr>
								</c:forEach>
						</tbody>
					</table>
				</form>
			</div><!-- /.span -->
		</div><!-- /.row --><!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->
<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

$('.date-txt').datepicker({
	format: 'yyyy-mm-dd',
	autoclose: true,
	todayHighlight: true
});

$('#save-btn').click(function() {
	var data = $('#privilege-form').serialize();
	$.ajax({
		url: '/adminuser/privilege/update',
		type: 'post',
		data: data,
		success: function(result, status, xhr) {
			meta = result.meta;
			if(meta.code == 1) {
				alert('保存权限成功');
				location.reload();
			} else {
				alert(meta.desc + 
						(meta.errInfo ? '\n\n信息:\n'+ meta.errInfo : ''));
			}
		}
	});
	
	return false;
});

</script>
