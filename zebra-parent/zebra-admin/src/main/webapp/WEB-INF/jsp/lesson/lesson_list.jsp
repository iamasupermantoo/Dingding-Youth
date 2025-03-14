<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>课次安排</title>
<link rel="stylesheet" href="/assets/css/chosen.css " />
<script type="text/javascript" src="/assets/js/chosen.jquery.min.js"></script>
<script type="text/javascript" src="/static/course/course.js"></script>

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
</style>

<div class="page-header">
	<h1>
		课程管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			课次安排
		</small>
	</h1>
</div><!-- /.page-header -->

<%-- <div>
<form id="s-query-form" class="form-inline">
	<table border="0">
		<tr>
			<td class="td-label">学生编号: </td>
			<td class="td-input">
				<input type="text" class="input-medium q-input" 
					style="padding: 3px;" value="${num }" id="num" name="num"/>
			</td>
			
			<td class="td-label">老师编号: </td>
			<td class="td-input">
				<input type="text" class="input-medium q-input" 
					style="padding: 3px;" value="${num }" id="num" name="num"/>
			</td>
			
			
			<td class="td-label">状态：</td>
			<td class="td-input">
				<select name="status">
				   <option value="WaitStart">等待开课</option>
				   <option value="OnProgress">课程正在进行</option>
				   <option value="Finished">课程已经完成</option>
				 </select>
			</td>
			<td class="td-label"><button type="button" class="btn btn-info btn-sm" id="s-query-btn" >
				<i class="ace-icon fa fa-key bigger-110"></i>查询
			</button></td>
		</tr>
	</table>
</form>
</div><br /> --%>

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<table id="sample-table-1" class="table table-striped table-bordered table-hover" style="margin-bottom: 10px;">
					<thead>
						<tr>
							<th>课程名</th>
							<th>教师</th>
							<th>学生</th>
							<th>课次</th>
							<th>上课日期</th>
							<th>上课时间</th>
							<th>状态</th>
							<th>课程回放</th>
						</tr>
					</thead>
					
					<tbody>
						<c:forEach var="lesson" items="${lessons.list}">
						<tr>
							<td>
								${lesson.course}
							</td>
							<td>${lesson.teacher.name }</td>
							<td>${lesson.student.name }</td>
							<td>${lesson.cnt }</td>
							<td>${lesson.date }</td>
							<td>${lesson.time }</td>
							<td>${zenum:lessonStatus(lesson.status) }</td>
							<td>
								<c:choose>
									<c:when test="${lesson.videoUploaded }">
										<a href="${lesson.videoUrl }" target="view_window">观看回放</a>
									</c:when>
									<c:otherwise>
										<font color="red">未上传</font>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div><!-- /.span -->
		</div><!-- /.row --><!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->

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
			<c:when test="${empty lessons.nextCursor }">
				<li class="disabled">
					<a href="javascript:void(0);">下一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:nextPage(lessons) }" data-url="${zweb:nextPage(lessons) }">
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

</script>
