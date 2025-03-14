<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>注册用户列表</title>

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
		注册用户管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			注册用户列表
		</small>
	</h1>
</div><!-- /.page-header -->


<div>
<form id="s-query-form" class="form-inline">
	<table border="0">
		<tr>
			<td class="td-label">用户手机号：</td>
			<td class="td-input">
				<input type="text" class="input-medium q-input" style="padding: 3px;" id="mobile" name="mobile" value="${mobile }"/>
				</td>
			<td class="td-label"><button type="button" class="btn btn-info btn-sm" id="s-query-btn" >
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
				<table id="sample-table-1" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th class="center">
								<label class="position-relative">
									<input type="checkbox" class="ace" />
									<span class="lbl"></span>
								</label>
							</th>
							<th>昵称</th>
							<th>登录账号</th>
							<th>头像</th>
							<th>性别</th>
							<th>个性签名</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="student" items="${users.list}">
						<tr id="tr-${student.sid}">
							<td class="center">
								<label class="position-relative">
									<input type="checkbox" class="ace" />
									<span class="lbl"></span>
								</label>
							</td>

							<td>
								<a href="#"><span id='span-name-${student.sid }'>${student.name}</span></a>
							</td>
							<td>${student.mobile }</td>
							<td><img src="${zweb:imageUrl(student.headImage, 50, 50) }" /></td>
							<td>${zenum:gender(student.gender) }</td>
							<td>${student.signature }</td>
							<td >
								${zweb:datetime(student.createTime) }
							</td>

							<td>
							<a href="javascript:void(0);">封禁</a>
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
			<c:when test="${empty users.nextCursor }">
				<li class="disabled">
					<a href="javascript:void(0);">下一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:nextPage(users) }" data-url="${zweb:nextPage(users) }">
					下一页
					</a>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>
<script type="text/javascript">
var scripts = ["/assets/js/fuelux/fuelux.spinner.min.js"]
ace.load_ajax_scripts(scripts, function() {
	$('#total-cnt-txt').ace_spinner({value:0,min:0,max:300,step:1, touch_spinner: true, icon_up:'ace-icon fa fa-caret-up', icon_down:'ace-icon fa fa-caret-down'});
});

//点击查询按钮
$('#s-query-btn').click(function() {
	var params = $('#s-query-form').serialize();
	location.href="#/student/admin/list?" + params;
});

</script>
