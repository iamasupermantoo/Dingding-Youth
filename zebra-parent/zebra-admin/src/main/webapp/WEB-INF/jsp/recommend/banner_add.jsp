<%@ page language="java" contentType="text/html; charset=UTF-8" session="false"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/inc/taglib.inc" %>
<title>添加banner</title>
<link rel="stylesheet" href="/assets/css/jquery-ui.custom.min.css" />
<link rel="stylesheet" href="/assets/css/chosen.css" />
<link rel="stylesheet" href="/assets/css/bootstrap-timepicker.css" />
<link rel="stylesheet" href="/assets/css/daterangepicker.css" />
<link rel="stylesheet" href="/assets/css/bootstrap-datetimepicker.css" />
<link rel="stylesheet" href="/assets/css/colorpicker.css" />
<script type="text/javascript" src="/assets/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="/assets/js/additional-methods.min.js"></script>

<style type="text/css">

.in-valid {
	margin-right: 20px;
}

</style>
<script type="text/javascript">
// 参数的隐藏和显示
$('.param').hide();
$('#gotoPage').change(function() {
	var goto = $(this).val();
	$('.param').hide();
	$('#'+goto).show();
});

</script>

<!-- ajax layout which only needs content area -->
<div class="page-header">
	<h1>
		推荐管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			添加banner
		</small>
	</h1>
</div><!-- /.page-header -->

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<form class="form-horizontal" id="form-1" role="form" action="/recommend/admin/addBanner" method="post">
			
			
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right" for="level-sel"><font color="red">*</font> 跳转:</label>
				
				<div class="col-sm-9">
					<select id="gotoPage" name="gotoPage">
						<option value="COURSE">课程终端页</option>
						<option value="URL">外部URL</option>
						<option value="NONE" selected="selected">不跳转</option>
					</select>
				 </div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right" for="type-sel"><font color="red">*</font> 类型:</label>
				<div class="col-sm-9">
					<select id="type-sel" name="type">
						<option value="APP">APP 首页</option>
						<!-- <option value="TOPICS_H5">主题 H5</option>
						<option value="TIMELINE">成长记录 底部</option> -->
					</select>
				 </div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right" for="image-file"><font color="red">*</font> 图片: </label>

				<div class="col-sm-3">
					<input type="file" id="image-file" name="image" />
				</div>
			</div>
			
			<span id="desc-span">
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right"><font color="red">*</font> 描述: </label>
	
					<div class="col-sm-9">
						<input type="text" name="desc" placeholder="请输入描述信息" class="col-sm-5 in-valid" />
					</div>
				</div>
			</span>
			
			
			<span id="FEEDBACK" class="param">
				<!-- nothing -->
			</span>
			
			<span id="NONE" class="param">
				<!-- nothing -->
			</span>
			
			<span id="COURSE" class="param">
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right"><font color="red">*</font>课程id: </label>
	
					<div class="col-sm-9">
						<input type="text" name="dataId" placeholder="请输入课程id" class="col-sm-5 in-valid" />
					</div>
				</div>
			</span>
			<span id="PROFILE" class="param">
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right"><font color="red">*</font> 用户id: </label>
	
					<div class="col-sm-9">
						<input type="text" name="dataId" placeholder="请输入用户id" class="col-sm-5 in-valid" />
					</div>
				</div>
			</span>
			
			<span id="URL" class="param">
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right"><font color="red">*</font> URL: </label>
	
					<div class="col-sm-9">
						<input type="text" name="url" placeholder="请输入URL" class="col-sm-5 in-valid" />
					</div>
				</div>
			</span>
			
			<span id="QUESTION" class="param">
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right"><font color="red">*</font> 问题id: </label>
	
					<div class="col-sm-9">
						<input type="text" name="qid" placeholder="请输入问题id" class="col-sm-5 in-valid" />
					</div>
				</div>
			</span>
			
			<span id="ANSWER" class="param">
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right"><font color="red">*</font> 问题id: </label>
	
					<div class="col-sm-9">
						<input type="text" name="qid" placeholder="请输入问题id" class="col-sm-5 in-valid" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right"><font color="red">*</font> 回答id: </label>
	
					<div class="col-sm-9">
						<input type="text" name="aid" placeholder="请输入回答id" class="col-sm-5 in-valid" />
					</div>
				</div>
			</span>
			
			<span id="ISSUE" class="param">
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right"><font color="red">*</font> 订阅id: </label>
	
					<div class="col-sm-9">
						<input type="text" name="subId" placeholder="请输入订阅id" class="col-sm-5 in-valid" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right"><font color="red">*</font> issue id: </label>
	
					<div class="col-sm-9">
						<input type="text" name="issueId" placeholder="请输入issue id" class="col-sm-5 in-valid" />
					</div>
				</div>
			</span>
						
			<div class="clearfix form-actions">
				<div class="col-md-offset-3 col-md-9">
					<button id="confirm-btn" class="btn btn-info" type="submit">
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


$('#desc-span').hide();
$('#type-sel').change(function() {
	var type = $(this).val();
	if(type == 'TIMELINE') {
		$('#desc-span').show();
	} else {
		$('#desc-span').hide();
	}
	
	return false;
});



// 图片上传
var upload_in_progress = false;
var $form = $('#form-1');
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
			return false;//if we are in the middle of uploading a file, don't allow resetting file input
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

$form.on('reset', function() {
	$(this).find('input[type=file]').ace_file_input('reset_input_ui');
});

// 表单校验
$('#confirm-btn').click(function() {
	$('#form-1').validate({
		errorElement: 'span',
		errorClass: 'help-block',
		focusInvalid: false,
		rules: {
			dataId: {
				required: true
			},
			url: {
				required: true,
				url: true,
			},
			qid: {
				required: true
			},
			aid: {
				required: true
			},
			subId: {
				required: true
			},
			issueId: {
				required: true
			},
		},

		messages: {
			dataId: '请输入id',
			url: {
				required: '请输入URL地址',
				url: 'URL地址格式不正确'
			},
			qid: '请输入问题id',
			aid: '请输入回答id',
			subId: '请输入订阅id',
			issueId: '请输入issue id',
		},
		
		highlight: function (e) {
			$(e).closest('.form-group').removeClass('has-info').addClass('has-error');
		},

		success: function (e) {
			$(e).closest('.form-group').removeClass('has-error');//.addClass('has-info');
			$(e).remove();
		},

		errorPlacement: function (error, element) {
			if(element.is('input[type=checkbox]') || element.is('input[type=radio]')) {
				var controls = element.closest('div[class*="col-"]');
				if(controls.find(':checkbox,:radio').length > 1) controls.append(error);
				else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
			}
			else if(element.is('.select2')) {
				error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
			}
			else if(element.is('.chosen-select')) {
				error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
			}
			else error.insertAfter(element);
		},

		submitHandler: function (form) {
			var imageFileJq = $form.find('#image-file');
			var image = imageFileJq.data('ace_input_files');
			if( !image || image.length == 0 ) {
				alert("必须选择一个图片");
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
						location.href = '/#/recommend/admin/query/banner?type=APP';
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
		},
		invalidHandler: function (form) {
		}
	});
});



</script>
