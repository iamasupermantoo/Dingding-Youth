<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>添加课程</title>

<!-- <link rel="stylesheet" href="/assets/css/datepicker.css" /> -->
<link rel="stylesheet" href="/assets/css/bootstrap-datetimepicker.min.css" />

<script type="text/javascript" src="/assets/js/date-time/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" src="/assets/js/date-time/bootstrap-datetimepicker.min.js"></script>
<!-- <script type="text/javascript" src="/assets/js/date-time/locales/bootstrap-datepicker.fr.js"></script> -->
<script type="text/javascript" src="/assets/js/date-time/locales/bootstrap-datepicker.zh-CN.js"></script>


<style type="text/css">
table {
	border-collapse: collapse;
}

table td {
	padding: 3px;
}

.td-label {
	text-align: right;
}

.td-input {
	text-align: left;
	padding-right: 10px;
}

.q-input {
	padding: 3px;
}
</style>

<div class="page-header">
	<h1>
		课程管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			添加公开课
		</small>
	</h1>
</div><!-- /.page-header -->


<div class="row">
	<div class="col-xs-12">
		
		

	<div class="modal-dialog col-xs-12">
			<form id="add-form" action="/opencourse/meta/admin/add" method="post">
			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12 col-sm-7">
						<div class="form-group">
							<label for="name"><font color="red">* </font>小图</label>

							<div>
								<input type="file" id="image" name="image"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="name"><font color="red">* </font>大图</label>
							<div>
								<input type="file" id="big-image" name="bigImage"/>
							</div>
						</div>
					
						<div class="form-group">
							<label for="name"><font color="red">* </font>名称</label>

							<div>
								<input id="name" name="name" class="input-large" type="text" />
							</div>
						</div>

						<%-- <div class="space-4"></div>
						
						<div class="form-group">
							<label for="bid"><font color="red">* </font>教材</label>
							<div>
								<select name="bid" id="bid">
								   <option value="">--- 请选择 ---</option>
								   <c:forEach items="${books }" var="book">
								   		<option value="${book.id }">${book.name }</option>
								   </c:forEach>
								 </select>
							</div>
						</div> --%>
						
						<div class="space-4"></div>
						<div class="form-group">
							<label for="price"><font color="red">* </font> 价格</label>

							<div>
								<input id="price" name="price" class="input-large" type="text"  onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" />
							</div>
						</div>
						
						
						<div class="form-group">
							<label for="teacher"><font color="red">* </font> 教师</label>

							<div>
								<select name="teacher" id="teacher">
								  <!-- <option >请选择教师</option> -->
								 </select>
							</div>
						</div>
						
						
						<div class="form-group">
							<label for="level"><font color="red">* </font> 类型</label>

							<div>
								<select name="type" id="type-sel">
								   <option value="1">普通课</option>
								   <option value="2">试听课</option>
								 </select>
							</div>
						</div>
						
						<div class="space-4"></div>
						<div class="form-group">
							<label for="level"><font color="red">* </font> 描述</label>
							<div>
								<!-- <input id="desc" name="desc" class="input-large" type="text" /> -->
								<textarea class="col-xs-10 limited" style="height: 100px" 
									id="desc" name="desc" maxlength="500"></textarea>
							</div>
						</div>
						
						<div class="form-group">
							<label for="level"><font color="red">* </font> 订阅须知</label>
							<div>
								<textarea class="col-xs-10 limited" style="height: 200px" 
									id="notes-textarea" name="subNotes" maxlength="500"></textarea>
							</div>
						</div>
						
						<div class="form-group">
							<label for="suitableCrowds-txt"><font color="red">* </font> 事宜人群</label>
							<div>
								<input id="suitableCrowds-txt" name="suitableCrowds" class="input-large" type="text" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="jionCnt-txt"><font color="red">* </font> 参与人数</label>
							<div>
								<input id="joinCnt" name="joinCnt" class="input-large" type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="openTime-txt"><font color="red">* </font> 开课时间</label>
							<div>
								<input id="openTime-txt" name="openTime" class="input-large date dateTime" type="text" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="shareDesc"> 分享描述</label>
							<div>
								<input id="shareDesc" name="shareDesc" class="input-large" type="text" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="share-image">分享图片</label>
							<div>
								<input type="file" id="share-image" name="shareImage"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="shareUrl-txt"> 分享链接</label>
							<div>
								<input id="shareUrl-txt" name="shareUrl" class="input-large" type="text" />
							</div>
						</div>

					</div>
				</div>
			</div>

			
				<button class="btn btn-sm" data-dismiss="modal">
					<i class="ace-icon fa fa-times"></i>
					取消
				</button>

				<button type="submit" class="btn btn-sm btn-primary" id="addBtn">
					<i class="ace-icon fa fa-check"></i>
					添加
				</button>
			
			</form>
	</div>
		
	</div><!-- /.col -->
</div><!-- /.row -->




<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);


$('.dateTime').datetimepicker({
	format:"yyyy-mm-dd hh:ii",
	language: 'zh-CN',
	autoclose: true,
	todayHighlight:true,
	todayBtn:true 
});


$('.modify-a').click(function() {
	var cmId = $(this).attr('cmid');
	var cmTr = $('#tr-'+cmId);
	var name = cmTr.find('#name-span-'+cmId).text();
	//var bid = cmTr.find('#bid-hid-'+cmId).val();
	var price = cmTr.find('#price-span-'+cmId).text();
	/* var level = cmTr.find('#level-span-'+cmId).text(); */
	var type = cmTr.find('#type-span-'+cmId).text();
	var desc = cmTr.find('#desc-hid-'+cmId).val();
	var subNotes = cmTr.find('#subNotes-hid-'+cmId).val();
	var suitableCrowds = cmTr.find('#suitableCrowds-hid-'+cmId).val();
	var openTime = cmTr.find('#openTime-hid-'+cmId).val();
	var joinCnt = cmTr.find('#joinCnt-hid-'+cmId).val();
	var shareDesc = cmTr.find('#shareDesc-hid-'+cmId).val();
	var shareUrl = cmTr.find('#shareUrl-hid-'+cmId).val();
	var teacher = cmTr.find('#teacher-hid-'+cmId).val();
	
	$('#modify-modal').find('#cmId').val(cmId);
	$('#modify-modal').find('#name').val(name);
	//$('#modify-modal').find('#bid').val(bid);
	$('#modify-modal').find('#price').val(price);
	//$('#modify-modal').find('#level').val(level);
	$('#modify-modal').find('#type').val(type);		// ??
	$('#modify-modal').find('#desc').val(desc);
	$('#modify-modal').find('#subNotes').val(subNotes);
	$('#modify-modal').find('#suitableCrowds').val(suitableCrowds);
	$('#modify-modal').find('#openTime-txt').val(openTime);
	$('#modify-modal').find('#joinCnt').val(joinCnt);
	$('#modify-modal').find('#shareDesc').val(shareDesc);
	$('#modify-modal').find('#shareUrl').val(shareUrl);
	$('#modify-modal').find('#teacher').val(teacher);
});



$('.Unshelved-a').click(function() {
	var result = confirm("确认下架吗？");
	if(!result) {
		return;
	}
	var cmId = $(this).attr('cmid');
	
	$.ajax({
		url: '/opencourse/meta/admin/unshelve',
		type: 'post',
		data: {'cmId': cmId},
		success: function(result, status, xhr) {
			meta = result.meta;
			if(meta.code == 1) {
				alert('下架成功');
				location.reload();
			} else {
				alert(meta.desc + 
						(meta.errInfo ? '\n\n信息:\n'+ meta.errInfo : ''));
			}
		}
	});
	
	return false;
});

$('#s-query-btn').click(function() {
	var params = $('#s-query-form').serialize();
	location.href="#/course/meta/admin/list?" + params;
});


addteachers();
function addteachers(){
	$.ajax({
		url: '/teacher/admin/allList',
		type: 'get',
		data: {},
		success: function(result, status, xhr) {
			
			var teachers = result.list;
			for (var i = 0; i < teachers.length; i++) {
				var tea = teachers[i];
				$("#teacher").append('<option value="'+ tea.id +'">'+ tea.name +'</option>');
			}
			
		}
	});
}


</script>
<script type="text/javascript">
	jQuery(function($) {
		var $form = $('#add-form,#modify-form');
		//you can have multiple files, or a file input with "multiple" attribute
		var file_input = $form.find('input[type=file]');
		var upload_in_progress = false;

		file_input.ace_file_input({
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
				//code = 1 means file load error
				//code = 2 image load error (possibly file is not an image)
				//code = 3 preview failed
				alert('error upload： ' + code)
			}
		});
		
		
		file_input.on('file.error.ace', function(ev, info) {
			if(info.error_count['ext'] || info.error_count['mime']) alert('不支持的图片格式');
			if(info.error_count['size']) alert('文件不能超过5M');
			
			//you can reset previous selection on error
			//ev.preventDefault();
			//file_input.ace_file_input('reset_input');
		});
		
		var ie_timeout = null;//a time for old browsers uploading via iframe
		$('#add-form,#modify-form').on('submit', function(e) {
			e.preventDefault();
			// alert($(this).attr('action'));
			// 数据校验
			var file_input = $(this).find('input[name="image"]');
			var files = file_input.data('ace_input_files');
			
			if( !files || files.length == 0 ) {
				alert("请选择一个课程小图");
				return false;
			} 
			
			var bigImage = $(this).find('input[name="bigImage"]');
			var bigImg = bigImage.data('ace_input_files');
			
			if( !bigImg || bigImg.length == 0 ) {
				alert("请选择一个课程大图");
				return false;
			} 

			if(!$(this).find('input[name="name"]').val()) {
				alert("请输入课程名称");
				return false;
			}
			/* if(!$(this).find('select[name="bid"]').val()) {
				alert("请选择一个教材");
				return false;
			} */
			if(!$(this).find('input[name="price"]').val()) {
				alert("请输入价格");
				return false;
			}
			/* if(!$(this).find('input[name="teacher"]').val()) {
				alert("请选择教师");
				return false;
			} */
			if(!$(this).find('textarea[name="desc"]').val()) {
				alert("请输入课程描述");
				return false;
			}
			if(!$(this).find('textarea[name="subNotes"]').val()) {
				alert("请输入订阅须知");
				return false;
			}
			if(!$(this).find('input[name="suitableCrowds"]').val()) {
				alert("请输入事宜人群");
				return false;
			}
			
			if(!$(this).find('input[name="joinCnt"]').val()) {
				alert("请输入参与人数");
				return false;
			} 
			
			if(!$(this).find('input[name="openTime"]').val()) {
				alert("请输入开课时间");
				return false;
			}
			
/* 			if(!$(this).find('input[name="shareUrl"]').val()) {
				alert("请输入分享链接");
				return false;
			}
			if(!$(this).find('input[name="shareDesc"]').val()) {
				alert("请输入分享描述");
				return false;
			} */
			
			/* alert($(this).find('input[name="type"]').val());
			alert($(this).find('input[name="level"]').val()); */
			
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
				$.each($(this).serializeArray(), function(i, item) {
					//add them one by one to our FormData 
					formData_object.append(item.name, item.value);							
				});
				//and then add files
				$(this).find('input[type=file]').each(function(){
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
				$(".btn").attr("disabled","disabled");
				deferred = $.ajax({
			        url: $(this).attr('action'),
			        type: $(this).attr('method'),
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
						$(".btn").removeAttr("disabled");
						alert('Not OK')
					},
					success : function() {
						$(".btn").removeAttr("disabled");
						alert('添加课程成功');
						location.href="#/opencourse/meta/admin/list";
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