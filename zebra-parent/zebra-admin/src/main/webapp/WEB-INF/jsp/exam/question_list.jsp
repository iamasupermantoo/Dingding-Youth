<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>测评管理</title>
<link rel="stylesheet" href="/assets/css/chosen.css " />
<script type="text/javascript" src="/assets/js/chosen.jquery.min.js"></script>

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

.group {
	font-size: 20px;
}

.rea-label {
	font-weight:bold;
}

.q-brief {
	overflow:hidden;
	height: 38px;
	margin-left: 10px;
}

audio::-webkit-media-controls {
    overflow: hidden !important
}
audio::-webkit-media-controls-enclosure {
    width: calc(100% + 57px);
    margin-left: auto;
}

audio {
	width: 170px;
}

</style>


<div class="page-header">
	<h1>
		课程管理
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			测评管理
		</small>
	</h1>
</div><!-- /.page-header -->

<p>
	<button class="btn btn-white btn-default btn-round">
		<a href="#modal-form" role="button" class="blue add-title" data-toggle="modal">添加问题</a>
	</button>
</p>

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<table id="sample-table-1" class="table table-striped table-bordered table-hover" style="margin-bottom: 10px;">
					<thead>
						<tr>
							<th>题号</th>							
							<th>题干</th>
							<th>选项</th>
							<th>选项显示</th>
							<th>正确答案</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="question" items="${questions.list}" varStatus="qv">
							<tr>
								<td>
									${qv.index + 1}
								</td>
								<td>
									<c:forEach items="${question.titleItems }" var="item">
										<c:choose>
											<c:when test="${item.type ==  'Image'}">
												<img alt="" src="${item.image.getURL(50, 50) }"><br />
											</c:when>
										</c:choose>
										<c:choose>
											<c:when test="${item.type ==  'Audio'}">
												<span id="audio-span">
													<audio controls="controls"><source src="${item.audio }" type="audio/mp3" /></audio>
												</span><br />
											</c:when>
											<c:when test="${item.type == 'Text' }">
												<span>${item.text }</span><br />
											</c:when>
										</c:choose>
									</c:forEach>
								</td>
								<td>
									<c:forEach items="${question.optionItems }" var="item">
										${item.label }
										<c:forEach items="${item.items }" var="it">
											<c:choose>
												<c:when test="${it.type ==  'Image'}">
													<img alt="" src="${it.image.getURL(50, 50) }"><br />
												</c:when>
											</c:choose>
											<c:choose>
												<c:when test="${it.type ==  'Audio'}">
													<span id="audio-span">
														<audio controls="controls"><source src="${it.audio }" type="audio/mp3" /></audio>
													</span><br />
												</c:when>
												<c:when test="${it.type == 'Text' }">
													<span>${it.text }</span><br />
												</c:when>
											</c:choose>
										</c:forEach>
										
									</c:forEach>
								</td>
								<td>${question.mode.name }</td>
								<td>${question.rightAnswer }</td>
								
								<td>
									<a href="#modal-form" role="button" class="blue add-title" data-toggle="modal" 
										question-id="${question.id }">添加题干内容</a> 
										&nbsp;|&nbsp;
									<a href="#modal-form" role="button" class="blue add-option" data-toggle="modal"
										question-id="${question.id }">添加选项</a>
										&nbsp;|&nbsp;
									<a href="javascript:void(0);" class="blue del-a" data-toggle="modal"
										question-id="${question.id }">删除</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div><!-- /.span -->
		</div><!-- /.row --><!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->

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
			<c:when test="${empty questions.nextCursor }">
				<li class="disabled">
					<a href="javascript:void(0);">下一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:nextPage(questions) }" data-url="${zweb:nextPage(questions) }">
					下一页
					</a>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>

<div id="modal-form" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">添加</h4>
			</div>

			<form id="add-form" action="/exam/admin/question/title" method="post">
			
			<input type="hidden" value="${param.exam }" name="exam" />
			<input type="hidden" class="question-hid" name="question" />
			
			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12 col-sm-7">
						
						<div class="form-group" id="label-div" style="display: none;">
							<label for="label-txt"><font color="red">* </font>选项标号</label>

							<div>
								<input id="label-txt" name="label" placeholder="选项标号，如：A、1" class="input-large" type="text" />
							</div>
						</div>
						
						<div class="form-group" id="right-div" style="display: none;">
							<label for="right-txt"><font color="red">* </font>是否正确答案</label>
							<div>
								<select name="right" id="right-sel">
								   <option value="false">否</option>
								   <option value="true">是</option>
								</select>
							</div>
						</div>
						
						<div class="form-group" id="mode-div" style="display: none;">
							<label for="mode-sel"><font color="red">* </font> 展示方式</label>
							<div>
								<select name="mode" id="mode-sel">
								   <option value="_1">横排</option>
								   <option value="_2">竖排</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="type-sel"><font color="red">* </font> 类型</label>
							<div>
								<select name="type" id="type-sel">
								   <option value="">-- 请选择 --</option>
								   <option value="Image">图片</option>
								   <option value="Audio">音频</option>
								   <option value="Text">文字</option>
								 </select>
							</div>
						</div>
						
						<div class="form-group" id="text-div" style="display: none;">
							<label for="text"><font color="red">* </font>文字</label>

							<div>
								<input id="text-txt" name="text" class="input-large" type="text" />
							</div>
						</div>
						
						<div class="form-group" id="image-div" style="display: none;">
							<label for="image-file"><font color="red">* </font> 图片</label>
							<div style="width: 200px;">
								<input type="file" id="image-file" name="image" />
							</div>
						</div>
						
						<div class="form-group" id="audio-div" style="display: none;">
							<label for="audio-file"><font color="red">* </font> 音频</label>
							<div style="width: 200px;">
								<input type="file" id="audio-file" name="audio" />
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

$('#type-sel').change(function() {
	var type = $(this).val();
	if(type == 'Image') {
		$('#image-div').show();
		$('#audio-div').hide();
		$('#text-div').hide();
	} else if(type == 'Audio') {
		$('#audio-div').show();
		$('#image-div').hide();
		$('#text-div').hide();
	} else if(type == 'Text') {
		$('#text-div').show();
		$('#image-div').hide();
		$('#audio-div').hide();
	}
});

// title和option,公用同一个form表单
$('.add-option,.add-title').click(function() {
	var questionId = $(this).attr('question-id');
	$('.question-hid').val(questionId);
	
	var option = $(this).hasClass('add-option');
	var title = $(this).hasClass('add-title');
	
	if(option) {
		$('#add-form').attr('action', '/exam/admin/question/option');
		$('#label-div,#mode-div,#right-div').show();
	} else if(title) {
		$('#add-form').attr('action', '/exam/admin/question/title');
		$('#label-div,#mode-div,#right-div').hide();
	}
	
});

$('.del-a').click(function() {
	var result = confirm("确认删除问题吗？");
	if(!result) {
		return;
	}
	var questionId = $(this).attr('question-id');
	
	$.ajax({
		url: '/exam/admin/question/del',
		type: 'post',
		data: {'question': questionId},
		success: function(result, status, xhr) {
			if(result.meta.code == 1) {
				alert('删除成功');
				location.reload();
			} else {
				alert(result.meta.desc||result.meta.errorInfo);
			}
		}
	});
	return false;
});


</script>

<script type="text/javascript">
var upload_in_progress = false;
var $form = $('#add-form');
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

var audioFileJq = $form.find('#audio-file');
audioFileJq.ace_file_input({
	no_file:'未选择',
	btn_choose:'选择文件',
	btn_change:'更换',
	droppable:false,
	onchange:null,
	thumbnail:false,
	
	maxSize: 10048576,//bytes
	allowExt: ["mp3"],
	before_remove: function() {
		if(upload_in_progress)
			return false;//if we are in the middle of uploading a file, don't allow resetting file input
		return true;
	},

	preview_error: function(filename , code) {
		alert('error upload： ' + code)
	}
});

audioFileJq.on('file.error.ace', function(ev, info) {
	if(info.error_count['ext'] || info.error_count['mime']) alert('音频要求必须是MP3格式');
	if(info.error_count['size']) alert('音频大小不能超过10M');
});

$form.on('reset', function() {
	$(this).find('input[type=file]').ace_file_input('reset_input_ui');
});
</script>


<script type="text/javascript">
	jQuery(function($) {
		var $form = $('#add-form');
		
		var ie_timeout = null;//a time for old browsers uploading via iframe
		$('#add-form').on('submit', function(e) {
			e.preventDefault();
			
			// 数据校验
			var file_input = $(this).find('input[type=file]');
			var files = file_input.data('ace_input_files');
			
			var type = $('#type-sel').val();
			if(!type) {
				alert("请选择类型");
				return false;
			}
			if(type == 'Image') {
				var imageFile = $(this).find('#image-file').data('ace_input_files');
				if(!imageFile || imageFile.length == 0) {
					alert("请选择一个图片");
					return false;
				}
			}
			if(type == 'Audio') {
				var audioFile = $(this).find('#audio-file').data('ace_input_files');
				if(!audioFile || audioFile.length == 0) {
					alert("请选择一个音频");
					return false;
				}
			}
			
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
						alert('Not OK')
					},
					success : function() {
						alert('添加成功');
						location.reload();
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

