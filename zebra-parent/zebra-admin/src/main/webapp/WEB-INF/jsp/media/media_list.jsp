<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>多媒体资源列表</title>
<div class="page-header">
	<h1>
		资源库管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			多媒体资源列表
		</small>
	</h1>
</div><!-- /.page-header -->

<p>
	<button class="btn btn-white btn-default btn-round">
		<a href="#add-form" id="add-frame-a" role="button" class="blue" data-toggle="modal">添加资源</a>
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
							<th>类型</th>
							<th>名称</th>
							<th>媒体地址</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="media" items="${medias.list}">
						<tr>
							<td class="center">
								<label class="position-relative">
									<input type="checkbox" class="ace" />
									<span class="lbl"></span>
								</label>
							</td>

							<td>
								 ${zenum:mediaType(media.type)}
							</td>
							<td>${media.name}</td>
							<td>
								<a href="${media.mediaUrl}" target="view_window">${media.mediaUrl }</a>
							</td>

							<td>
								<a href="#">编辑</a>
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
				<h4 class="blue bigger">添加资源</h4>
			</div>

			<form id="add-form" action="/media/meta/admin/add" method="post">
			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12 col-sm-5">
						<div class="space"></div>
						<input type="file" id="file" name="file"/>
					</div>
					
					<div class="col-xs-12 col-sm-7">
						<div class="form-group">
							<label for="name">名称</label>

							<div>
								<input id="name" name="name" class="input-large" type="text" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="level">媒体类型</label>

							<div>
								<select name="type" id="type">
								   <option value="Image">图片</option>
								   <option value="Audio">音频</option>
								   <option value="Video">视频</option>
								 </select>
							</div>
						</div>
						<div class="space-4"></div>
						<div class="form-group">
							<label for="level">描述</label>
							<div>
								<input id="desc" name="desc" class="input-large" type="text" />
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
</script>

<script type="text/javascript">
	jQuery(function($) {
		var $form = $('#add-form');
		//you can have multiple files, or a file input with "multiple" attribute
		var file_input = $form.find('input[type=file]');
		var upload_in_progress = false;
	
		file_input.ace_file_input({
			style : 'well',
			btn_choose : '图片上传，支持拖拽',
			btn_change: null,
			// no_icon:'ace-icon fa fa-cloud-upload',
			droppable: true,
			thumbnail: 'large',
			
			maxSize: 1048576,//bytes
			allowExt: ["jpeg", "jpg", "png", "gif", "mp3", "mp4"],
			allowMime: ["image/jpg", "image/jpeg", "image/png", "image/gif", "audio/mpeg", "video/mp4"],

			before_remove: function() {
				if(upload_in_progress)
					return false;//if we are in the middle of uploading a file, don't allow resetting file input
				return true;
			},

			preview_error: function(filename , code) {
				//code = 1 means file load error
				//code = 2 image load error (possibly file is not an image)
				//code = 3 preview failed
				alert('error upload' + code)
			}
		});
		
		
		file_input.on('file.error.ace', function(ev, info) {
			if(info.error_count['ext'] || info.error_count['mime']) alert('不支持的图片格式');
			if(info.error_count['size']) alert('文件不能超过1M');
			
			//you can reset previous selection on error
			//ev.preventDefault();
			//file_input.ace_file_input('reset_input');
		});
		
		
		var ie_timeout = null;//a time for old browsers uploading via iframe
		$form.on('submit', function(e) {
			e.preventDefault();
		
			var files = file_input.data('ace_input_files');
			if( !files || files.length == 0 ) return false;//no files selected
								
			var deferred ;
			if( "FormData" in window ) {
				//for modern browsers that support FormData and uploading files via ajax
				//we can do >>> var formData_object = new FormData($form[0]);
				//but IE10 has a problem with that and throws an exception
				//and also browser adds and uploads all selected files, not the filtered ones.
				//and drag&dropped files won't be uploaded as well
				
				//so we change it to the following to upload only our filtered files
				//and to bypass IE10's error
				//and to include drag&dropped files as well
				formData_object = new FormData();//create empty FormData object
				
				//serialize our form (which excludes file inputs)
				$.each($form.serializeArray(), function(i, item) {
					//add them one by one to our FormData 
					formData_object.append(item.name, item.value);							
				});
				//and then add files
				$form.find('input[type=file]').each(function(){
					var field_name = $(this).attr('name');
					//for fields with "multiple" file support, field name should be something like `myfile[]`
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
						alert('OK');
					}
				})

			}
			else {
				//for older browsers that don't support FormData and uploading files via ajax
				//we use an iframe to upload the form(file) without leaving the page

				deferred = new $.Deferred //create a custom deferred object
				
				var temporary_iframe_id = 'temporary-iframe-'+(new Date()).getTime()+'-'+(parseInt(Math.random()*1000));
				var temp_iframe = 
						$('<iframe id="'+temporary_iframe_id+'" name="'+temporary_iframe_id+'" \
						frameborder="0" width="0" height="0" src="about:blank"\
						style="position:absolute; z-index:-1; visibility: hidden;"></iframe>')
						.insertAfter($form)

				$form.append('<input type="hidden" name="temporary-iframe-id" value="'+temporary_iframe_id+'" />');
				
				temp_iframe.data('deferrer' , deferred);
				//we save the deferred object to the iframe and in our server side response
				//we use "temporary-iframe-id" to access iframe and its deferred object
				
				$form.attr({
							  method: 'POST',
							 enctype: 'multipart/form-data',
							  target: temporary_iframe_id //important
							});

				upload_in_progress = true;
				file_input.ace_file_input('loading', true);//display an overlay with loading icon
				$form.get(0).submit();
				
				
				//if we don't receive a response after 30 seconds, let's declare it as failed!
				ie_timeout = setTimeout(function(){
					ie_timeout = null;
					temp_iframe.attr('src', 'about:blank').remove();
					deferred.reject({'status':'fail', 'message':'Timeout!'});
				} , 30000);
			}


			////////////////////////////
			//deferred callbacks, triggered by both ajax and iframe solution
			deferred
			.done(function(result) {//success
				//format of `result` is optional and sent by server
				//in this example, it's an array of multiple results for multiple uploaded files
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


		//when "reset" button of form is hit, file field will be reset, but the custom UI won't
		//so you should reset the ui on your own
		$form.on('reset', function() {
			$(this).find('input[type=file]').ace_file_input('reset_input_ui');
		});
	});
</script>
