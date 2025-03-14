<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>管理员列表</title>
<div class="page-header">
	<h1>
		系统管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			管理员列表
		</small>
	</h1>
</div><!-- /.page-header -->

<p>
	<button class="btn btn-white btn-default btn-round">
		<a href="#/adminuser/addUI" role="button" class="blue">添加账号</a>
	</button>
</p>

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<table id="sample-table-1" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>登录账号</th>
							<th>姓名</th>
							<th class="hidden-480">状态</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="user" items="${users.list}">
						<tr>
							<td>
								<a href="#">${user.username}</a>
							</td>
							<td>${user.name}</td>
							<td class="hidden-480">${zenum:userStatus(user.status) }</td>

							<td>
								${zweb:datetime(user.createTime) }
							</td>

							<td>
								<a href="#/adminuser/details?aid=${user.aid }">编辑</a>
								<%-- <a href="#/adminuser/privilege/list?aid=${user.aid }">权限</a> &nbsp; | &nbsp;
								<a href="javascript:void(0);" class="aid" aid="${user.aid }" status=${user.status }>
									${user.status == 1 ? '解除封禁':'封禁'}
								</a> --%>
								
								
								<c:if test="${user.username != 'root' }">
									&nbsp; | &nbsp;
									<a href="#/adminuser/privilege/list?aid=${user.aid }">权限</a> &nbsp; | &nbsp;
									<a href="javascript:void(0);" class="aid" aid="${user.aid }" status=${user.status }>
										${user.status == 1 ? '解除封禁':'封禁'}
									</a>
								</c:if>
								
								
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div><!-- /.span -->
		</div><!-- /.row --><!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->
<script type="text/javascript">
	var scripts = []
	ace.load_ajax_scripts(scripts);
</script>
<script type="text/javascript">
$(document).ready(function() {
	// 删除（封禁）
	$('.aid').click(function() {
		var aid = $(this).attr("aid");
		var status = $(this).attr('status');
		if(status == 0) {
			block(aid);
			
		} else {
			unblock(aid);
		}
	});
});

function block(aid) {
	var result = confirm("确认禁用用户吗？");
	if(!result) {
		return;
	}
	$.ajax({
		url: '/adminuser/block',
		type: 'post',
		data: {'aid': aid},
		success: function(result, status, xhr) {
			if(result.meta.code == 1) {
				alert('禁用成功');
				location.reload();
			} else {
				alert(result.desc||result.errorInfo);
			}
		}
	});
}

function unblock(aid) {
	var result = confirm("确认解除封禁吗？");
	if(!result) {
		return;
	}
	$.ajax({
		url: '/adminuser/unblock',
		type: 'post',
		data: {'aid': aid},
		success: function(result, status, xhr) {
			if(result.meta.code == 1) {
				alert('解除封禁成功');
				location.reload();
			} else {
				alert(result.desc||result.errorInfo);
			}
		}
	});
}
</script>
