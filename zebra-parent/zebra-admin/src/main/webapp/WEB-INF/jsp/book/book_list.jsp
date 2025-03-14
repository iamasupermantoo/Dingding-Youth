<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>教材管理</title>
<script type="text/javascript" src="/static/book/book.js"></script>
<div class="page-header">
	<h1>
		教材管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			教材列表
		</small>
	</h1>
</div><!-- /.page-header -->

<p>
	<button class="btn btn-white btn-default btn-round">
		<a href="#modal-form" role="button" class="blue" data-toggle="modal">添加教材</a>
	</button>
	<button class="btn btn-white btn-default btn-round">
		<a href="#/book/admin/import" role="button" class="blue">导入教材</a>
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
							<th class="center">
								<label class="position-relative">
									<input type="checkbox" class="ace" />
									<span class="lbl"></span>
								</label>
							</th>
							<th>名称</th>
							<th>总课时</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody id="table-1">
						<c:forEach var="book" items="${books.list}">
						<tr id="tr-${book.bid}">
							<td class="center">
								<label class="position-relative">
									<input type="checkbox" class="ace" />
									<span class="lbl"></span>
								</label>
							</td>

							<td>
								<a href="#"><span id='span-name-${book.bid }'>${book.name}</span></a>
								<span id='span-desc-${book.bid }'><input type="hidden" value="${book.desc }" /></span>
							</td>
							<td><span id='span-totalcnt-${book.bid }'>${book.totalCnt}</span></td>
							<td >
								${zweb:datetime(book.createTime) }
							</td>

							<td>
							<a href="#/chapter/admin/list?bid=${book.bid }">查看章节</a> &nbsp; | &nbsp;
							<a href="#modify-modal" role="button" class="blue modify-a" bid="${book.bid }" 
								data-toggle="modal">修改</a>&nbsp; | &nbsp;
							<a href="javascript:void(0);" class="remove-a" bid="${book.bid }"><font color="red">删除</font></a>
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div><!-- /.span -->
		</div><!-- /.row --><!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->

<div>
	<ul class="pagination" style="margin-top: 0px;">
		<li class="${empty param.cursor ? 'disabled':''}">
			<a href="#/book/admin/list" data-url="/book/admin/list">
				<i class="ace-icon fa fa-angle-double-left">首页</i>
			</a>
		</li>
		<li class="${empty param.cursor ? 'disabled':''}">
			<a href="javascript:history.go(-1)">上一页</a>
		</li>
		
		<li class="${empty books.nextCursor ? 'disabled':'' }">
			<a href="#/book/admin/list?cursor=${books.nextCursor }" data-url="/book/admin/list?cursor=${books.nextCursor }">下一页</a>
		</li>
	</ul>
</div>



<!-- 添加教材，弹出form -->
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
								<input class="input-large" type="text" id="desc-txtarea" value="" />
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


<!-- 修改教材，弹出form -->
<div id="modify-modal" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">修改教材</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12 col-sm-7">
						<div class="space-4"></div>
						<input type="hidden" id="book-hid" />						
						<div class="form-group">
							<label for="name-txt">教材名称</label>
							<div>
								<input class="input-large" type="text" id="name-txt2" value="" />
							</div>
						</div>
						<div class="space-4"></div>

						<div class="form-group">
							<label for="total-cnt-txt">课时数</label>
							<div>
								<input class="input-large" type="text" id="total-cnt-txt2" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="name-txtarea">描述</label>
							<div>
								<input class="input-large" type="text" id="desc-txtarea2" value="" />
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

				<button class="btn btn-sm btn-primary" id="modify-btn">
					<i class="ace-icon fa fa-check"></i>
					确认修改
				</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
var scripts = ["/assets/js/fuelux/fuelux.spinner.min.js"]
ace.load_ajax_scripts(scripts, function() {
	$('#total-cnt-txt,#total-cnt-txt2').ace_spinner({value:0,min:0,max:300,step:1, touch_spinner: true, icon_up:'ace-icon fa fa-caret-up', icon_down:'ace-icon fa fa-caret-down'});
});

</script>
