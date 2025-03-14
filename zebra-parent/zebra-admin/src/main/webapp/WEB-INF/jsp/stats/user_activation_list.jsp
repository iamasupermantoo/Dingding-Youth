<%@ page language="java" contentType="text/html; charset=UTF-8" session="false"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/inc/taglib.inc" %>
<title>主题列表</title>
<div class="page-header">
	<h1>
		数据统计
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			用户活跃分析
		</small>
	</h1>
</div><!-- /.page-header -->

<div class="row">
	<div class="col-xs-12">
		<div>
			<h3>说明</h3>
			<ul>
				<li>以天为单位，统计“普通用户活跃数”和“付费用户活跃数”，两者没有关联。</li>
				<li>当天数据，需要次日统计出来，如：2017-08-09日统计出2017-08-08的数据</li>
			</ul>
			<hr />
		</div>
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<table id="sample-table-1" class="table table-striped table-bordered table-hover">
					<thead>
                    <tr>
                        <th style="width: 200px;">日期</th>
                        <th>总活跃数</th>
                        <th>普通用户活跃数</th>
                        <th>付费用户活跃数</th>
                    </tr>
                    </thead>
					<tbody>
						<c:forEach items="${stats.list }" var="stat">
							<tr>
								<td>${stat.day }</td>
								<td>${stat.normalUserCount +  stat.payUserCount}</td>
								<td>${stat.normalUserCount }</td>
								<td>${stat.payUserCount }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
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
							<c:when test="${empty topics.nextCursor }">
								<li class="disabled">
									<a href="javascript:void(0);">下一页</a>
								</li>
							</c:when>
							<c:otherwise>
								<li class="">
									<a href="#${zweb:nextPage(topics) }" data-url="${zweb:nextPage(topics) }">
									下一页
									</a>
								</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
			</div><!-- /.span -->
		</div><!-- /.row --><!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->


<div id="modify-modal" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">更新状态</h4>
			</div>

			<form id="form-1">
				<div class="modal-body">
					<div class="row">
						<input type="hidden" id="topic-hid" name="topic"  />
						<div class="col-xs-12 col-sm-7">
							<div class="form-group">
								<label for="level"><font color="red">* </font> 状态</label>
									<div>
									<select name="status" id="status-sel">
									   <option value="Normal">正常显示</option>
									   <option value="Wait">敬请期待</option>
									   <option value="AdminDel">删除</option>
									 </select>
								</div>
							</div>
						</div>
					</div>
				</div>
	
				<div class="modal-footer">
					<button type="submit" class="btn btn-sm btn-primary" id="confirm-btn">
						<i class="ace-icon fa fa-check"></i>
						修改
					</button>
					<button class="btn btn-sm" data-dismiss="modal">
						<i class="ace-icon fa fa-times"></i>
						取消
					</button>
				</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

$(document).ready(function () {
	$('.status-a').click(function() {
		$('#topic-hid').val($(this).attr('topic'));
	});
	
	$('#confirm-btn').click(function () {
		var params = $('#form-1').serialize();
		$.ajax({
			url: '/topic/admin/status',
			type: 'post',
			data: params,
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					location.reload();
				} else {
					alert(result.meta.desc||result.meta.errorInfo);
				}
			}
		});
		
		return false;
	});
});

</script>