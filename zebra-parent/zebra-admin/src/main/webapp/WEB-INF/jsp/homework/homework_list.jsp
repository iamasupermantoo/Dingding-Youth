<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>作业列表</title>
<link rel="stylesheet" href="/assets/css/chosen.css " />
<script type="text/javascript" src="/assets/js/chosen.jquery.min.js"></script>
<script type="text/javascript" src="/static/homework/homework.js"></script>

<style type="text/css">
table {
	border-collapse: collapse;
}

table td {
	padding: 3px;
}

.td-label {
	text-align: right;
}

.td-input {
	text-align: left;
	padding-right: 10px;
}

.q-input {
	padding: 3px;
}

.group {
	font-size: 20px;
}

.rea-label {
	font-weight:bold;
}


</style>


<div class="page-header">
	<h1>
		作业管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			作业列表
		</small>
	</h1>
</div><!-- /.page-header -->

<div>
<form id="query-form" class="form-inline">
	<table border="0">
		<tr>
			<td class="td-label">状态：</td>
			<td class="td-input">
				<input type="hidden" id="status-hid" value="${status }" />
				<select id="status-sel" name="status">
				   <option value="">-- 请选择 --</option>
				   <option value="WAIT_COMMIT">待提交</option>
				   <option value="WAIT_CORRECT">待评分</option>
				   <option value="CORRECTED">已评分</option>
				 </select>
				
				</td>
			<td class="td-label"><button type="button" class="btn btn-info btn-sm" id="query-btn" >
				<i class="ace-icon fa fa-key bigger-110"></i>查询
			</button></td>
		</tr>
	</table>
</form>
</div><br />

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<table id="sample-table-1" class="table table-striped table-bordered table-hover" style="margin-bottom: 10px;">
					<thead>
						<tr>
							<th>作业标题</th>
							<th>课程名称</th>
							<th>教师</th>
							<th>学生</th>
							<th>上课日期</th>
							<th>上课时间</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="homework" items="${homeworks.list}">
						<tr>
							<td>
								<a href="#">${homework.title}</a>
							</td>
							<td>${homework.course }</td>
							<td>${homework.teacher }</td>
							<td>${homework.student }</td>
							<td>${homework.date}</td>
							<td>${homework.time}</td>
							<td>
								${zweb:datetime(homework.createTime) }
							</td>

							<td>
								
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div><!-- /.span -->
		</div><!-- /.row --><!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->

<!-- 翻页 -->
<div>
	<ul class="pagination" style="margin-top: 0px;">
		<c:choose>
			<c:when test="${empty param.cursor }">
				<li class="disabled">
					<a href="javascript:void(0);">
						<i class="ace-icon fa fa-angle-double-left">首页</i>
					</a>
				</li>
				<li class="disabled">
					<a href="javascript:void(0)">上一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:firstPage() }" data-url="${zweb:firstPage() }">
						<i class="ace-icon fa fa-angle-double-left">首页</i>
					</a>
				</li>
				<li class="">
					<a href="javascript:history.go(-1)">上一页</a>
				</li>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${empty homeworks.nextCursor }">
				<li class="disabled">
					<a href="javascript:void(0);">下一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:nextPage(homeworks) }" data-url="${zweb:nextPage(homeworks) }">
					下一页
					</a>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>

<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

$('#status-sel').val($('#status-hid').val());

//点击查询按钮
$('#query-btn').click(function() {
	var params = $('#query-form').serialize();
	location.href="#/homework/admin/list?" + params;
});

</script>
