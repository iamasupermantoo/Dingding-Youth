<%@page import="com.youshi.zebra.reaction.constants.StudentReactionStatus"%>
<%@page import="com.youshi.zebra.reaction.constants.TeacherReactionStatus"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>订单列表</title>
<!-- <script type="text/javascript" src="/static/order/order.js"></script> -->

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
			学生评价
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
					<option value="">--请选择--</option>
					<option value="WaitCommit">待评价</option>
					<option value="Committed">已评价</option>
					<option value="AdminDel">已删除</option>
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
				<table id="sample-table-1" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>课程</th>
							<th>课程图片</th>
							<th>课次</th>
							<th>时间</th>
							<th>学生</th>
							<th>老师</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="reaction" items="${reactions.list}">
						<tr id="tr-${reaction.lid}">
							<td>${reaction.course }</td>
							<td><img src="${reaction.image.getURL(50, 50) }" /></td>
							<td>${reaction.cnt }</td>
							<td>${reaction.date } ${reaction.time }</td>
							<td>${reaction.student }</td>
							<td>${reaction.teacher }</td>
							<td><c:set value="${reaction.status }" var="status"></c:set>
								<%=StudentReactionStatus
									.fromValue((Integer)pageContext.findAttribute("status")).getName() %>
							</td>
							<td>
								<a href="javascript:void(0);" class="view-a" cid="${reaction.cid }" lid="${reaction.lid }">查看详情</a>
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
			<c:when test="${empty reactions.nextCursor }">
				<li class="disabled">
					<a href="javascript:void(0);">下一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:nextPage(reactions) }" data-url="${zweb:nextPage(reactions) }">
					下一页
					</a>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>

<!-- 详情，弹窗 -->
<div id="modal-form" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content" style="width: 480px;">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">教师反馈详情</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12 col-sm-7" style="left: 40px;">
						
						<h5 class="group">上课信息</h5>
						<div class="form-group">
							<label class="rea-label">上课时间：</label><span id="datetime-span">2016-10-10 12:12:00</span>
						</div>
						
						<div class="form-group">
							<label class="rea-label">课程名称：</label>
							<span id="course-span">乐高学习</span>
						</div>
						
						<div class="form-group">
							<label class="rea-label">老师姓名：</label>
							<span id="teacher-span">张老师</span>
						</div>
						
						<hr />
						
						<h5 class="group">评价</h5>
						<div class="form-group">
							<label class="rea-label">授课质量：</label>
							<span id="quality-span">暂无</span>
						</div>
						<div class="form-group">
							<label class="rea-label">接受程度：</label>
							<span id="acceptance-span">暂无</span>
						</div>

						<div class="form-group">
							<label class="rea-label">本次备注：</label>
							<span id="remark-span">暂无</span>
						</div>
						
						<hr />
						
						<h5 class="group">老师得分</h5>
						<div class="form-group">
							<label class="rea-label">评级：</label><span id="score-span">暂无</span>
						</div>
						<div class="form-group">
							<label class="rea-label">得分：</label><span id="star-span">暂无</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>



<script type="text/javascript">
var scripts = ["/assets/js/fuelux/fuelux.spinner.min.js"]
ace.load_ajax_scripts(scripts, function() {

});

$('#status-sel').val($('#status-hid').val());

//点击查询按钮
$('#query-btn').click(function() {
	var params = $('#query-form').serialize();
	location.href="#/reaction/admin/list/byteacher?" + params;
});

$('.view-a').click(function() {
	var cid = $(this).attr('cid');
	var lid = $(this).attr('lid');
	
	$.ajax({
		url: '/reaction/admin/details/bystudent',
		type: 'get',
		data: {'lid': lid, 'cid': cid},
		success: function(result, status, xhr) {
			meta = result.meta;
			if(meta.code == 1) {
				var reac = result.data.reaction;
				$('#datetime-span').text(reac.date + " " + reac.time);
				$('#course-span').text(reac.course);
				$('#teacher-span').text(reac.teacher);
				
				$('#quality-span').text(reac.quality);
				$('#acceptance-span').text(reac.acceptance);
				$('#remark-span').text(reac.remark);
				
				$('#score-span').text(reac.score ? reac.score + "分" : reac.score);
				$('#star-span').text(reac.star ? reac.star + "星" : reac.star);
				
				$('#modal-form').modal('show');
			} else {
				alert(meta.desc + 
						(meta.errInfo ? '\n\n信息:\n'+ meta.errInfo : ''));
			}
		}
	});
	return false;
});
</script>
