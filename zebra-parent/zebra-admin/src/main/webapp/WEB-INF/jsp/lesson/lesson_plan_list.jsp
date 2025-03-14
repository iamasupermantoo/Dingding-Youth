<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>课次安排</title>
<link rel="stylesheet" href="/assets/css/datepicker.css" />
<link rel="stylesheet" href="/assets/css/bootstrap-timepicker.css " />
<script type="text/javascript" src="/assets/js/date-time/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="/assets/js/date-time/bootstrap-timepicker.min.js"></script>
<!-- <script type="text/javascript" src="/static/lesson/lesson.js"></script> -->

<div class="page-header">
	<h1>
		助管管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			排课操作
		</small>
	</h1>
</div><!-- /.page-header -->

<div class="col-xs-12 col-sm-15" style="padding-left: 0px;padding-right: 0px; margin-bottom: 20px">
	<div class="widget-box">
		<div class="widget-header">
			<h4 class="widget-title">课程信息</h4>
			<span class="widget-toolbar">
				<a href="#" data-action="collapse">
					<i class="ace-icon fa fa-chevron-up"></i>
				</a>
			</span>
		</div>

		<div class="widget-body">
			<div class="widget-main" style="font-size: 15px; ">
				<div style="width: 200px; height: 30px; display:inline-block;">
					<span style="">课程名：</span>
					<span>${course.name }</span>
				</div>
				<div style="width: 200px; height: 30px; display:inline-block;"><span>状态：</span><span>${zenum:courseStatus(course.status) }</span></div>
				<div style="width: 200px; height: 30px; display:inline-block;"><span>学生：</span><span>${course.student }</span></div>
				<div style="width: 200px; height: 30px; display:inline-block;"><span>总课时：</span><span>${course.totalCnt }</span></div>
				
				<input type="hidden" value="${course.cid }" id="cid-hid"/>
				<input type="hidden" value="Lesson-${course.planedMaxCnt }" id="max-label-hid"/>
				<input type="hidden" value="${course.planedMaxCnt }" id="planed-max-cnt-hid"/>
				<input type="hidden" id="has-add-hid"/>
				
			</div>
		</div>
	</div>
</div><!-- /.span -->

<p>
	<button class="btn btn-white btn-default btn-round">
		<a class="blue" id="add-lesson-btn">添加</a>
	</button>
</p>

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<table id="lesson-plan-table" class="table table-striped table-bordered table-hover" style="margin-bottom: 10px;">
					<thead>
						<tr>
							<th style="width: 118px;">名称</th>
							<th>课次</th>
							<th style="width: 175px">老师</th>
							<th style="width: 57px">上课日期</th>
							<th style="width: 378px;">上课时间</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="lesson" items="${lessons.list}" varStatus="ss">
						<tr id="tr-${lesson.lid }">

							<td>
								<a class="label-a" href="#">${lesson.label}</a>
							</td>
							<td class="cnt-td">${lesson.cnt }</td>
							<td>
								<select class="teacher-sel" tid="${lesson.teacher.tid }" >
									<option value="">-- 请选择老师 --</option>
									<c:forEach items="${teachers }" var="teacher">
										<option value="${teacher.id }">${teacher.name }</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<input type="text" class="date-txt" value="${lesson.date }" />
							</td>
							<td>
								<input type="text" class="start-time-txt" value="${lesson.startTime }" disabled="disabled" />
								-
								<input type="text" class="end-time-txt" value="${lesson.endTime }" disabled="disabled" />&nbsp;
								<a role="button" class="time-range-a" onclick="selectTime(this);" 
									data-toggle="modal" style="color: red;">选择</a>
							</td>
							
							<td>${zenum:lessonStatus(lesson.status) }</td>
							<td>
								<a href="#" class="save-a" cid="${lesson.cid }" lid="${lesson.lid }">保存</a>
								<c:if test="${ss.index == 0 }">
									&nbsp;|&nbsp;
									<a href="javascript:void(0);" class="del-a" cid="${lesson.cid }" lid="${lesson.lid }">
										<font color="red">删除</font></a>
								</c:if>
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<table id="clone-table" style="display: none;">
					<tr id="clone-tr">
						<td>
							<a class="label-a" href="#"></a>
						</td>
						<td class="cnt-td">${course.planedMaxCnt }</td>
						<td>
							<select class="teacher-sel" >
								<option value="">-- 请选择老师 --</option>
								<c:forEach items="${teachers }" var="teacher">
									<option value="${teacher.id }">${teacher.name }</option>
								</c:forEach>
							</select>
						</td>
						<td>
							<input type="text" class="date-txt"/>
						</td>
						<td>
							<input type="text" class="start-time-txt" disabled="disabled" />
							-
							<input type="text" class="end-time-txt" disabled="disabled" />&nbsp;
							<a role="button" class="time-range-a" onclick="selectTime(this);" 
								data-toggle="modal" style="color: red;">选择</a>
						</td>
						<td></td>
						<td>
							<a href="#" class="save-a">保存</a>
							&nbsp;|&nbsp;
							<a href="javascript:void(0);" class="del-a"><font color="red">删除</font></a>
						</td>
					</tr>
				</table>
				<div>
					<ul class="pagination" style="margin-top: 0px;">
						<li class="${empty param.cursor ? 'disabled':''}">
							<a href="#/lesson/region/plan/list?cid=${param.cid }" data-url="/lesson/region/plan/list?cid=${param.cid }">
								<i class="ace-icon fa fa-angle-double-left">首页</i>
							</a>
						</li>
						<li class="${empty param.cursor ? 'disabled':''}">
							<a href="javascript:history.go(-1)">上一页</a>
						</li>
						
						<li class="${empty books.nextCursor ? 'disabled':'' }">
							<a href="#/lesson/region/plan/list?cid=${param.cid }&cursor=${books.nextCursor }" data-url="/lesson/region/plan/list?cid=${param.cid }&cursor=${books.nextCursor }">下一页</a>
						</li>
					</ul>
				</div>
			</div><!-- /.span -->
		</div><!-- /.row --><!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->

<div id="modal-table" class="modal fade" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header no-padding">
				<div class="table-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						<span class="white">&times;</span>
					</button>
					教师：<span id="teacher-name-span"></span>，日期：<span id="date-span"></span>
				</div>
			</div>
			<input id="cnt-hid"  type="hidden"/>
			<div class="modal-body no-padding">
				<table class="table table-striped table-bordered table-hover no-margin-bottom no-border-top" id="times-table">
					<thead>
						<tr>
							<th>时间段</th>
							<th>状态</th>
							<th>操作</th>
							
							<th>时间段</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="hour" begin="8" end="21">
							<tr>
								<td class="time-td">${hour }:00 - ${hour }:30</td>
								<td></td>
								<td></td>
								
								<td class="time-td">${hour }:30 - ${hour + 1}:00</td>
								<td></td>
								<td></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<div class="modal-footer no-margin-top">
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div>


<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

$(document).ready(function() {
	// 绑定事件
	bindDatetime();
	bindSave();

	// 回显教师下拉框
	$('#lesson-plan-table .teacher-sel').each(function(idx, ele) {
		$(ele).val($(ele).attr('tid'));
	});
	
	// 添加一节课
	$('#add-lesson-btn').click(function() {
		if($('#has-add-hid').val()) {
			alert("请先保存最新的排课");
			return;
		}
		
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
		bindDatetime(tr);
		bindSave(tr);
		bindDel(tr);
		
		// 标识控制
		$('#has-add-hid').val(true);
		
	});
	
	$('.del-a').click(function() {
		var result = confirm("确认删除吗？");
		if(!result) {
			return;
		}
				
		var cid = $(this).attr('cid');
		var lid = $(this).attr('lid');
		
		$.ajax({
			url: '/lesson/region/plan/remove',
			type: 'post',
			data: {'cid':cid, 'lid': lid},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					location.reload();
				} else {
					alert(result.meta.errorInfo || result.meta.desc);
				}
			}
		});
		
		return false;
	});
});

function bindDel(tr) {
	$(tr).find('.del-a').click(function() {
		$(this).parent().parent().remove();
		var cnt = parseInt($('#planed-max-cnt-hid').val()) - 1;
		$('#planed-max-cnt-hid').val(cnt);
		
		// 标识控制
		$('#has-add-hid').val('');
		return false;
	});
}


function bindSave(tr) {
	$(tr).find('.save-a').click(function() {
		var cid = $(this).attr('cid');
		var lid = $(this).attr('lid');
		
		var tr = $(this).parent().parent();
		var cnt = tr.find('.cnt-td').text();
		var label = tr.find('.label-a').text();
		var tid = tr.find('.teacher-sel').val();
		var date = tr.find('.date-txt').val();
		var start = tr.find('.start-time-txt').val();
		var end = tr.find('.end-time-txt').val();
		
		if(!date) {
			alert("请填写上课日期");
			return false;
		}
		if(!start || !end) {
			alert("上课开始或结束日期不能为空");
			return false;
		}
				
		var result = confirm("确认保存吗？");
		if(!result) {
			return;
		}
		
		if(lid) {
			$.ajax({
				url: '/lesson/region/plan/modify',
				type: 'post',
				data: {'cid':cid, 'lid': lid, 'tid': tid, 'cnt': cnt, 'label': label, 'date':date, 'start':start, 'end':end},
				success: function(result, status, xhr) {
					if(result.meta.code == 1) {
						location.reload();
					} else {
						alert(result.meta.errorInfo || result.meta.desc);
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
						alert("排课成功");
						location.reload();
					} else {
						alert(result.meta.errorInfo || result.meta.desc);
					}
				}
			});
		}
		
		return false;
	});
}

function bindDatetime(tr) {
	$(tr).find('.date-txt').datepicker({
		format: 'yyyy-mm-dd',
		autoclose: true,
		todayHighlight: true
	});
}

function selectTime(ele) {
	var tr = $(ele).parent().parent();
	var teacherSel = tr.find('.teacher-sel');
	var tid = teacherSel.val();
	if(!tid) {
		alert('请选择老师');
		return false;
	}
	var date = tr.find('.date-txt').val();
	if(!date) {
		alert('请选择日期');
		return false;
	}
	$('#teacher-name-span').text(teacherSel.find(':selected').text());
	$('#date-span').text(date);
	$("#modal-table").modal('show');
	
	// 记录是对哪个cnt的lesson操作
	var cnt = tr.find('.cnt-td').text();
	$('#cnt-hid').val(cnt);
	
	$.ajax({
		url: '/lesson/region/plan/times',
		type: 'get',
		data: {'tid': tid, 'date': date},
		success: function(result, status, xhr) {
			if(result.meta.code == 1) {
				var times = result.data.times;
				
				var trs = $('#times-table tr:gt(0)');
				trs.each(function(i, ele) {
					var timeTd = $(ele).find('td:eq(0)');
					var time = timeTd.text();
					var contains = $.inArray(time, times) != -1;
					setLine(timeTd, $(ele).find('td:eq(1)'), $(ele).find('td:eq(2)'), contains);
					
					timeTd = $(ele).find('td:eq(3)');
					time = timeTd.text();
					contains = $.inArray(time, times) != -1;
					setLine(timeTd, $(ele).find('td:eq(4)'), $(ele).find('td:eq(5)'), contains);
				});
			} else {
				alert(result.meta.errorInfo || result.meta.desc);
			}
			
			return false;
		}
	});
	
	return false;
}

function setLine(timeTd, statusTd, opTd, contains) {
	var statusHtml = contains ? '<span class="label label-sm label-inverse arrowed-in">已占用</span>' 
			: '<span class="label label-sm label-success">可选择</span>';
	var opHtml = contains ? '' : '<a class="select-a" href="javascript:void(0);" time-range="' + 
		timeTd.text() + '" onclick="setTimeRange(this);" >选择</a>';
	
	statusTd.empty(); opTd.empty();
	statusTd.append(statusHtml); opTd.append(opHtml);
}

function setTimeRange(ele) {
	var timeRange = $(ele).attr('time-range');
	var start = timeRange.split('-')[0].trim();
	var end = timeRange.split('-')[1].trim();
	var cnt = $('#cnt-hid').val();
	$('.cnt-td').each(function(idx, ele) {
		if($(ele).text() == cnt) {
			$(ele).parent().find('.start-time-txt').val(start);
			$(ele).parent().find('.end-time-txt').val(end);
		}
	});
	
	$("#modal-table").modal('hide');
	
}

</script>
