<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>编辑章节教案</title>
<div class="page-header">
	<h1>
		教材管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			编辑章节教案
		</small>
	</h1>
</div><!-- /.page-header -->


<style>
.table>tbody>tr>td, .table>tbody>tr>th, .table>tfoot>tr>td, .table>tfoot>tr>th, .table>thead>tr>td, .table>thead>tr>th {
	vertical-align: middle;
}

</style>

<p>
	<button class="btn btn-white btn-default btn-round">
		<a href="#add-form" id="add-frame-a" role="button" class="blue" data-toggle="modal">添加一帧</a>
	</button>
</p>

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<table class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th class="center">
								<label class="position-relative">
									<input type="checkbox" class="ace" />
									<span class="lbl"></span>
								</label>
							</th>
							<th>展示顺序</th>
							<th>媒体类型</th>
							<th>内容</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody id="table-1">
						<c:forEach var="frame" items="${frames}">
						
						<c:if test="${!frame.isChild }">
							<c:set value="${frame.idx }" var="parentIdx" scope="request"></c:set>
						</c:if>
						
						<tr>
							<td class="center">
								<c:if test="${!frame.isChild }">
									<label class="position-relative">
										<input type="checkbox" class="ace" />
										<span class="lbl"></span>
									</label>
								</c:if>
							</td>
							<td>${frame.idx + 1 }</td>
							<td>
								 ${zenum:mediaType(frame.type)}
							</td>
							<td style="width: 150px; height: 100px">
								<c:choose>
									<c:when test="${frame.type == 0 }">
										<a href="${zweb:imageUrl(frame.image, 1400, 1000) }" target="view_window">
										<img src="${zweb:imageUrl(frame.image, 140, 100) }" /></a>
									</c:when>
									<c:when test="${frame.type == 1 }">
										<a href="${frame.audio}" target="view_window">点击播放</a>
									</c:when>
									<c:when test="${frame.type == 2 }">
										<a href="${frame.video}" target="view_window">点击观看</a>
									</c:when>
								</c:choose>
							</td>

							<td>
								<%-- <a href="#add-form" role="button" class="modify-a" idx="${frame.idx }" 
									parent-idx="${frame.isChild ? parentIdx : '' }" data-toggle="modal">修改</a> --%>
									
									
									
								<c:if test="${not frame.isChild }">
									<a href="#add-form" role="button" class="add-child" idx="${frame.idx }" 
										data-toggle="modal">添加子帧</a>&nbsp;|&nbsp;
								</c:if>
								<a class="remove-a" idx="${frame.idx }" href="javascript:void(0);"
									parent-idx="${frame.isChild ? parentIdx : '' }" data-toggle="modal">删除</a>
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div><!-- /.span -->
		</div><!-- /.row --><!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->

<!-- 添加，弹出form -->
<div id="add-form" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">添加一帧</h4>
			</div>

			<form id="add-form-s" action="/frame/config/add" method="post">
			<div class="modal-body" style="height: 203px; padding-top: 0px;">
				<div class="row">
					<div class="col-xs-12 col-sm-5">
						<div class="space"></div>
						<input type="file" id="mediaFile" name="mediaFile"/>
					</div>
					
					<div class="col-xs-12 col-sm-7" style="margin-top: 20px;">
						<input type="hidden" id="cid" name="cid" value="${param.cid }"/>
						<input type="hidden" id="parentIdx" name="parentIdx" />
											
						<div class="form-group">
							<label for="name">类型</label>
							<div>
								<input id="type" name="type" disabled="disabled" value="图片" class="input-large" type="text" />
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

				<button type="submit" class="btn btn-sm btn-primary" id="addBtn">
					<i class="ace-icon fa fa-check"></i>
					添加
				</button>
			</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
	var scripts = []
	ace.load_ajax_scripts(scripts);
		
	$('.add-child').click(function() {
		var parentIdx = $(this).attr('idx');
		$('#parentIdx').val(parentIdx);
	});
	
	// 删除
	$('.remove-a').click(function() {
		var parentIdx = $(this).attr('parent-idx');
		var childIdx = null;
		
		var message = null;
		if(!parentIdx) {
			parentIdx = $(this).attr('idx');
			message = "确认删除吗，子帧将一并被删除";
		} else {
			childIdx = $(this).attr('idx');
			message = "确认删除子帧吗";
		}
		
		var result = confirm(message);
		if(!result) {
			return;
		}
		
		var cid = $('#cid').val();
		
		$.ajax({
			url: '/frame/config/remove',
			type: 'post',
			data: {'cid': cid, 'parentIdx': parentIdx, 'childIdx': childIdx},
			success: function(result, status, xhr) {
				meta = result.meta;
				if(meta.code == 1) {
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

<script type="text/javascript">
	jQuery(function($) {
		var $form = $('#add-form-s');
		var file_input = $form.find('input[type=file]');
		var upload_in_progress = false;
	
		file_input.ace_file_input({
			style : 'well',
			btn_choose : '媒体文件上传',
			btn_change: null,
			droppable: true,
			thumbnail: 'large',
			
			maxSize: 50048576,//bytes
			allowExt: ["jpeg", "jpg", "png", "mp3", "mp4"],

			before_remove: function() {
				if(upload_in_progress)
					return false;
				return true;
			},

			preview_error: function(filename , code) {
				alert('error upload' + code)
			}
		});
		
		
		file_input.on('file.error.ace', function(ev, info) {
			if(info.error_count['ext'] || info.error_count['mime']) alert('不支持的文件格式');
			if(info.error_count['size']) alert('文件不能超过50M');
		});
		
		
		var ie_timeout = null;//a time for old browsers uploading via iframe
		$form.on('submit', function(e) {
			e.preventDefault();
		
			var files = file_input.data('ace_input_files');
			if( !files || files.length == 0 ) return false;//no files selected
								
			var deferred ;
			if( "FormData" in window ) {
				formData_object = new FormData();//create empty FormData object
				$.each($form.serializeArray(), function(i, item) {
					formData_object.append(item.name, item.value);							
				});
				$form.find('input[type=file]').each(function(){
					var field_name = $(this).attr('name');
					var files = $(this).data('ace_input_files');
					if(files && files.length > 0) {
						for(var f = 0; f < files.length; f++) {
							formData_object.append(field_name, files[f]);
						}
					}
				});

				upload_in_progress = true;
				file_input.ace_file_input('loading', true);
				deferred = $.ajax({
			        url: $form.attr('action'),
			        type: $form.attr('method'),
					processData: false,//important
					contentType: false,//important
					dataType: 'json',
					data: formData_object,
					xhr: function() {
						var req = $.ajaxSettings.xhr();
						if (req && req.upload) {
							req.upload.addEventListener('progress', function(e) {
								if(e.lengthComputable) {	
									var done = e.loaded || e.position, total = e.total || e.totalSize;
									var percent = parseInt((done/total)*100) + '%';
									//percentage of uploaded file
								}
							}, false);
						}
						return req;
					},
					beforeSend : function() {
					},
					error: function() {
						alert('Not OK')
					},
					success : function() {
						location.reload();
					}
				})

			}
			else {
				deferred = new $.Deferred //create a custom deferred object
				
				var temporary_iframe_id = 'temporary-iframe-'+(new Date()).getTime()+'-'+(parseInt(Math.random()*1000));
				var temp_iframe = 
						$('<iframe id="'+temporary_iframe_id+'" name="'+temporary_iframe_id+'" \
						frameborder="0" width="0" height="0" src="about:blank"\
						style="position:absolute; z-index:-1; visibility: hidden;"></iframe>')
						.insertAfter($form)

				$form.append('<input type="hidden" name="temporary-iframe-id" value="'+temporary_iframe_id+'" />');
				
				temp_iframe.data('deferrer' , deferred);
				$form.attr({
							  method: 'POST',
							 enctype: 'multipart/form-data',
							  target: temporary_iframe_id //important
							});

				upload_in_progress = true;
				file_input.ace_file_input('loading', true);//display an overlay with loading icon
				$form.get(0).submit();
				
				ie_timeout = setTimeout(function(){
					ie_timeout = null;
					temp_iframe.attr('src', 'about:blank').remove();
					deferred.reject({'status':'fail', 'message':'Timeout!'});
				} , 30000);
			}

			deferred
			.done(function(result) {//success
				var message = '';
				for(var i = 0; i < result.length; i++) {
					if(result[i].status == 'OK') {
						message += "File successfully saved. Thumbnail is: " + result[i].url
					}
					else {
						message += "File not saved. " + result.message;
					}
					message += "\n";
				}
				// alert(message);
			})
			.fail(function(result) {//failure
				alert("There was an error");
			})
			.always(function() {//called on both success and failure
				if(ie_timeout) clearTimeout(ie_timeout)
				ie_timeout = null;
				upload_in_progress = false;
				file_input.ace_file_input('loading', false);
			});

			deferred.promise();
		});
		$form.on('reset', function() {
			$(this).find('input[type=file]').ace_file_input('reset_input_ui');
		});
	});
</script>
