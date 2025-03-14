<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>操作日志列表</title>
<div class="page-header">
	<h1>
		系统管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			操作日志
		</small>
	</h1>
</div>

<div>
<form id="query-form" class="form-inline" action="#/adminlog/list">
	用户名：<input type="text" class="input-large" placeholder="" />&nbsp;&nbsp;
	日期：<div class="input-daterange input-group">
				<input type="text" class="input-sm form-control" name="start">
				<span class="input-group-addon">
					<i class="fa fa-exchange"></i>
				</span>
				<input type="text" class="input-sm form-control" name="end">
			</div>
	<button type="button" class="btn btn-info btn-sm" id="query-btn">
		<i class="ace-icon fa fa-key bigger-110"></i>查询
	</button>
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
							<th>操作者姓名</th>
							<th>操作数据</th>
							<th>事件类型</th>
							<th>创建时间</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="log" items="${logs.list}">
						<tr>
							<td class="center">
								<label class="position-relative">
									<input type="checkbox" class="ace" />
									<span class="lbl"></span>
								</label>
							</td>

							<td>
								<a href="#">${log.adminUser.username}</a>
							</td>
							<td>${log.data}</td>
							<td>${log.type }</td>

							<td>
								${zweb:datetime(log.createTime) }
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