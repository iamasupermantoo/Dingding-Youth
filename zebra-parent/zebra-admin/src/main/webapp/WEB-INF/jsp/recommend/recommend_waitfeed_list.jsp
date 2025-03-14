<%@ page language="java" contentType="text/html; charset=UTF-8" session="false"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/inc/taglib.inc" %>
<title>小编推荐帖子列表</title>

<link rel="stylesheet" href="/assets/css/colorbox.css" />

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
	
	
	
	
	
	
});
</script>
<div class="page-header">
	<h1>推荐管理
	<small><i class="ace-icon fa fa-angle-double-right"></i>
		小编推荐
	</small>
	</h1>
</div>

<div class="row">
	<div class="col-xs-12">
		<div class="row" style="width: 1050px;">
			<div class="">
				<table id="sample-table-1"
					class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<td width="350">内容</td>
							<td>类型</td>
							<td>推荐人</td>
							<td>推荐人所在主题</td>
							<td>操作</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${feeds.list }" var="feed" varStatus="status">
						<tr>
							<c:if test="${feed.type==1 }">
								<td>
									<ul class=" clearfix" style="width: 500px">
											
											<a href="javascript:void()" data-rel="colorbox">
											<img width="150" height="150" alt="150x150" src="${feed.image.getURL(150, 150) }" />
											</a>
											<div class="text">
												<div class="inner">${feed.desc }</div>
												<div class="inner">作者：${feed.author.name }</div>
												<div class="inner">发布时间：${hweb:dateTime(feed.createTime) }</div>
											</div>
											
											<input type="hidden" class="feed-id-hid" value="${feed.id }"/>
											
										
									</ul>
								</td>
								<td>图片</td>
							</c:if>
							
							<c:if test="${feed.type==2 }">
								<td>
									<%-- <ul class=" clearfix" style="width: 500px;" >
											
												<c:forEach items="${feed.images }" var="image" >
													<a href="javascript:void()" data-rel="colorbox">
														<img width="150" height="150" alt="150x150" src="${image.getURL(150, 150) }" />
														
													</a>
												</c:forEach>
												<div class="text">
													<div class="inner">标题：${feed.desc }</div>
													<div class="inner">作者：${feed.author.name }</div>
													<div class="inner">发布时间：${hweb:dateTime(feed.createTime) }</div>
													
												</div>
												<input type="hidden" class="feed-id-hid" value="${feed.id }"/>
											
										
									</ul> --%>
									<ul class="ace-thumbnails clearfix" style="width: 500px;">
										<c:forEach var="image" items="${feed.images}"  >
											<li class="images-li">
												<a href="${image.getURL()}" data-rel="colorbox">
													<img width="70" height="70" alt="80x80" src="${image.getURL()}" />
													<div class="text">
														<div class="inner">点击查看大图</div>
													</div>
												</a>
											</li>
										</c:forEach>
									</ul>
									<div class="text">
										<div class="inner">标题：${feed.desc }</div>
										<div class="inner">作者：${feed.author.name }</div>
										<div class="inner">发布时间：${hweb:dateTime(feed.createTime) }</div>
										
									</div>
									<input type="hidden" class="feed-id-hid" value="${feed.id }"/>
									
									
								</td>
								<td>多图</td>
							</c:if>
							
							<c:if test="${feed.type==0 }">
								<td height="150">
									<ul class=" clearfix" style="width: 500px">
										<a href="#/post/admin/details?post=${feed.postId }" 
											style="text-decoration: none; color: black;" target="_blank" title="在新窗口打开，查看文章详情">
											<div align="center">
												<h5><b>${feed.title }</b></h5>
											</div>
											<div>
													<hweb:richAbbreviate length="80" rawValue="${feed.brief }"/>
												
											</div>
										</a>
										<input type="hidden" class="feed-id-hid" value="${feed.id }"/>
										<p>
										<div class="text">
											<div class="inner">作者：${feed.author.name }</div>
											<div class="inner">发布时间：${hweb:dateTime(feed.createTime) }</div>
										</div>
									</ul>
								</td>
								<td>文章</td>
							</c:if>
							<td>
								${feed.referee.name }
							</td>
							<td>
								${feed.topic.name }
							</td>
							<td>
								<a class="cancel-b" href="javascript:put('${feed.id }');"><font color="blue">允许推荐</font></a>
								<a class="cancel-c" href="javascript:del('${feed.id }');"><font color="red">拒绝推荐</font></a>
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			
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
									<a href="#${hweb:firstPage() }" data-url="${hweb:firstPage() }">
										<i class="ace-icon fa fa-angle-double-left">首页</i>
									</a>
								</li>
								<li class="">
									<a href="javascript:history.go(-1)">上一页</a>
								</li>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${empty feeds.nextCursor }">
								<li class="disabled">
									<a href="javascript:void(0);">下一页</a>
								</li>
							</c:when>
							<c:otherwise>
								<li class="">
									<a href="#${hweb:nextPage(feeds) }" data-url="${hweb:nextPage(feeds) }">
									下一页
									</a>
								</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
			
		</div>
	</div>
</div>
<script type="text/javascript">
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
	
	
	function put(id){
		var result = confirm("确认推荐到首页吗？");
		if(!result) {
			return;
		}
		//alert(id);
		
		$.ajax({
			url: '/recommend/admin/allowFeed',
			type: 'post',
			data: {
				id:id
			},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					alert('推荐成功');
					location.reload();
				} else {
					alert(result.meta.desc||result.meta.errorInfo);
				}
			}
		});
	}
	
	function del(id){
		var result = confirm("确认拒绝推荐吗？");
		if(!result) {
			return;
		}
		//alert(id);
		
		$.ajax({
			url: '/recommend/admin/refuseFeed',
			type: 'post',
			data: {
				id:id
			},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					alert('成功');
					location.reload();
				} else {
					alert(result.meta.desc||result.meta.errorInfo);
				}
			}
		});
	}
	
</script>

<script type="text/javascript">
	var scripts = [null,"/assets/js/jquery.colorbox-min.js", null]
	ace.load_ajax_scripts(scripts, function() {
		 jQuery(function($) {
			var $overflow = '';
			var colorbox_params = {
				rel: 'colorbox',
				reposition:true,
				scalePhotos:true,
				scrolling:false,
				previous:'<i class="ace-icon fa fa-arrow-left"></i>',
				next:'<i class="ace-icon fa fa-arrow-right"></i>',
				close:'&times;',
				current:'{current} of {total}',
				maxWidth:'100%',
				maxHeight:'100%',
				onOpen:function(){
					$overflow = document.body.style.overflow;
					document.body.style.overflow = 'hidden';
				},
				onClosed:function(){
					document.body.style.overflow = $overflow;
				},
				onComplete:function(){
					$.colorbox.resize();
				}
			};
		
			$('.images-li').click(function() {
				$.colorbox.remove();
				$(this).parent().find('[data-rel="colorbox"]').colorbox(colorbox_params);
			});
			
		})
	});
</script>