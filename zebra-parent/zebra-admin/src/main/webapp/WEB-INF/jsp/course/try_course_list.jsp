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
		课程管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			免费试听课
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

<!-- <p>
	<button class="btn btn-white btn-default btn-round">
		<a href="#modal-form" role="button" class="blue" data-toggle="modal">添加试听课</a>
	</button>
</p> -->

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<table id="sample-table-1" class="table table-striped table-bordered table-hover" style="margin-bottom: 10px;">
					<thead>
						<tr>
							<th>用户名</th>
							<!-- <th>头像</th> -->
							<th>手机号</th>
							<th>注册时间</th>
							<th>点聊结果</th>
							<th>是否付费</th>
							<th>是否已排课</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="course" items="${courses.list}">
						<tr>
							<td>${course.student.name }</td>
							<%-- <td>
								<img src="${zweb:imageUrl(course.student.headImage, 50, 50) }" />
							</td> --%>
							<td>${course.student.mobile }</td>
							<td>
								${zweb:datetime(course.student.createTime) }
							</td>
							<td>${zenum:chatResult(course.chatResult) }</td>
							<td>${course.paid }</td>
							<td>${course.planned }</td>
							<td>
								<a href="#modal-form2" role="button" class="blue confirm-a chat-res-a" chat-result="${course.chatResult }" 
									sid="${course.student.sid }" data-toggle="modal">
								电聊结果
								</a>
								<c:if test="${not course.paid }">
									&nbsp;|&nbsp;
									<a href="#/lesson/region/plan/list" 
										data-url="/lesson/region/plan/list" 
										cmId="${course.cmId }" sid="${course.student.sid }" cid="${course.cid }" class="confirm-aa">
										排课
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

<%-- <div id="modal-form" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">添加试听课</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<form id="form-1">
						<div class="col-xs-12 col-sm-7">
							<div class="form-group">
								<label for="mobile-txt"><font color="red">*</font> 用户账号（手机号）</label>
								<div>
									<input class="input-large" name="mobile" type="text" id="mobile-txt" value="" />
								</div>
							</div>
							<div class="form-group">
								<label for="cmid-sel"><font color="red">*</font> 课程</label>
								<div>
									<select id="cmid-sel" name="cmid">
									   <option value="">-- 请选择 --</option>
									   <c:forEach items="${courseMetas }" var="cm">
									   		<option value="${cm.id }">${cm.name }</option>
									   </c:forEach>
									 </select>
								 </div>
							</div>
						</div>
					</form>
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
</div> --%>

<div id="modal-form2" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">填写电聊结果</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12 col-sm-7">
						<div class="form-group">
							<label for="chatResult-sel"><font color="red">*</font> 电聊结果</label>
							<div>
								<select id="chatResult-sel" name="chatResult">
								   <option value="0">未电聊</option>
								   <option value="1">没兴趣</option>
								   <!-- <option value="2">可下单</option> -->
								   <option value="3">其他</option>
								 </select>
							 </div>
						</div>
						<input type="hidden" id="sid-hid" />
						<div class="form-group">
							<label for="remark-txt">备注</label>
							<div>
								<!-- <input class="input-large" type="text" id="remark-txt" /> -->
								
								<textarea rows="3" cols="30" id="remark"></textarea>
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

				<button class="btn btn-sm btn-primary" id="confirm-res-btn">
					<i class="ace-icon fa fa-check"></i>
					修改
				</button>
			</div>
		</div>
	</div>
</div>





<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

$('.chat-res-a').click(function() {
	var studentId = $(this).attr('sid');
	$('#sid-hid').val(studentId);
});

$('.confirm-aa').click(function() {
	var confirmA = $(this);
	var sid = confirmA.attr('sid');
	var cid = confirmA.attr('cid');
	var cmId = confirmA.attr('cmId');
	
	
	// cid是空的，则先创建试听课
	if(!cid) {
		var result = confirm("将对试听课进行排课，是否确认？");
		if(!result) {
			return false;
		}
		$.ajax({
			url: '/try/course/admin/confirm',
			type: 'post',
			data: {'sid': sid,'cmId':cmId},
			async: false,
			success: function(result, status, xhr) {
				meta = result.meta;
				if(meta.code == 1) {
					cid = result.data.cid;
				} else {
					alert(meta.desc + 
							(meta.errInfo ? '\n\n信息:\n'+ meta.errInfo : ''));
				}
			}
		});
	}
	var href = confirmA.attr('href') + '?cid=' + cid;
	confirmA.attr('href', href);
});

// 电聊结果
$('#confirm-res-btn').click(function() {
	var chatResult = $('#chatResult-sel').val();
	var remark = $('#remark').val();
	var studentId = $('#sid-hid').val();
	
	$.ajax({
		url: '/try/course/admin/chat/result',
		type: 'post',
		data: {'sid': studentId, 'result': chatResult, 'remark': remark},
		success: function(result, status, xhr) {
			meta = result.meta;
			if(meta.code == 1) {
				alert('操作成功');
				location.reload();
			} else {
				alert(meta.desc + 
						(meta.errInfo ? '\n\n信息:\n'+ meta.errInfo : ''));
			}
		}
	});
	
	return false;
});



</script>
