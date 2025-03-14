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

<style>
	.lesson-warn {
		border: 1px;
		border-color: red;
	
	}
</style>


<div class="page-header">
	<h1>
		助管管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			上课监控
		</small>
	</h1>
</div><!-- /.page-header -->


<div>
	<h3>说明</h3>
	<ul>
		<!-- <li>每10秒刷新，显示<b>当前时间</b>(假设为10:00)前后1小时内的直播课状态</li> -->
		<li><b>“即将上课”</b>代表，往后三个上课时间段内，将要进行的课程</li>
		<li><b>“已经上课”</b>代表，往前三个上课时间段内，已经开始或结束的课程</li>
		<li><b>“上课异常”</b>代表，应该是已经上课，但老师或学生直播状态有异常（如：老师不在线）</li>
	</ul>
	<hr />
</div>

<div class="col-xs-12 col-sm-15" style="padding-left: 0px;padding-right: 0px; margin-bottom: 20px">
	<div class="widget-box">
		<div class="widget-header">
			<h4 class="widget-title">上课概况</h4>
			<span class="widget-toolbar">
				<a href="#" data-action="collapse">
					<i class="ace-icon fa fa-chevron-up"></i>
				</a>
			</span>
		</div>

		<div class="widget-body">
			<div class="widget-main" style="font-size: 15px; ">
				<div style="width: 200px;height: 30px;display:inline-block;margin-right: 100px;"><span style="">当前时间：</span>
					<span>${zweb:datetime(info.currTime) }</span></div>
				<div style="width: 200px; height: 30px; display:inline-block;"><span>即将上课：</span><span>${info.nextCount }个</span></div>
				<div style="width: 200px; height: 30px; display:inline-block;"><span>已经上课：</span><span>${info.preCount }个</span></div>
				<div style="width: 200px; height: 30px; display:inline-block;"><span>上课异常：</span><span>${info.excepCount }个</span></div>
			</div>
		</div>
	</div>
</div><!-- /.span -->


<div class="row">
    <div class="col-xs-12">
    	<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
				<li id="next-li">
					<a data-toggle="tab" href="#home" flag="next" class="flag-a">
						即将上课
					</a>
				</li>

				<li id="pre-li">
					<a data-toggle="tab" href="#messages" flag="pre" class="flag-a">
						已经上课
					</a>
				</li>
			</ul>
			<input  type="hidden" id="flag-hid" value="${flag }" />
			
			<div class="tab-content">
				<div id="home" class="tab-pane fade in active">
					<table id="sample-table-1" class="table table-striped table-bordered table-hover">
                    <thead>
	                    <tr>
	                        <th>课程名</th>
	                        <th>学生</th>
	                        <th>教师</th>
	                        <th>上课日期</th>
	                        <th>上课时间</th>
	                        <th>课次名</th>
	                        <th>课次</th>
	                        <th>状态</th>
	                        <th>老师状态</th>
	                        <th>学生状态</th>
	                        <th>操作</th>
	                    </tr>
                    </thead>
                    
                    <c:forEach items="${nextLessons }" var="lesson">
                    	<tr>
	                    	<td>${lesson.course }</td>
	                    	<td>${lesson.student.name }</td>
	                    	<td>${lesson.teacher.name }</td>
	                    	<td>${lesson.date }</td>
	                    	<td>${lesson.time }</td>
	                    	<td>${lesson.label }</td>
	                    	<td>${lesson.cnt }</td>
	                    	<td>${zenum:lessonStatus(lesson.status) }</td>
	                    	<td>
	                    		<c:if test="${lesson.status != 2 }">
		                    		<c:choose>
		                    		<c:when test="${lesson.teacherOnlineStatus == 0 or empty lesson.teacherOnlineStatus}">
										<span class="label label-danger arrowed-in">不在线</span>
									</c:when>
									<c:when test="${lesson.teacherOnlineStatus == 1 }">
										<span class="label label-warning">已进入房间</span>
									</c:when>
									<c:when test="${lesson.teacherOnlineStatus == 2 }">
										<span class="label label-warning">已准备</span>
									</c:when>
									<c:when test="${lesson.teacherOnlineStatus == 3 }">
										<span class="label label-info arrowed-in-right arrowed">正在直播</span>
									</c:when>
									</c:choose>
								</c:if>
								<c:if test="${lesson.status == 2 }"><span class="label label-success arrowed">已完成</span></c:if>
							</td>
	                    	<td>
	                    		<c:if test="${lesson.status != 2 }">
	                    			<c:choose>
		                    		<c:when test="${lesson.studentOnlineStatus == 0 or empty lesson.studentOnlineStatus}">
										<span class="label label-danger arrowed-in">不在线</span>
									</c:when>
									<c:when test="${lesson.studentOnlineStatus == 1 }">
										<span class="label label-warning">已进入房间</span>
									</c:when>
									</c:choose>
	                    		</c:if>
	                    		<c:if test="${lesson.status == 2 }"><span class="label label-success arrowed">已完成</span></c:if>
	                    	</td>
	                    	<td>
	                    	<a href="#modal-form2" role="button" class="blue confirm-a view-a" s-mobile="${lesson.student.mobile }" 
									t-mobile="${lesson.teacher.mobile }" data-toggle="modal">
								查看电话
								</a>
	                    	</td>
                    	</tr>
                    </c:forEach>
                </table>
				</div>

				<div id="messages" class="tab-pane fade">
					<table id="sample-table-1" class="table table-striped table-bordered table-hover">
                    <thead>
	                    <tr>
	                        <th>课程名</th>
	                        <th>学生</th>
	                        <th>教师</th>
	                        <th>上课日期</th>
	                        <th>上课时间</th>
	                        <th>课次名</th>
	                        <th>课次</th>
	                        <th>状态</th>
	                        <th>老师状态</th>
	                        <th>学生状态</th>
	                        <th>操作</th>
	                    </tr>
                    </thead>
                    
                    <c:forEach items="${preLessons }" var="lesson">
                    	<tr>
	                    	<td>${lesson.course }</td>
	                    	<td>${lesson.student.name }</td>
	                    	<td>${lesson.teacher.name }</td>
	                    	<td>${lesson.date }</td>
	                    	<td>${lesson.time }</td>
	                    	<td>${lesson.label }</td>
	                    	<td>${lesson.cnt }</td>
	                    	<td>${zenum:lessonStatus(lesson.status) }</td>
	                    	<td>
	                    		<c:if test="${lesson.status != 2 }">
		                    		<c:choose>
		                    		<c:when test="${lesson.teacherOnlineStatus == 0 or empty lesson.teacherOnlineStatus}">
										<span class="label label-danger arrowed-in">不在线</span>
									</c:when>
									<c:when test="${lesson.teacherOnlineStatus == 1 }">
										<span class="label label-warning">已进入房间</span>
									</c:when>
									<c:when test="${lesson.teacherOnlineStatus == 2 }">
										<span class="label label-warning">已准备</span>
									</c:when>
									<c:when test="${lesson.teacherOnlineStatus == 3 }">
										<span class="label label-info arrowed-in-right arrowed">正在直播</span>
									</c:when>
									</c:choose>
								</c:if>
								<c:if test="${lesson.status == 2 }"><span class="label label-success arrowed">已完成</span></c:if>
							</td>
	                    	<td>
	                    		<c:if test="${lesson.status != 2 }">
	                    			<c:choose>
		                    		<c:when test="${lesson.studentOnlineStatus == 0 or empty lesson.studentOnlineStatus}">
										<span class="label label-danger arrowed-in">不在线</span>
									</c:when>
									<c:when test="${lesson.studentOnlineStatus == 1 }">
										<span class="label label-warning">已进入房间</span>
									</c:when>
									</c:choose>
	                    		</c:if>
	                    		<c:if test="${lesson.status == 2 }"><span class="label label-success arrowed">已完成</span></c:if>
	                    	</td>
	                    	<td>
	                    	<a href="#modal-form2" role="button" class="blue confirm-a view-a" s-mobile="${lesson.student.mobile }" 
									t-mobile="${lesson.teacher.mobile }" data-toggle="modal">
								查看电话
								</a>
	                    	</td>
                    	</tr>
                    </c:forEach>
                </table>
				</div>
			</div>
		</div>
	</div>
</div>


<div id="modal-form2" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">查看电话</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12 col-sm-7">
						<div class="form-group">
							<label for="t-mobile-span">老师电话</label>
							<div>
								<span id="t-mobile-span"></span>
							 </div>
						</div>
						
						<div class="form-group">
							<label for="s-mobile-span">学生电话</label>
							<div>
								<span id="s-mobile-span"></span>
							</div>
						</div>
						
					</div>
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm" data-dismiss="modal">
					<i class="ace-icon fa fa-times"></i>
					取消
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

function myrefresh() {
	location.href = '/#/lesson/admin/monitor/list?flag='+$('#flag-hid').val() + "&t="+new Date().getTime();
}

$(document).ready(function() {
	var flag = $('#flag-hid').val();
    if(flag == 'next') {
    	$('#next-li').addClass('active');
    	// $('#next-li').click();
    } else if(flag == 'pre') {
    	$('#pre-li').addClass('active');
    	// $('#pre-li').click();
    }
    
	$('.view-a').click(function() {
		$('#s-mobile-span').text($(this).attr('s-mobile'));
		$('#t-mobile-span').text($(this).attr('t-mobile'));
	});
	
	$('.flag-a').click(function() {
		$('#flag-hid').val($(this).attr('flag'));
	});
	
	// 10秒刷新一次
	// setTimeout('myrefresh()',10000);
});

</script>
