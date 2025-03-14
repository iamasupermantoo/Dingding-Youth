<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>课程管理</title>
<link rel="stylesheet" href="/assets/css/chosen.css " />
<script type="text/javascript" src="/assets/js/chosen.jquery.min.js"></script>
<script type="text/javascript" src="/static/course/course.js"></script>

<div class="page-header">
	<h1>
		上课管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			课程列表
		</small>
	</h1>
</div><!-- /.page-header -->

<%-- <div>
<form id="query-form" class="form-inline">
	学生编号：<input type="text" class="input-large" value="${sid }" name="sid" />&nbsp;&nbsp;
	状态：<select name="status">
	   <option value="WaitStart">等待开课</option>
	   <option value="OnProgress">课程正在进行</option>
	   <option value="Finished">课程已经完成</option>
	 </select>&nbsp;&nbsp;
	
	<button type="button" id="query-btn" class="btn btn-info btn-sm" id="query-btn">
			<i class="ace-icon fa fa-key bigger-110"></i>查询
	</button>
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
							<th>学生</th>
							<th>名称</th>
							<th>课程logo</th>
							<th>已排课时</th>
							<th>已上课时</th>
							<th>总课时</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="course" items="${courses.list}">
						<tr>
							<td>${course.student.name }</td>
							<td>
								<a href="#">${course.name}</a>
							</td>
							<td><img src="${zweb:imageUrl(course.image, 50, 50) }" /></td>
							<td>${course.planedMaxCnt }</td>
							<td>${course.finishedMaxCnt }</td>
							<td>${course.totalCnt }</td>
							<td>
								${zweb:datetime(course.createTime) }
							</td>

							<td>
								<%-- <a href="#/lesson/admin/list?cid=${course.cid }">历史课次</a> --%>
								<a href="#/lesson/region/plan/list?cid=${course.cid }" data-url="/lesson/region/plan/list?cid=${course.cid }">
									排课
								</a>
								&nbsp;|&nbsp;
								
								<a href="#/lesson/admin/list?cid=${course.cid }" data-url="/lesson/admin/list?cid=${course.cid }">
									查看课次
								</a>
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
			<c:when test="${empty courses.nextCursor }">
				<li class="disabled">
					<a href="javascript:void(0);">下一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:nextPage(courses) }" data-url="${zweb:nextPage(courses) }">
					下一页
					</a>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>
<!-- 添加，弹出form -->
<div id="modal-form" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">添加教材</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12 col-sm-7">
						<div class="space-4"></div>

						<div class="form-group">
							<label for="name-txt">教材名称</label>
							<div>
								<input class="input-large" type="text" id="name-txt" value="" />
							</div>
						</div>
						<div class="space-4"></div>

						<div class="form-group">
							<label for="total-cnt-txt">课时数</label>
							<div>
								<input class="input-large" type="text" id="total-cnt-txt" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="name-txtarea">描述</label>
							<div>
								<input class="input-large" type="text" id="name-txtarea" value="" />
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

				<button class="btn btn-sm btn-primary" id="add-btn">
					<i class="ace-icon fa fa-check"></i>
					添加
				</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

</script>
