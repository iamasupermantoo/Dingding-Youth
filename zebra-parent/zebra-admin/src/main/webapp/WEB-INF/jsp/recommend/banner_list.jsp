<%@ page language="java" contentType="text/html; charset=UTF-8" session="false"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/inc/taglib.inc" %>
<title>首页焦点管理</title>
<script type="text/javascript" src="/assets/js/date-time/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="/assets/js/date-time/bootstrap-timepicker.min.js"></script>
<link rel="stylesheet" type="text/css" href="/recommend/banner.css"/>
<script type="text/javascript" src="/recommend/banner.js?v=1"></script>

<style>

.q-input {
	padding: 3px;
}

</style>

<div class="page-header">
	<h1>
		推荐管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			顶部焦点图
		</small>
	</h1>
</div><!-- /.page-header -->
<p>
	<button class="btn btn-white btn-default btn-round">
		<a href="#/recommend/admin/addBannerUI" class="blue">添加banner图</a>
	</button>
	<button class="btn btn-white btn-default btn-round" id="btn_save_order">
		<a href="javascript:void(0);" class="blue">保存当前顺序</a>
	</button>
</p>

<div class="row">
	<div class="col-xs-12">
		<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
				<li class="${type == 'APP' ? 'active' : '' }">
					<a data-toggle="tab" href="#/recommend/admin/query/banner?type=APP" class="banner-a" id="app-a">
						APP首页
					</a>
				</li>
			</ul>
		
			<div class="tab-content">
				<!-- 列表 -->
				<table id="table_banner" class="table table-striped table-bordered table-hover">
				<tr>
					<td>拖动+号</td>
					<td>图片</td>
					<td width="150">跳转</td>
					<td width="50">状态</td>
					<!-- <td width="100">上线时间</td>
					<td width="100">下线时间</td> -->
					<td>操作</td>
				</tr>
				<tbody id="tbody-drag">
				<c:forEach items="${ banners}" var="banner">
						<tr id="${banner.id }" banner-status="${banner.status }" class="${banner.status == 1 || banner.status == 2 ? 'offline' : ''}">
							<td>+</td>
							<td><img alt="" src="${banner.image.getURL(130, 70) }"></td>
							<td>${zenum:gotoPageType(banner.gotoPageType) }</td>
							<td>${zenum:bannerStatus(banner.status) }</td>
							<%-- <td>${hweb:dateTime(banner.startTime) }</td>
							<td>${hweb:dateTime(banner.endTime) }</td> --%>
							<td>
								<a class="a_op" banner-id="${banner.id }" banner-status="${banner.status }" href="javascript:void(0);">
									${banner.status == 1 ? '上线':'下线'}</a>
							</td>
						</tr>
				</c:forEach>
				</tbody>
			</table>
			</div><!-- /.span -->
		</div><!-- /.row --><!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->
<script type="text/javascript">
	var scripts = []
	ace.load_ajax_scripts(scripts);
	
	$('.datetime-txt').datepicker({
		format: 'yyyy-mm-dd hh:mm:ss',
		autoclose: true,
		todayHighlight: true
	});
	
	
</script>

<script type="text/javascript">
	function showErrorAlert (reason, detail) {
		var msg='';
		if (reason==='unsupported-file-type') { msg = "Unsupported format " +detail; }
		else {
			//console.log("error uploading file", reason, detail);
		}
		$('<div class="alert"> <button type="button" class="close" data-dismiss="alert">&times;</button>'+ 
		 '<strong>File upload error</strong> '+msg+' </div>').prependTo('#alerts');
	}
</script>

<!-- 处理文件上传、提交动作 -->
<script type="text/javascript">
	var upload_in_progress = false;
	
	var $form = $('#add_form');
	var imageFileJq = $form.find('#image-file');
	
	imageFileJq.ace_file_input({
		no_file:'未选择',
		btn_choose:'选择文件',
		btn_change:'更换',
		droppable:false,
		onchange:null,
		thumbnail:false,
		
		maxSize: 2048576,//bytes
		allowExt: ["jpeg", "jpg", "png"],
		before_remove: function() {
			if(upload_in_progress)
				return false;
			return true;
		},

		preview_error: function(filename , code) {
			alert('error upload： ' + code)
		}
	});
	
	
	imageFileJq.on('file.error.ace', function(ev, info) {
		if(info.error_count['ext'] || info.error_count['mime']) alert('图片要求必须是jpeg、jpg或png格式');
		if(info.error_count['size']) alert('图片大小不能超过2M');
	});
	
	// 提交
	$('#add-btn').click(function(e) {
		var result = confirm("确认添加吗？");
		if(!result) {
			return;
		}
		e.preventDefault();
		
		var imageFileJq = $form.find('#image-file');
		var image = imageFileJq.data('ace_input_files');
		if( !image || image.length == 0 ) {
			alert("必须选择一个顶部图片");
			return false;
		}
		
		var formData_object = new FormData();
		$.each($form.serializeArray(), function(i, item) {
			formData_object.append(item.name, item.value);							
		});
		
		var fileJqs = $form.find('#image-file');
		fileJqs.each(function() {
			var field_name = $(this).attr('name');
			var files = $(this).data('ace_input_files');
			if(files && files.length > 0) {
				for(var f = 0; f < files.length; f++) {
					formData_object.append(field_name, files[f]);
				}
			}
		});

		upload_in_progress = true;
		fileJqs.ace_file_input('loading', true);
		
		var deferred = $.ajax({
	        url: $form.attr('action'),
	        type: $form.attr('method'),
			processData: false,//important
			contentType: false,//important
			dataType: 'json',
			data: formData_object,
			error: function(xhr, error, exception) {
				alert('失败');
			},
			success : function(result, status, xhr) {
				meta = result.meta;
				if(meta.code == 1) {
					alert('成功');
					location.href = '/#/recommend/admin/query/banner';
				} else {
					alert(meta.desc + 
							(meta.errInfo ? '\n\n信息:\n'+ meta.errInfo : ''));
				}
			}
		});

		deferred.always(function() {
			upload_in_progress = false;
			fileJqs.ace_file_input('loading', false);
		});

		deferred.promise();
	});

	$form.on('reset', function() {
		$(this).find('input[type=file]').ace_file_input('reset_input_ui');
	});
</script>



