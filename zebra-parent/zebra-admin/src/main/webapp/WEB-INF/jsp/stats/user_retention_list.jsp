<%@ page language="java" contentType="text/html; charset=UTF-8" session="false"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/inc/taglib.inc" %>
<title>主题列表</title>
<div class="page-header">
	<h1>
		数据统计
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			用户留存分析
		</small>
	</h1>
</div><!-- /.page-header -->

<style>
.tab-content>.active {
display: table;
}
</style>

<div class="row">
	<div class="col-xs-12">
		<div>
			<h3>说明</h3>
			<ul>
				
				<li>当天后数据，需要次日统计出来，如：2017-08-09日统计出2017-08-08的数据</li>
			</ul>
			<hr />
		</div>
		<!-- PAGE CONTENT BEGINS -->
		<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
				<li class="active">
					<a data-toggle="tab" href="#normal">
						普通用户
					</a>
				</li>
				<li>
					<a data-toggle="tab" href="#pay">
						付费用户
					</a>
				</li>
			</ul>
		
			<div class="tab-content">
				<table id="normal" class="table table-striped table-bordered table-hover tab-pane fade in active">
					<thead>
                    <tr>
                        <th style="width: 200px;"></th>
                        <th>注册数</th>
                        <th>1天后</th>
                        <th>2天后</th>
                        <th>3天后</th>
                        <th>4天后</th>
                        <th>5天后</th>
                        <th>6天后</th>
                        <th>7天后</th>
                        <th>14天后</th>
                        <th>21天后</th>
                        <th>28天后</th>
                    </tr>
                    </thead>
					<tbody>
						<c:forEach items="${stats.list }" var="stat">
							<tr>
								<td>${stat.day }</td>
								<td>${stat.normalUserCount }</td>
								<c:forEach items="${stat.normalRates }" var="rate">
									<td>
										<c:choose>
											<c:when test="${rate == 'NaN'}">0%</c:when>
											<c:when test="${rate == '.0' }">0%</c:when>
											<c:when test="${not empty rate }">${rate }%</c:when>
										</c:choose>
									</td>
								</c:forEach>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<table id="pay" class="table table-striped table-bordered table-hover tab-pane fade in">
					<thead>
                    <tr>
                        <th style="width: 200px;"></th>
                        <th>付费数</th>
                        <th>1天后</th>
                        <th>2天后</th>
                        <th>3天后</th>
                        <th>4天后</th>
                        <th>5天后</th>
                        <th>6天后</th>
                        <th>7天后</th>
                        <th>14天后</th>
                        <th>21天后</th>
                        <th>28天后</th>
                    </tr>
                    </thead>
					<tbody>
						<c:forEach items="${stats.list }" var="stat">
							<tr>
								<td>${stat.day }</td>
								<td>${stat.payUserCount }</td>
								<c:forEach items="${stat.payRates }" var="rate">
									<td>
										<c:choose>
											<c:when test="${rate == 'NaN'}">0%</c:when>
											<c:when test="${rate == '.0' }">0%</c:when>
											<c:when test="${not empty rate }">${rate }%</c:when>
										</c:choose>
									</td>
								</c:forEach>
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