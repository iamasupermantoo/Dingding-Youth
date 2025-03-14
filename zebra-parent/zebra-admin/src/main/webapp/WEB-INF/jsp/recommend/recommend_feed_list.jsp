<%@ page language="java" contentType="text/html; charset=UTF-8" session="false"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/inc/taglib.inc" %>
<title>当前首页内容</title>
<link rel="stylesheet" href="../../assets/css/colorbox.css" />
<script type="text/javascript">
$(document).ready(function() {
	// 添加feed
	$('#addFeed').click(function() {
		var dataId = $('#dataId').val();
		var feedtype = $('#type').val();
		if(!dataId || !feedtype) {
			alert("信息不完整");
			return false;
		}
		
		$.ajax({
			url: '/recommend/admin/addFeed',
			type: 'post',
			data: {'dataId': dataId, 'type': feedtype, 'params': ''},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					alert('提交成功');
					location.reload();
				} else {
					alert(result.desc||result.errorInfo);
				}
			}
		});
		
		return false;
	});
	
	// 从首页移除推荐内容
	$('.cancel-a').click(function() {
		var _this = $(this);
		var result = confirm("确认从首页移除吗？");
		if(!result) {
			return;
		}
		
		var feedIdHids = $(this).parent().parent()
			.find('.feed-id-hid');
		var data = '';
		feedIdHids.each(function(idx, ele) {
			data = data + '&feeds[]=' + $(ele).val();
		});
		
		//alert(data);
		
		$.ajax({
			url: '/recommend/admin/removeFeed',
			type: 'post',
			data: data,
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					alert('移除成功');
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
<div class="page-header">
	<h1>推荐内容
	<small><i class="ace-icon fa fa-angle-double-right"></i>
		当前首页
	</small>
	</h1>
</div>

<p>
	<button class="btn btn-white btn-default btn-round">
		<a href="#/recommend/admin/addFeedUI" class="blue">添加首页内容</a>
	</button>
</p>

<div class="row">
	<div class="col-xs-12">
		<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
				<li class="${param.type == 'TRY_COURSE' ? 'active' : '' }">
					<a data-toggle="tab" href="#/recommend/admin/trials?type=TRY_COURSE" class="feed-a" id="try-course-a">
						试听课程
					</a>
				</li>
				<li class="${param.type == 'TRY_LIVE' ? 'active' : '' }">
					<a data-toggle="tab" href="#/recommend/admin/opencourses?type=TRY_LIVE" class="feed-a" id="try-live-a">
						开课信息
					</a>
				</li>
				<li class="${param.type == 'WEIXIN_ARTICLE' ? 'active' : '' }">
					<a data-toggle="tab" href="#/recommend/admin/informations?type=WEIXIN_ARTICLE" class="feed-a" id="weixin-article-a">
						资讯
					</a>
				</li>
			</ul>
		
			<div class="tab-content">
				<c:if test="${param.type == 'TRY_COURSE' }">
					<table id="table_feed" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<td width="350">标题</td>
							<td>图片</td>
							<td>参与人数</td>
							<td>开课时间</td>
							<td>类型</td>
							<td>操作</td>
						</tr>
					</thead>
					
					<tbody id="tbody-drag">
					<c:forEach items="${ feeds.list}" var="feed">
							<tr>
								<td>${feed.title }</td>
								<td><img src="${feed.image.getURL(50, 50) }" /></td>
								<td>${feed.joinCnt }</td>
								<td>${feed.openTime }</td>
								<td>
									<c:if test="${feed.dataType == 0 }">
										1v1
									</c:if>
									<c:if test="${feed.dataType == 1 }">
										公开课
									</c:if>
								</td>
								<td>
									<a href="javascript:void(0);">取消推荐</a>
								</td>
							</tr>
					</c:forEach>
					</tbody>
				</table>
			</c:if>
			
			
			<c:if test="${param.type == 'TRY_LIVE' }">
					<table id="table_feed" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<td width="350">标题</td>
							<td>图片</td>
							<td>参与人数</td>
							<td>开课时间</td>
							<td>操作</td>
						</tr>
					</thead>
					
					<tbody id="tbody-drag">
					<c:forEach items="${ feeds.list}" var="feed">
							<tr>
								<td>${feed.title }</td>
								<td><img src="${feed.image.getURL(50, 50) }" /></td>
								<td>${feed.joinCnt }</td>
								<td>${feed.openTime }</td>
								<td>
									<a href="javascript:void(0);">取消推荐</a>
								</td>
							</tr>
					</c:forEach>
					</tbody>
				</table>
			</c:if>
			
			<c:if test="${param.type == 'WEIXIN_ARTICLE' }">
					<table id="table_feed" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<td width="350">标题</td>
							<td>图片</td>
							<td>链接地址</td>
							<td>发布时间</td>
							<td>操作</td>
						</tr>
					</thead>
					
					<tbody id="tbody-drag">
					<c:forEach items="${ feeds.list}" var="feed">
							<tr>
								<td>${feed.title }</td>
								<td><img src="${feed.image.getURL(50, 50) }" /></td>
								<td><a href="${feed.url }">点击查看</a></td>
								<td>${feed.pubTime }</td>
								<td>
									<a href="javascript:void(0);">取消推荐</a>
								</td>
							</tr>
					</c:forEach>
					</tbody>
				</table>
			</c:if>
			
			</div><!-- /.span -->
		</div><!-- /.row --><!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->

<script type="text/javascript">
	$('.feed-a').click(function() {
		var href = $(this).attr('href');
		location.href = href;
		return false;
	});

	var scripts = [ null, "../../assets/js/jquery.colorbox-min.js", null ]
	ace.load_ajax_scripts(scripts, function() {
		jQuery(function($) {
			var $overflow = '';
			var colorbox_params = {
				rel : 'colorbox',
				reposition : true,
				scalePhotos : true,
				scrolling : false,
				previous : '<i class="ace-icon fa fa-arrow-left"></i>',
				next : '<i class="ace-icon fa fa-arrow-right"></i>',
				close : '&times;',
				current : '{current} of {total}',
				maxWidth : '100%',
				maxHeight : '100%',
				onOpen : function() {
					$overflow = document.body.style.overflow;
					document.body.style.overflow = 'hidden';
				},
				onClosed : function() {
					document.body.style.overflow = $overflow;
				},
				onComplete : function() {
					$.colorbox.resize();
				}
			};

			$('.ace-thumbnails [data-rel="colorbox"]').colorbox(
					colorbox_params);
		})
	});
</script>