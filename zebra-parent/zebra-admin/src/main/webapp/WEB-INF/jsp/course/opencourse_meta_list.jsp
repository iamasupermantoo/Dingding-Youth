<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>课程列表</title>

<link rel="stylesheet" href="/assets/css/datepicker.css" />
<!-- <link rel="stylesheet" href="/assets/css/bootstrap-timepicker.css " /> -->
<link rel="stylesheet" href="/assets/css/bootstrap-datetimepicker.css" />

<script type="text/javascript" src="/assets/js/date-time/bootstrap-datepicker.min.js"></script>
<!-- <script type="text/javascript" src="/assets/js/date-time/bootstrap-timepicker.min.js"></script> -->
<script type="text/javascript" src="/assets/js/date-time/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="/assets/js/date-time/locales/bootstrap-datepicker.zh-CN.js"></script>


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
			公开课管理
		</small>
	</h1>
</div><!-- /.page-header -->

<!-- TODO 先隐藏掉 -->
<!-- <div>
<form id="s-query-form" class="form-inline">
	<table border="0">
		<tr>
			<td class="td-label">状态：</td>
			<td class="td-input">
				<select name="status" id="status">
				   <option value="">--- 请选择 ---</option>
				   <option value="Normal">正常，可以选课</option>
				   <option value="Unshelved">已下架，不可选课</option>
				 </select>
			</td>
			<td class="td-label"><button type="button" class="btn btn-info btn-sm" id="s-query-btn" >
				<i class="ace-icon fa fa-key bigger-110"></i>查询
			</button></td>
		</tr>
	</table>
</form>
</div><br /> -->


<p>
	<!-- <button class=""> </button> -->
	<a href="#/opencourse/meta/admin/add" role="button" class="blue btn btn-white btn-default btn-round " >添加课程</a>
	
</p>
<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<table id="sample-table-1" class="table table-striped table-bordered table-hover" style="margin-bottom: 10px;">
					<thead>
						<tr>
							<th>id</th>
							<th>名称</th>
							<th>课程logo</th>
							
							<th>价格</th>
							<th>级别</th>
							<th style="display: none;">类型</th>
							<th>开课时间</th>
							<th>创建时间</th>
							<th>任课教师</th>
							<th>教师手机</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="x" items="${pages.list}">
						<tr id="tr-${x.cmId }">
							<td>${x.cmId }</td>
							<td>
								<span id="name-span-${x.cmId }">${x.name}</span>
							</td>
							<td><img src="${x.image.getURL(50, 50) }" /></td>

							
							<td><span id="price-span-${x.cmId }">${x.price }</span></td>
							
							<td><span id="level-span-${x.cmId }">${x.level }</span></td>
							<td style="display: none;"><span id="type-span-${x.cmId }">${x.type }</span>
								<input type="hidden" id="desc-hid-${x.cmId }" value="${x.desc }" />
								<input type="hidden" id="subNotes-hid-${x.cmId }" value="${x.subNotes }" />
								<input type="hidden" id="suitableCrowds-hid-${x.cmId }" value="${x.suitableCrowds }" />
								<input type="hidden" id="openTime-hid-${x.cmId }" value="${x.openTime }" />
								<input type="hidden" id="joinCnt-hid-${x.cmId }" value="${x.joinCnt }" />
								<input type="hidden" id="shareDesc-hid-${x.cmId }" value="${x.shareBrief }" />
								<input type="hidden" id="shareUrl-hid-${x.cmId }" value="${x.shareJumpUrl }" />
								<input type="hidden" id="teacher-hid-${x.cmId }" value="${x.userName }" />
							</td>
							
							<td>
								${x.openTime }
							</td>
							<td>
								${zweb:datetime(x.createTime) }
							</td>
							
							<td>
								${x.userName}
							</td>
							<td>
								${x.mobile}
							</td>
							
							<td>
								<a href="#/opencourse/meta/admin/edit?cmId=${x.cmId }" role="button" class="blue modify-a" cmid="${x.cmId }" 
									>修改</a>
								
								<c:if test="${x.status == 0 }">
								&nbsp;|&nbsp;
									<a href="javascript:void(0);" class="Unshelved-a" cmid="${x.cmId }">下架</a>
								</c:if>
								<c:if test="${x.status == 4 }">
								&nbsp;|&nbsp;
									<a href="javascript:void(0);" class="Unshelved-b" cmid="${x.cmId }">上架</a>
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
			<c:when test="${empty pages.nextCursor }">
				<li class="disabled">
					<a href="javascript:void(0);">下一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:nextPage(pages) }" data-url="${zweb:nextPage(pages) }">
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



$('.Unshelved-a').click(function() {
	var result = confirm("确认下架吗？");
	if(!result) {
		return;
	}
	var cmId = $(this).attr('cmid');
	//alert(cmId)
	$.ajax({
		url: '/opencourse/meta/admin/unshelve',
		type: 'post',
		data: {'cmId': cmId},
		success: function(result, status, xhr) {
			meta = result.meta;
			if(meta.code == 1) {
				alert('下架成功');
				location.reload();
			} else {
				alert(meta.desc + 
						(meta.errInfo ? '\n\n信息:\n'+ meta.errInfo : ''));
			}
		}
	});
	
	return false;
});

$('.Unshelved-b').click(function() {
	var result = confirm("确认上架吗？");
	if(!result) {
		return;
	}
	var cmId = $(this).attr('cmid');
	//alert(cmId)
	$.ajax({
		url: '/opencourse/meta/admin/shelve',
		type: 'post',
		data: {'cmId': cmId},
		success: function(result, status, xhr) {
			meta = result.meta;
			if(meta.code == 1) {
				alert('上架成功');
				location.reload();
			} else {
				alert(meta.desc + 
						(meta.errInfo ? '\n\n信息:\n'+ meta.errInfo : ''));
			}
		}
	});
	
	return false;
});

$('#s-query-btn').click(function() {
	var params = $('#s-query-form').serialize();
	location.href="#/course/meta/admin/list?" + params;
});



</script>
<script type="text/javascript">

</script>