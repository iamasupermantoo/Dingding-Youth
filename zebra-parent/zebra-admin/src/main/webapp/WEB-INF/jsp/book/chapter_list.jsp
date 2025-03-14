<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>教案 - 章节列表</title>
<div class="page-header">
	<h1>
		教材章节
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			教材章节列表
		</small>
	</h1>
</div><!-- /.page-header -->

<p>
	<button class="btn btn-white btn-default btn-round">
		<a href="#add-model" id="add-chapter-a" role="button" class="blue" data-toggle="modal">添加章节</a>
	</button>
</p>

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<table id="chapter-table" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th class="center">
								<label class="position-relative">
									<input type="checkbox" class="ace" />
									<span class="lbl"></span>
								</label>
							</th>
							<th>名称</th>
							<th>课次</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="chapter" items="${chapters.list}">
						<tr id="tr-${chapter.cid }">
							<td class="center">
								<label class="position-relative">
									<input type="checkbox" class="ace" />
									<span class="lbl"></span>
								</label>
							</td>

							<td>
								<a href="#"><span id="span-label-${chapter.cid}">${chapter.label}</span></a>
							</td>
							<td class="cnt-td"><span id="span-cnt-${chapter.cid}">${chapter.cnt}</span></td>
							<td>
								${zweb:datetime(chapter.createTime) }
								
								<input type="hidden" id="hid-homeworkTitle-${chapter.cid }" value="${chapter.homeworkTitle }" />
								<input type="hidden" id="hid-homeworkContent-${chapter.cid }" value="${chapter.homeworkContent }" />
								<input type="hidden" id="hid-desc-${chapter.cid }" value="${chapter.desc }" />
							</td>

							<td>
								<a href="#/frame/config/list?cid=${chapter.cid }">编辑教案</a>&nbsp; | &nbsp;
								<a href="#modify-modal" role="button" class="blue modify-a" bid="${book.bid }" cid="${chapter.cid }"
									data-toggle="modal">修改</a>&nbsp; | &nbsp;
								<a href="javascript:void(0);" class="remove-chapter" cid="${chapter.cid }" >删除</a>
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
			<c:when test="${empty chapters.nextCursor }">
				<li class="disabled">
					<a href="javascript:void(0);">下一页</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="">
					<a href="#${zweb:nextPage(chapters) }" data-url="${zweb:nextPage(chapters) }">
					下一页
					</a>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>

<div id="add-model" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">添加章节</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12 col-sm-7">
						<form id="form-1">
						<div class="form-group">
							<input type="hidden" id="bid-hid" name="bid" value="${param.bid }"/>
							<label for="label-txt"><font color="red">* </font>章节名称</label>
							<div>
								<input class="input-large" type="text" id="label-txt"  name=""  value="" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="cnt-txt">课次</label>
							<div>
								<input class="input-large" type="text" id="cnt-txt" disabled="disabled"/>
							</div>
						</div>

						<div class="form-group">
							<label for="homeworkTitle"><font color="red">* </font> 作业标题</label>
							<div>
								<input class="input-large" type="text" id="homeworkTitle" name="homeworkTitle" value="" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="homeworkContent"><font color="red">* </font> 作业内容</label>
							<textarea class="form-control limited" id="homeworkContent" name="homeworkContent" maxlength="50"></textarea>
						</div>
						
						<div class="form-group">
							<label for="desc-textarea">备注</label>
							<textarea class="form-control limited" id="desc-textarea" maxlength="50"></textarea>
						</div>
					</form>
					</div>
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm" data-dismiss="modal">
					<i class="ace-icon fa fa-times"></i>
					取消
				</button>

				<button class="btn btn-sm btn-primary" id="add-btn">
					<i class="ace-icon fa fa-check"></i>
					确定
				</button>
			</div>
		</div>
	</div>
</div>

<div id="modify-modal" class="modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="blue bigger">修改章节</h4>
			</div>

			<div class="modal-body">
				<div class="row">
					<div class="col-xs-12 col-sm-7">
						<form id="form-1">
						<div class="form-group">
							<input type="hidden" id="bid-hid" name="bid" value="${param.bid }"/>
							<input type="hidden" id="cid-hid" name="cid" />
							<label for="label-txt2"><font color="red">* </font>章节名称</label>
							<div>
								<input class="input-large" type="text" id="label-txt2"  name=""  value="" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="cnt-txt2">课次</label>
							<div>
								<input class="input-large" type="text" id="cnt-txt2" disabled="disabled"/>
							</div>
						</div>

						<div class="form-group">
							<label for="homeworkTitle2"><font color="red">* </font> 作业标题</label>
							<div>
								<input class="input-large" type="text" id="homeworkTitle2" name="homeworkTitle" value="" />
							</div>
						</div>
						
						<div class="form-group">
							<label for="homeworkContent2"><font color="red">* </font> 作业内容</label>
							<textarea class="form-control limited" id="homeworkContent2" name="homeworkContent" maxlength="50"></textarea>
						</div>
						
						<div class="form-group">
							<label for="desc-textarea2">备注</label>
							<textarea class="form-control limited" id="desc-textarea2" maxlength="50"></textarea>
						</div>
					</form>
					</div>
				</div>
			</div>

			<div class="modal-footer">
				<button class="btn btn-sm" data-dismiss="modal">
					<i class="ace-icon fa fa-times"></i>
					取消
				</button>

				<button class="btn btn-sm btn-primary" id="modify-btn">
					<i class="ace-icon fa fa-check"></i>
					确定
				</button>
			</div>
		</div>
	</div>
</div>


<script type="text/javascript">
var scripts = []
ace.load_ajax_scripts(scripts);

$(document).ready(function() {
	$('#add-chapter-a').click(function() {
		var lastTr = $('#chapter-table').find('tr:eq(1)');
		var cnt = parseInt(lastTr.find('.cnt-td').text());
		if(!cnt) {
			cnt = 0;
		}
		$('#add-model').find('#cnt-txt').val(cnt + 1);
	});
	
	$('#modify-btn').click(function() {
		var bid = $('#bid-hid').val();
		var cid = $('#cid-hid').val();
		var cnt = $('#cnt-txt2').val();
		var label = $('#label-txt2').val();
		var desc = $('#desc-textarea2').val();
		var homeworkTitle = $('#homeworkTitle2').val();
		var homeworkContent = $('#homeworkContent2').val();
		
		$.ajax({
			url: '/chapter/admin/update',
			type: 'post',
			data: {'bid':bid, 'cid': cid,'cnt': cnt, 'label': label, 'desc': desc,'homeworkTitle':homeworkTitle, 'homeworkContent': homeworkContent},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					$('#modify-modal').modal('hide');
					location.href = '/#/chapter/admin/list?bid=' + bid + '&t=' + new Date().getMilliseconds();
				} else {
					alert(result.meta.desc || result.meta.errorInfo);
				}
			}
		});
		
		return false;
	});
	
	$('#add-btn').click(function() {
		var bid = $('#bid-hid').val();
		var cnt = $('#cnt-txt').val();
		var label = $('#label-txt').val();
		var desc = $('#desc-textarea').val();
		var homeworkTitle = $('#homeworkTitle').val();
		var homeworkContent = $('#homeworkContent').val();
		
		$.ajax({
			url: '/chapter/admin/add',
			type: 'post',
			data: {'bid':bid, 'cnt': cnt, 'label': label, 'desc': desc,'homeworkTitle':homeworkTitle, 'homeworkContent': homeworkContent},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					$('#add-model').modal('hide');
					location.href = '/#/chapter/admin/list?bid=' + bid + '&t=' + new Date().getMilliseconds();
				} else {
					alert(result.meta.desc||result.meta.errorInfo);
				}
			}
		});
		
		return false;
	});
	
	$('.modify-a').click(function() {
		var bookId = $(this).attr('bid');
		var chapterId = $(this).attr('cid');
		var chapterTr = $('#tr-'+chapterId);
		
		var label = chapterTr.find('#span-label-'+chapterId).text();
		var cnt = chapterTr.find('#span-cnt-'+chapterId).text();
		var desc = chapterTr.find('#hid-desc-'+chapterId).val();
		var homeworkTitle = chapterTr.find('#hid-homeworkTitle-'+chapterId).val();
		var homeworkContent = chapterTr.find('#hid-homeworkContent-'+chapterId).val();
		
		$('#modify-modal').find('#book-hid').val(bookId);
		$('#modify-modal').find('#cid-hid').val(chapterId);
		
		$('#modify-modal').find('#label-txt2').val(label);
		$('#modify-modal').find('#cnt-txt2').val(cnt);
		
		$('#modify-modal').find('#desc-textarea2').val(desc);
		$('#modify-modal').find('#homeworkTitle2').val(homeworkTitle);
		$('#modify-modal').find('#homeworkContent2').val(homeworkContent);
	});
	
	
	$('.remove-chapter').click(function() {
		var result = confirm("确认删除章节吗？");
		if(!result) {
			return;
		}
		
		var bid = $('#bid-hid').val();
		var cid = $(this).attr('cid');
		$.ajax({
			url: '/chapter/admin/remove',
			type: 'post',
			data: {'bid':bid, 'cid': cid},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					location.href = '/#/chapter/admin/list?bid=' + bid + '&t=' + new Date().getMilliseconds();
				} else {
					alert(result.meta.desc||result.meta.errorInfo);
				}
			}
		});
		
		return false;
	});
});
</script>

