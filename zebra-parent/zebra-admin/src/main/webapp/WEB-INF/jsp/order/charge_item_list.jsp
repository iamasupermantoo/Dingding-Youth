<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>订单列表</title>
<!-- <script type="text/javascript" src="/static/item/item.js"></script> -->

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
			充值项列表
		</small>
	</h1>
</div><!-- /.page-header -->


<p>
	<button class="btn btn-white btn-default btn-round">
		<a href="#modal-form" role="button" class="blue" data-toggle="modal">添加充值项</a>
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
							<th>充值额</th>
							<th>价格（元）</th>
							<th>IAP商品id</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="item" items="${items}">
						<tr>
							<td>${item.plusAmount }</td>
							<td>${item.price }</td>
							<td>${item.appleProductId }</td>
							<td>${zweb:datetime(item.createtime) }</td>
							<td>
								<a href="javascript:void(0);" class="remove-a" item-id="${item.id }">删除</a>
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div><!-- /.span -->
		</div><!-- /.row --><!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->


<div id="modal-form" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">添加充值项</h4>
			</div>

			<div class="modal-body">
				<div class="row">
				<form id="form-1">
					<div class="col-xs-12 col-sm-7">
						<div class="form-group">
							<label for="plus-amount-txt">充值额</label>
							<div>
								<input class="input-large" name="plusAmount" type="text" id="plus-amount-txt" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="price-txt">价格（元）</label>
							<div>
								<input class="input-large" name="price" type="text" id="price-txt" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="appleProductId-txt">苹果IAP商品id</label>
							<div>
								<input class="input-large" name="appleProductId" type="text" id="appleProductId-txt" />
							</div>
						</div>
						
						<input type="hidden" id="item-hid" />
					</div>
				</form>
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm" data-dismiss="modal">
					<i class="ace-icon fa fa-times"></i>
					取消
				</button>

				<button class="btn btn-sm btn-primary" id="confirm-btn">
					<i class="ace-icon fa fa-check"></i>
					确认
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
var scripts = ["/assets/js/fuelux/fuelux.spinner.min.js"]
ace.load_ajax_scripts(scripts, function() {
$('#price-txt,#plus-amount-txt').ace_spinner({
		value : 10,
		min : 0,
		max : 20000,
		step : 1,
		touch_spinner : true,
		icon_up : 'ace-icon fa fa-caret-up',
		icon_down : 'ace-icon fa fa-caret-down'
	});
});

$('#confirm-btn').click(function() {
	var result = confirm("确认添加吗？");
	if(!result) {
		return;
	}
	$.ajax({
		url: '/product/admin/charge/item/add',
		type: 'post',
		data: $('#form-1').serialize(),
		success: function(result, status, xhr) {
			if(result.meta.code == 1) {
				location.reload();
			} else {
				alert(result.meta.errorInfo || result.meta.desc);
			}
		}
	});
});

$('.remove-a').click(function() {
	var result = confirm("确认删除吗？");
	if(!result) {
		return;
	}
	
	var itemId = $(this).attr('item-id');
	
	$.ajax({
		url: '/product/admin/charge/item/remove',
		type: 'post',
		data: {id: itemId},
		success: function(result, status, xhr) {
			if(result.meta.code == 1) {
				location.reload();
			} else {
				alert(result.meta.errorInfo || result.meta.desc);
			}
		}
	});
});

</script>
