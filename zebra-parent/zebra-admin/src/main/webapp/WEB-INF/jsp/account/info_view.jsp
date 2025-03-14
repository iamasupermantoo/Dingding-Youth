<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>我的信息</title>
<link rel="stylesheet" href="/assets/css/datepicker.css" />
<link rel="stylesheet" href="/assets/css/bootstrap-timepicker.css " />
<script type="text/javascript" src="/assets/js/date-time/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="/assets/js/date-time/bootstrap-timepicker.min.js"></script>

<div class="page-header">
	<h1>
		<small>
			基本信息
			<i class="ace-icon fa fa-angle-double-right"></i>
			个人基本信息
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
						<td style="width: 200px;">${details.username }&nbsp;&nbsp;&nbsp;&nbsp;<a href="#/account/password" style="font-size: 12px;">修改密码</a></td>
						<td style="width: 200px; padding-left: 50px;">角色类型</td>
						<td style="width: 200px;"><c:out value="${zweb:hasPrivilege('Godlike') ? '超级管理员':'普通管理员' }"></c:out> </td>
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
			<a href="#/account/update" style="float:right;margin-right: 20px;margin-top: 10px;">修改</a>
		</div>
		<div class="widget-body">
			<div class="widget-main" style="font-size: 14px; ">
				<table border="0" style="margin-left: 20px;">
					<tr>
						<td style="width: 200px; padding-left: 50px;">姓名</td>
						<td style="width: 200px;">${details.name }</td>
						
						<td style="width: 200px; padding-left: 50px;">手机号</td>
						<td style="width: 200px;">${details.mobile }</td>
					</tr>
					
					<tr>
						<td style="width: 200px; padding-left: 50px;">email</td>
						<td style="width: 200px;">${details.email }</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

$(document).ready(function() {
	// 绑定事件
	bindDatetime();
	bindSave();

	// 添加一节课
	$('#add-lesson-btn').click(function() {
		var courseId = $('#cid-hid').val();
		var cnt = parseInt($('#planed-max-cnt-hid').val()) + 1;
		var label = "Lesson-" + cnt;
		
		// alert('cid: ' + courseId + ', cnt: '+ cnt + ', label: ' + label);
		var tr = $('#clone-table').find('tr').clone();
		
		tr.find('.save-a').attr('cid', courseId);
		tr.find('.label-a').text(label);
		tr.find('.cnt-td').text(cnt);
		$('#lesson-plan-table tbody').prepend(tr);
		$('#planed-max-cnt-hid').val(cnt);
		
		// 事件
		bindDatetime();
		bindSave();
	});
});

function bindSave() {
	$('.save-a').click(function() {
		var cid = $(this).attr('cid');
		var lid = $(this).attr('lid');
		
		var tr = $(this).parent().parent();
		var cnt = tr.find('.cnt-td').text();
		var label = tr.find('.label-a').text();
		var tid = tr.find('.tid-hid').val();
		var date = tr.find('.date-txt').val();
		var start = tr.find('.start-time-txt').val();
		var end = tr.find('.end-time-txt').val();
		
		// alert('cnt: ' + cnt + ', label: ' + label + ', tid: ' + tid +', date:'+date+', start: ' + start +', end: ' + end );
		
		// TODO 校验
		
		var result = confirm("确认保存吗？");
		if(!result) {
			return;
		}
		
		if(lid){
			$.ajax({
				url: '/lesson/region/plan/modify',
				type: 'post',
				data: {'cid':cid, 'lid': lid, 'tid': tid, 'cnt': cnt, 'label': label, 'date':date, 'start':start, 'end':end},
				success: function(result, status, xhr) {
					if(result.meta.code == 1) {
						location.href = '/#/book/admin/list';
					} else {
						alert(result.desc||result.errorInfo);
					}
				}
			});
		} else {
			$.ajax({
				url: '/lesson/region/plan/add',
				type: 'post',
				data: {'cid':cid, 'tid': tid, 'cnt': cnt, 'label': label, 'date':date, 'start':start, 'end':end},
				success: function(result, status, xhr) {
					if(result.meta.code == 1) {
						location.href = '/#/book/admin/list';
					} else {
						alert(result.desc||result.errorInfo);
					}
				}
			});
		}
		
		return false;
	});
}

function bindDatetime() {
	$('.date-txt').datepicker({
		format: 'yyyy-mm-dd',
		autoclose: true,
		todayHighlight: true
	});

	$('.start-time-txt,.end-time-txt').timepicker({
		minuteStep: 1,
	    showSeconds: false,
	    showMeridian: false,
	    defaultTime: false
	});
}


</script>
