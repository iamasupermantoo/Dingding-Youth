<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>教师列表</title>
<div class="page-header">
	<h1>
		教师管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			教师列表
		</small>
	</h1>
</div><!-- /.page-header -->

<p>
	
		<a href="#/teacher/admin/addUI" data-url="/teacher/admin/addUI" class="btn btn-white btn-default btn-round">注册教师账号</a>
	<!-- <button ></button> -->
</p>

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<table id="sample-table-1" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>真实姓名</th>
							<th>登录账号</th>
							<th>头像</th>
							<th>性别</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="teacher" items="${teachers.list}">
						<tr id="tr-${teacher.tid}">
							<td>
								<a href="#"><span id='span-name-${teacher.tid }'>${teacher.name}</span></a>
							</td>
							<td>${teacher.mobile }</td>
							<td><img src="${zweb:imageUrl(teacher.headImage, 50, 50) }" /></td>
							<td>${zenum:gender(teacher.gender) }</td>
							<td >
								${zweb:datetime(teacher.createTime) }
							</td>
							<td >
								<a href="#/teacher/admin/edit?tid=${teacher.tid }">编辑</a> 
							</td>
							<!-- <td> -->
							<%-- <a href="#/chapter/admin/list?tid=${teacher.tid }">封禁</a> &nbsp; | &nbsp;
							<a href="#" class="remove-a" tid="${teacher.tid }"><font color="red">删除</font></a> --%>
							<!-- </td> -->
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
			<c:when test="${empty teachers.nextCursor }">
				<li class="disabled">
					<a href="javascript:void(0);">下一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:nextPage(teachers) }" data-url="${zweb:nextPage(teachers) }">
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

</script>
