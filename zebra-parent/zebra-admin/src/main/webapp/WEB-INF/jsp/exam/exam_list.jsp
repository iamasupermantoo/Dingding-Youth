<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>测评管理</title>
<link rel="stylesheet" href="/assets/css/chosen.css " />
<script type="text/javascript" src="/assets/js/chosen.jquery.min.js"></script>

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
		课程管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			测评管理
		</small>
	</h1>
</div><!-- /.page-header -->

<p>
	<button class="btn btn-white btn-default btn-round">
		<a href="#modal-form" role="button" class="blue" data-toggle="modal">添加测评</a>
	</button>
</p>

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<table id="sample-table-1" class="table table-striped table-bordered table-hover" style="margin-bottom: 10px;">
					<thead>
						<tr>
							<th>名称</th>
							<th>级别</th>
							<th>状态</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="exam" items="${exams.list}">
							<tr>
								<td>
									${exam.name}
								</td>
								<td>${exam.level.name }</td>
								<td>${exam.status.name }</td>
								<td>
									${zweb:datetime(exam.createTime) }
								</td>
								<td>
									<a href="#/exam/admin/question/list?exam=${exam.id }">查看问题</a>
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
			<c:when test="${empty exams.nextCursor }">
				<li class="disabled">
					<a href="javascript:void(0);">下一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:nextPage(exams) }" data-url="${zweb:nextPage(exams) }">
					下一页
					</a>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>

<div id="modal-form" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">添加测评</h4>
			</div>

			<form id="add-form" action="/exam/admin/create" method="post">
			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12 col-sm-7">
						<div class="form-group">
							<label for="name"><font color="red">* </font>名称</label>

							<div>
								<input id="name" name="name" class="input-large" type="text" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="level"><font color="red">* </font> 级别</label>
							<div>
								<select name="level" id="level">
								   <option value="level1">Level1</option>
								   <option value="level2">Level2</option>
								   <option value="level3">Level3</option>
								   <option value="level4">Level4</option>
								 </select>
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

				<button type="submit" class="btn btn-sm btn-primary" id="addBtn">
					<i class="ace-icon fa fa-check"></i>
					添加
				</button>
			</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

$('#status-sel').val($('#status-hid').val());

//点击查询按钮
$('#query-btn').click(function() {
	var params = $('#query-form').serialize();
	location.href="#/exam/admin/list?" + params;
});

// 添加
$('#addBtn').click(function() {
	var params = $('#add-form').serialize();
	
	$.ajax({
		url: '/exam/admin/create',
		type: 'post',
		data: params,
		success: function(result, status, xhr) {
			if(result.meta.code == 1) {
				alert('创建成功');
				location.reload();
			} else {
				alert(result.meta.desc 
						|| result.meta.errorInfo);
			}
		}
	});
	
	return false;
});

</script>
