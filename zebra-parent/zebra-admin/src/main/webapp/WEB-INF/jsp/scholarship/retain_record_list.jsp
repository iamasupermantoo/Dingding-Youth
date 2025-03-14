<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>提现申请列表</title>

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


<%-- <div>
<form id="s-query-form" class="form-inline">
	<table border="0">
		<tr>
			<td class="td-label">提现手机号：</td>
			<td class="td-input">
				<input type="text" class="input-medium q-input" style="padding: 3px;" id="bankUserMobile" name="bankUserMobile" value="${bankUserMobile }"/>
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
		<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
				<li class="${empty param.status ? 'active' : ''}">
					<a data-toggle="tab" href="#/scholarship/admin/retain/record/list" class="q-a" id="hot-a">
						全部
					</a>
				</li>
				<li class="${param.status == 'WAIT' ? 'active' : '' }">
					<a data-toggle="tab" href="#/scholarship/admin/retain/record/list?status=WAIT" class="q-a" id="rank-a">
						待审核
					</a>
				</li>
				<li class="${param.status == 'SUCC' ? 'active' : '' }">
					<a data-toggle="tab" href="#/scholarship/admin/retain/record/list?status=SUCC" class="q-a" id="latest-a">
						提现成功
					</a>
				</li>
				<li class="${param.status == 'FAIL' ? 'active' : '' }">
					<a data-toggle="tab" href="#/scholarship/admin/retain/record/list?status=FAIL" class="q-a" id="latest-a">
						提现失败
					</a>
				</li>
			</ul>
		
			<div class="tab-content">
				<table id="sample-table-1" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>用户名</th>
							<th>真实姓名</th>
							<th>手机号</th>
							<th>开户行</th>
							<th>银行卡号</th>
							<th>金额（元）</th>
							<th>状态</th>
							<th>申请时间</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="record" items="${records.list}">
						<tr>
							<td>
								${record.username }
							</td>
							<td>${record.bankUser }</td>
							<td>${record.bankUserMobile }</td>
							<td>${record.bankName }</td>
							<td>${record.bankCardNum }</td>
							<td>${record.applyAmount }</td>
							<td>
								<c:choose>
									<c:when test="${record.status == 0 }">
										<span class="label label-sm label-warning">待审核</span>
									</c:when>
									<c:when test="${record.status == 1 }">
										<span class="label label-sm label-success">提现成功</span>
									</c:when>
									<c:when test="${record.status == 2 }">
										<span class="label label-sm label-inverse arrowed-in">提现失败</span>
									</c:when>
								</c:choose>
							</td>
							<td >${record.datetime } </td>
							<td>
								<c:if test="${record.status == 0 }">
									<a href="javascript:void(0);" class="retain-succ" record-id="${record.id }">提现成功</a> &nbsp;|&nbsp;
									<a href="javascript:void(0);" class="retain-fail" record-id="${record.id }">提现失败</a>
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
			<c:when test="${empty records.nextCursor }">
				<li class="disabled">
					<a href="javascript:void(0);">下一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:nextPage(records) }" data-url="${zweb:nextPage(records) }">
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
	location.href="#/record/admin/list?" + params;
});

//tab切换
$('.q-a').click(function() {
	var href = $(this).attr('href');
	location.href = href;
	return false;
});

$('.retain-succ').click(function() {
	var result = confirm("确认提现成功吗？此操作无法撤销");
	if(!result) {
		return;
	}
	var recordId = $(this).attr('record-id');
	
	$.ajax({
		url: '/scholarship/admin/retain/pass',
		type: 'post',
		data: {'record': recordId},
		success: function(result, status, xhr) {
			if(result.meta.code == 1) {
				alert('操作成功');
				location.reload();
			} else {
				alert(result.meta.errorInfo || result.meta.desc);
			}
		}
	});
});

$('.retain-fail').click(function() {
	var result = confirm("确认提现失败吗？此操作无法撤销");
	if(!result) {
		return;
	}
	var recordId = $(this).attr('record-id');
	
	$.ajax({
		url: '/scholarship/admin/retain/unpass',
		type: 'post',
		data: {'record': recordId},
		success: function(result, status, xhr) {
			if(result.meta.code == 1) {
				alert('操作成功');
				location.reload();
			} else {
				alert(result.meta.errorInfo || result.meta.desc);
			}
		}
	});
});

</script>
