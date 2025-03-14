<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>付费列表</title>
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
</style>


<div class="page-header">
	<h1>
		订单管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			财务管理
		</small>
	</h1>
</div><!-- /.page-header -->

<%-- <div>
<form id="query-form" class="form-inline">
	
	<table border="0">
		<tr>
			<td class="td-label">订单状态：</td>
			<td class="td-input">
				<input type="hidden" id="status-hid" value="${status }" />
				<select id="status-sel" name="status">
					<option value="">--请选择--</option>
					<option value="FINISHED">已完成</option>
					<option value="ADMIN_CONFIRMED">待支付</option>
					<option value="CLOSED">已关闭</option>
				</select>
				</td>
			<td class="td-label"><button type="button" class="btn btn-info btn-sm" id="query-btn" >
				<i class="ace-icon fa fa-key bigger-110"></i>查询
			</button></td>
		</tr>
	</table>
</form>
</div><br /> --%>

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
							<th>订单编号</th>
							<th>用户学号</th>
							<th>用户名</th>
							<th>课程</th>
							<th>金额</th>
							<th>支付渠道</th>
							<th>支付时间</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="order" items="${orders.list}">
						<tr id="tr-${order.oid}">
							<td class="center">
								<label class="position-relative">
									<input type="checkbox" class="ace" />
									<span class="lbl"></span>
								</label>
							</td>
							<td>${order.orderSn }</td>
							<td>dd${order.student.sid }</td>
							<td>${order.student.name }</td>
							<td>${order.courseMeta.name }</td>
							<td>${order.totalPrice }</td>
							<td>${zenum:payChannel(order.payChannel) }</td>
							<td>${zweb:datetime(order.createTime) }</td>
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
			<c:when test="${empty orders.nextCursor }">
				<li class="disabled">
					<a href="javascript:void(0);">下一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:nextPage(orders) }" data-url="${zweb:nextPage(orders) }">
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
				<h4 class="blue bigger">确认订单</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12 col-sm-7">
						<div class="form-group">
							<label for="price-txt">价格（元）</label>
							<div>
								<input class="input-large" type="text" id="price-txt" />
							</div>
						</div>
						<input type="hidden" id="order-hid" />
					</div>
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm" data-dismiss="modal">
					<i class="ace-icon fa fa-times"></i>
					取消
				</button>

				<button class="btn btn-sm btn-primary" id="confirm-btn">
					<i class="ace-icon fa fa-check"></i>
					确认订单
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
var scripts = ["/assets/js/fuelux/fuelux.spinner.min.js"]
ace.load_ajax_scripts(scripts, function() {
$('#price-txt').ace_spinner({
		value : 0,
		min : 0,
		max : 20000,
		step : 100,
		touch_spinner : true,
		icon_up : 'ace-icon fa fa-caret-up',
		icon_down : 'ace-icon fa fa-caret-down'
	});
});

var status = $('#status-hid').val();
if(status){
	$('#status-hid').val(status);
}

$('.confirm-a').click(function() {
	var orderId = $(this).attr('oid');
	$('#order-hid').val(orderId);
});

//点击查询按钮
$('#query-btn').click(function() {
	var params = $('#query-form').serialize();
	location.href="#/order/admin/query?" + params;
});

$('.paid-a').click(function() {
	var result = confirm("确认吗？");
	if(!result) {
		return;
	}
	var orderSn = $(this).attr('order-sn');
	
	$.ajax({
		url: '/order/admin/paid',
		type: 'post',
		data: {'orderSn': orderSn},
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

$('#confirm-btn').click(function() {
	var result = confirm("确认订单吗？");
	if(!result) {
		return;
	}
	
	var price = $('#price-txt').val();
	if(!price) {
		alert("请输入订单价格（元）");
		return false;
	}
	var orderId = $('#order-hid').val();
	
	$.ajax({
		url: '/order/admin/confirm',
		type: 'post',
		data: {'oid': orderId, 'price': price},
		success: function(result, status, xhr) {
			meta = result.meta;
			if(meta.code == 1) {
				alert('确认订单成功');
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
