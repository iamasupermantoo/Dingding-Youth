<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>

<title>添加教师</title>

<link rel="stylesheet" href="/assets/css/jquery-ui.custom.min.css" />
<link rel="stylesheet" href="/assets/css/chosen.css" />
<link rel="stylesheet" href="/assets/css/daterangepicker.css" />
<link rel="stylesheet" href="/assets/css/bootstrap-datetimepicker.css" />
<link rel="stylesheet" href="/assets/css/colorpicker.css" />

<link rel="stylesheet" href="/assets/css/datepicker.css" />
<link rel="stylesheet" href="/assets/css/bootstrap-timepicker.css " />
<script type="text/javascript" src="/assets/js/date-time/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="/assets/js/date-time/bootstrap-timepicker.min.js"></script>
<script type="text/javascript" src="/assets/js/date-time/locales/bootstrap-datepicker.zh-CN.js"></script>

<!-- ajax layout which only needs content area -->
<div class="page-header">
	<h1>
		教师管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			添加教师信息
		</small>
	</h1>
</div><!-- /.page-header -->

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<form class="form-horizontal" id="add-form" role="form" action="/teacher/admin/add" method="post">
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="mobile-txt"><font color="red">* </font> 手机号: </label>

				<div class="col-sm-9">
					<input type="text" id="mobile-txt" name="mobile" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="password-pwd"><font color="red">* </font> 密码: </label>

				<div class="col-sm-9">
					<input type="password" id="password-pwd" name="password" class="col-xs-10 col-sm-5" />
				</div>
			</div>
		
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="name-txt"><font color="red">* </font> 真实姓名: </label>

				<div class="col-sm-9">
					<input type="text" id="name-txt" name="name" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="head-image-file"> 头像: </label>

				<div class="col-sm-3">
					<input type="file" id="head-image-file" name="headImage" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="head-image-file"> 教师图片: </label>

				<div class="col-sm-3">
					<input type="file" id="image-file" name="image" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="gender-sel"><font color="red">* </font>性别:</label>
				
				<div class="col-sm-9">
					<select id="gender-sel" name="gender">
					   <option value="male">男</option>
					   <option value="female">女</option>
					 </select>
				 </div>
			</div>
			
			<div class="space-4"></div>

			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="email-txt"> email: </label>

				<div class="col-sm-9">
					<input type="text" id="email-txt" name="email" class="col-xs-10 col-sm-5" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="email-txt"> 出生日期: </label>

				<div class="col-sm-9">
					<input type="text" id="birthday" name="birthday" class="col-xs-10 col-sm-5" />
				</div>
			</div>

			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right" for="desc-textarea"> 备注: </label>

				<div class="col-sm-9">
					<textarea class="col-xs-10 col-sm-5 limited" id="desc-textarea" name="desc" maxlength="50"></textarea>
				</div>
			</div>

			<div class="clearfix form-actions">
				<div class="col-md-offset-3 col-md-9">
					<button id="add-btn" class="btn btn-info" type="submit">
						<i class="ace-icon fa fa-check bigger-110"></i>
						添加
					</button>

					&nbsp; &nbsp; &nbsp;
					<button class="btn" type="reset">
						<i class="ace-icon fa fa-undo bigger-110"></i>
						清空
					</button>
				</div>
			</div><!-- /.row -->
		</form>

		<!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->
<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

$('#head-image-file').ace_file_input({
	no_file:'未选择',
	btn_choose:'选择图片',
	btn_change:'Change',
	droppable:false,
	onchange:null,
	thumbnail:false
});

$('#birthday').datepicker({
	format:"yyyy-mm-dd",
	language: 'zh-CN',
	autoclose: true,
	todayHighlight:true,
	todayBtn:true 
});

</script>
<!-- 文件上传 -->
<script type="text/javascript">
	jQuery(function($) {
		var $form = $('#add-form');
		//you can have multiple files, or a file input with "multiple" attribute
		var file_input = $form.find('input[type=file]');
		var upload_in_progress = false;
		
		file_input.ace_file_input({
			no_file:'未选择',
			btn_choose:'选择图片',
			btn_change:'更换',
			droppable:false,
			onchange:null,
			thumbnail:false,
			
			maxSize: 1048576,//bytes
			allowExt: ["jpeg", "jpg", "png", "gif"],
			allowMime: ["image/jpg", "image/jpeg", "image/png", "image/gif"],

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
			// if( !files || files.length == 0 ) return false;//no files selected
			var deferred ;
			//if( "FormData" in window ) {
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
					success: function(result, status, xhr) {
						meta = result.meta;
						if(meta.code == 1) {
							alert('注册老师账号成功');
							location.href = '#/teacher/admin/list'
						} else {
							alert("注册失败：" + meta.desc + 
									(meta.errInfo ? '\n\n信息:\n'+ meta.errInfo : ''));
						}
					}
				})

			//}
			/* else {
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
			} */


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

