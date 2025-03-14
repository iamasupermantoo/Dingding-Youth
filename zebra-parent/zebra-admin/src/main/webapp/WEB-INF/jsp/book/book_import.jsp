<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>

<title>导入教材</title>

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


<!-- ajax layout which only needs content area -->
<div class="page-header">
	<h1>
		课程管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			导入教材
		</small>
	</h1>
</div><!-- /.page-header -->

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<form class="form-horizontal" id="form-1" role="form" action="/book/admin/import" method="post">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right" for="name-txt"><font color="red">*</font> 教材名: </label>

				<div class="col-sm-9">
					<input type="text" id="name-txt" name="name" placeholder="请输入教材名" class="col-sm-5 in-valid" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right" for="zip-file"><font color="red">*</font> 资源zip包: </label>

				<div class="col-sm-3">
					<input type="file" id="zip-file" name="zip" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right" for="excel-file"><font color="red">*</font> 课程excel: </label>

				<div class="col-sm-3">
					<input type="file" id="excel-file" name="excel" />
				</div>
			</div>
			
			<div class="clearfix form-actions">
				<div class="col-md-offset-3 col-md-9">
					<button id="confirm-btn" class="btn btn-info" type="submit">
						<i class="ace-icon fa fa-check bigger-110"></i>
						导入
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

var upload_in_progress = false;
var $form = $('#form-1');
var zipFileJq = $form.find('#zip-file');
zipFileJq.ace_file_input({
	no_file:'未选择',
	btn_choose:'选择文件',
	btn_change:'更换',
	droppable:false,
	onchange:null,
	thumbnail:false,
	
	maxSize: 200048576,//bytes
	allowExt: ["zip"],
	before_remove: function() {
		if(upload_in_progress)
			return false;//if we are in the middle of uploading a file, don't allow resetting file input
		return true;
	},

	preview_error: function(filename , code) {
		alert('error upload： ' + code)
	}
});


zipFileJq.on('file.error.ace', function(ev, info) {
	if(info.error_count['ext'] || info.error_count['mime']) alert('必须上传zip包');
	if(info.error_count['size']) alert('zip包大小不能超过200M');
});

var excelFileJq = $form.find('#excel-file');
excelFileJq.ace_file_input({
	no_file:'未选择',
	btn_choose:'选择文件',
	btn_change:'更换',
	droppable:false,
	onchange:null,
	thumbnail:false,
	
	maxSize: 50048576,//bytes
	allowExt: ["xls", "xlsx"],
	before_remove: function() {
		if(upload_in_progress)
			return false;//if we are in the middle of uploading a file, don't allow resetting file input
		return true;
	},

	preview_error: function(filename , code) {
		alert('error upload： ' + code)
	}
});

excelFileJq.on('file.error.ace', function(ev, info) {
	if(info.error_count['ext'] || info.error_count['mime']) alert('必须上传xls或者xlsx格式的文件');
	if(info.error_count['size']) alert('excel文件大小不能超过50M');
});

$form.on('reset', function() {
	$(this).find('input[type=file]').ace_file_input('reset_input_ui');
});

// 表单校验
$('#confirm-btn').click(function() {
	/* jQuery.validator.addMethod("region", function (value, element) {
		var province = $('#province').val();
		var region = $('#city').val();
		if(province == 0 || region == 0) {
			return false;
		}
		return true;
	}, "请选择所在地"); */
	
	$('#form-1').validate({
		errorElement: 'span',
		errorClass: 'help-block',
		focusInvalid: false,
		rules: {
			name: {
				required: true,
			},
			num: {
				required: true,
			},
			author: {
				required: true
			},
			publisher: {
				required: true
			},
			publishTime: {
				required: true
			},
			pages: {
				required: true,
			},
			words: {
				required: true,
				digits:true,
				min: 1,
			},
			price: {
				required: true
			},
			level: {
				required: true
			},
			guide: {
				maxlength: 500
			},
			brief: {
				required: true,
				maxlength: 500
			},
			catalogue: {
				maxlength: 500
			}
		},

		messages: {
			name: '请输入学校名称',
			num: '请输入ISBN编号',
			author: '请输入作者',
			publisher: '请输入出版社',
			publishTime: '请输入出版日期',
			pages: '请输入页数',
			words: '请输入字数',
			price: '请输入价格',
			level: '请选择阅读层级',
			guide: '引导阅读最多500个字',
			brief: {
				required: '请输入内容简介',
				maxlength: '内容简介最多500个字'
			},
			catalogue: '目录最多500个字'
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
			var zipFileJq = $form.find('#zip-file');
			var zipfile = zipFileJq.data('ace_input_files');
			/* if( !zipfile || zipfile.length == 0 ) {
				alert("必须选择一个书籍封面图");
				return false;
			} */
			
			var formData_object = new FormData();
			$.each($form.serializeArray(), function(i, item) {
				formData_object.append(item.name, item.value);							
			});
			
			var fileJqs = $form.find('#zip-file');
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
						location.href = '/#/book/list';
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
