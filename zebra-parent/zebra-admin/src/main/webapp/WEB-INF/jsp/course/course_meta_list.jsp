<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>课程列表</title>
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
			课程列表
		</small>
	</h1>
</div><!-- /.page-header -->


<p>
	<button class="btn btn-white btn-default btn-round">
		<a href="#modal-form" role="button" class="blue" data-toggle="modal">添加课程</a>
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
							<th>id</th>
							<th>名称</th>
							<th>课程logo</th>
							<th>教材</th>
							<th>价格</th>
							<th>级别</th>
							<th style="display: none;">类型</th>
							<th>总课时</th>
							<th>类型</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="courseMeta" items="${courseMetas.list}">
						<tr id="tr-${courseMeta.cmId }">
							<td>${courseMeta.cmId }</td>
							<td>
								<span id="name-span-${courseMeta.cmId }">${courseMeta.name}</span>
							</td>
							<td><img src="${zweb:imageUrl(courseMeta.image, 50, 50) }" /></td>
							<td><span id="book-name-span-${courseMeta.cmId }">${courseMeta.book.name }</span>
								<input id="bid-hid-${courseMeta.cmId }" type="hidden" value="${courseMeta.book.bid }" />
							</td>
							
							<td><span id="price-span-${courseMeta.cmId }">${courseMeta.price }</span></td>
							
							<td><span id="level-span-${courseMeta.cmId }">${courseMeta.level }</span></td>
							<td style="display: none;"><span id="type-span-${courseMeta.cmId }">${courseMeta.type }</span>
								<input type="hidden" id="desc-hid-${courseMeta.cmId }" value="${courseMeta.desc }" />
								<input type="hidden" id="subNotes-hid-${courseMeta.cmId }" value="${courseMeta.subNotes }" />
								<input type="hidden" id="suitableCrowds-hid-${courseMeta.cmId }" value="${courseMeta.suitableCrowds }" />
								<input type="hidden" id="joinCnt-hid-${courseMeta.cmId }" value="${courseMeta.joinCnt }" />
								<input type="hidden" id="joinCnt-hid-${courseMeta.cmId }" value="${courseMeta.joinCnt }" />
								<input type="hidden" id="share-jump-url-hid-${courseMeta.cmId }" value="${courseMeta.shareJumpUrl }" />
								<input type="hidden" id="share-brief-hid-${courseMeta.cmId }" value="${courseMeta.shareBrief }" />
								
								
							</td>
							<td><span id="total-cnt-span-${courseMeta.cmId }">${courseMeta.totalCnt }</span></td>
							<td>
								<c:if test="${courseMeta.type == 1 }">
									非试听
								</c:if>
								<c:if test="${courseMeta.type == 2 }">
									试听课
								</c:if>
							</td>
							<td>
								${zweb:datetime(courseMeta.createTime) }
							</td>
							<td>
								<a href="#modify-modal" role="button" class="blue modify-a" cmid="${courseMeta.cmId }" 
									data-toggle="modal">修改</a>&nbsp;|&nbsp;
								<c:if test="${courseMeta.status == 0 }">
									<a href="javascript:void(0);" class="Unshelved-a" cmid="${courseMeta.cmId }">下架</a>
								</c:if>
								<c:if test="${courseMeta.status == 1 or  courseMeta.status == 2}">
									<a href="javascript:void(0);" class="shelved-a" cmid="${courseMeta.cmId }">上架</a>
								</c:if>
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
			<c:when test="${empty courseMetas.nextCursor }">
				<li class="disabled">
					<a href="javascript:void(0);">下一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:nextPage(courseMetas) }" data-url="${zweb:nextPage(courseMetas) }">
					下一页
					</a>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>
<!-- 添加，弹出form -->
<div id="modal-form" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">添加课程</h4>
			</div>

			<form id="add-form" action="/course/meta/admin/add" method="post">
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

						<div class="space-4"></div>
						
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
						</div>
						<div class="space-4"></div>
						<div class="form-group">
							<label for="price"><font color="red">* </font> 价格</label>

							<div>
								<input id="price" name="price" class="input-large" type="text" />
							</div>
						</div>
						
						<div class="space-4"></div>
						<div class="form-group">
							<label for="level"><font color="red">* </font> 级别</label>

							<div>
								<select name="level" id="level">
								   <option value="1">Level1</option>
								   <option value="2">Level2</option>
								   <option value="3">Level3</option>
								   <option value="4">Level4</option>
								   <option value="5">Level5</option>
								   <option value="6">Level6</option>
								   
								   <option value="7">Level7</option>
								   <option value="8">Level8</option>
								   <option value="9">Level9</option>
								   <option value="10">Level10</option>
								   <option value="11">Level11</option>
								   <option value="12">Level12</option>
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
								<input id="desc" name="desc" class="input-large" type="text" />
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
							<label for="shareJumpUrl"> 微信分享链接</label>
							<div>
								<input id="shareJumpUrl" name="shareJumpUrl" class="input-large" type="text" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="shareImage">微信分享图片</label>
							<div>
								<input type="file" id="shareImage" name="shareImage"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="shareBrief">微信分享描述</label>
							<div>
								<input id="shareBrief" name="shareBrief" class="input-large" type="text" />
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

<div id="modify-modal" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">修改课程</h4>
			</div>

			<form id="modify-form" action="/course/meta/admin/modify" method="post">
			<div class="modal-body">
				<div class="row">
					<input type="hidden" id="cmId" name="cmId" value="" />
					<div class="col-xs-12 col-sm-7">
						<div class="form-group">
							<label for="name">小图</label>

							<div>
								<input type="file" id="image" name="image"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="name">大图</label>
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

						<div class="space-4"></div>
						
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
						</div>
						<div class="space-4"></div>
						<div class="form-group">
							<label for="price"><font color="red">* </font> 价格</label>

							<div>
								<input id="price" name="price" class="input-large" type="text" />
							</div>
						</div>
						
						<div class="space-4"></div>
						<div class="form-group">
							<label for="level"><font color="red">* </font> 级别</label>

							<div>
								<select name="level" id="level">
								   <option value="1">Level1</option>
								   <option value="2">Level2</option>
								   <option value="3">Level3</option>
								   <option value="4">Level4</option>
								   <option value="5">Level5</option>
								   <option value="6">Level6</option>
								   
								   <option value="7">Level7</option>
								   <option value="8">Level8</option>
								   <option value="9">Level9</option>
								   <option value="10">Level10</option>
								   <option value="11">Level11</option>
								   <option value="12">Level12</option>
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
							<label for="desc"><font color="red">* </font> 描述</label>
							<div>
								<input id="desc" name="desc" class="input-large" type="text" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="subNotes"><font color="red">* </font> 订阅须知</label>
							<div>
								<textarea class="col-xs-10 limited" style="height: 200px" 
									id="subNotes" name="subNotes" maxlength="500"></textarea>
							</div>
						</div>
						
						<div class="form-group">
							<label for="suitableCrowds"><font color="red">* </font> 事宜人群</label>
							<div>
								<input id="suitableCrowds" name="suitableCrowds" class="input-large" type="text" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="jionCnt"><font color="red">* </font> 参与人数</label>
							<div>
								<input id="joinCnt" name="joinCnt" class="input-large" type="text" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="shareJumpUrl"> 微信分享链接</label>
							<div>
								<input id="shareJumpUrl" name="shareJumpUrl" class="input-large" type="text" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="shareImage">微信分享图片</label>
							<div>
								<input type="file" id="shareImage" name="shareImage"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="shareBrief">微信分享描述</label>
							<div>
								<input id="shareBrief" name="shareBrief" class="input-large" type="text" />
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
					修改
				</button>
			</div>
			</form>
		</div>
	</div>
</div>


<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

$('.modify-a').click(function() {
	var cmId = $(this).attr('cmid');
	var cmTr = $('#tr-'+cmId);
	var name = cmTr.find('#name-span-'+cmId).text();
	var bid = cmTr.find('#bid-hid-'+cmId).val();
	var price = cmTr.find('#price-span-'+cmId).text();
	var level = cmTr.find('#level-span-'+cmId).text();
	var type = cmTr.find('#type-span-'+cmId).text();
	var desc = cmTr.find('#desc-hid-'+cmId).val();
	var subNotes = cmTr.find('#subNotes-hid-'+cmId).val();
	var suitableCrowds = cmTr.find('#suitableCrowds-hid-'+cmId).val();
	var joinCnt = cmTr.find('#joinCnt-hid-'+cmId).val();
	
	var shareJumpUrl = cmTr.find('#share-jump-url-hid-'+cmId).val();
	var shareBrief = cmTr.find('#share-brief-hid-'+cmId).val();
	
	$('#modify-modal').find('#cmId').val(cmId);
	$('#modify-modal').find('#name').val(name);
	$('#modify-modal').find('#bid').val(bid);
	$('#modify-modal').find('#price').val(price);
	$('#modify-modal').find('#level').val(level);
	$('#modify-modal').find('#type').val(type);		// ??
	$('#modify-modal').find('#desc').val(desc);
	$('#modify-modal').find('#subNotes').val(subNotes);
	$('#modify-modal').find('#suitableCrowds').val(suitableCrowds);
	$('#modify-modal').find('#joinCnt').val(joinCnt);
	$('#modify-modal').find('#shareJumpUrl').val(shareJumpUrl);
	$('#modify-modal').find('#shareBrief').val(shareBrief);
});



$('.Unshelved-a').click(function() {
	var result = confirm("确认下架吗？");
	if(!result) {
		return;
	}
	var cmId = $(this).attr('cmid');
	
	$.ajax({
		url: '/course/meta/admin/unshelve',
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
			var file_input = $(this).find('input[type=file]');
			var files = file_input.data('ace_input_files');
			
			/* if( !files || files.length == 0 ) {
				alert("请选择一个课程图片");
				return false;
			} */
			if(!$(this).find('input[name="name"]').val()) {
				alert("请输入课程名称");
				return false;
			}
			if(!$(this).find('select[name="bid"]').val()) {
				alert("请选择一个教材");
				return false;
			}
			if(!$(this).find('input[name="price"]').val()) {
				alert("请输入价格");
				return false;
			}
			if(!$(this).find('input[name="desc"]').val()) {
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
						alert('操作成功');
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