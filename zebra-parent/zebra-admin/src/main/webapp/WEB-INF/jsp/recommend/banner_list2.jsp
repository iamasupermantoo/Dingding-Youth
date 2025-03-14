<%@ page language="java" contentType="text/html; charset=UTF-8" session="false"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/inc/taglib.inc" %>
<title>首页焦点管理</title>
<link rel="stylesheet" type="text/css" href="/recommend/banner.css"/>
<script type="text/javascript" src="/recommend/banner.js"></script>
	<!-- 添加一个banner -->
	<h5>添加焦点图</h5>
	notes: 
	<ol>
		<li>时间格式：2017-10-11 22:30:21</li>
		<li>暂未支持自动上下线</li>
		<li>暂未支持排序</li>
	</ol><br />
	<form id="add_form" action="/recommend/admin/addBanner" method="post" enctype="multipart/form-data">
		gotopage类型：<select id="gotoPage" name="gotoPage">
			<option value="POST">帖子终端页</option>
			<option value="PROFILE">个人Profile页</option>
			<option value="URL">外部URL</option>
			<option value="FEEDBACK">用户反馈页</option>
			<option value="QUESTION">问题终端页</option>
			<option value="ANSWER">回答终端页</option>
			<option value="NONE" selected="selected">不跳转</option>
		</select><br />
		上线时间：<input type="text" id="start_time" name="startTime" value="${currTime }"/>&nbsp; - &nbsp;
		<input type="text" id="end_time" name="endTime"/><br />
		上传文件：<input type="file" id="image" name="image"/><br />
		
		<div>
			<div id="POST" class="param">
				<br />参数：<br />
				帖子id：<input id="dataId" name="dataId" type="text"  /><br />
			</div>
			<div id="PROFILE" class="param">
				<br />参数：<br />
				用户id：<input id="dataId" name="dataId" type="text"  /><br />
			</div>
			<div id="URL" class="param">
				<br />参数：<br />
				url: <input id="url" name="url" type="text"  /><br />
			</div>
			<div id="FEEDBACK" class="param"></div>
			<div id="QUESTION" class="param">
				<br />参数：<br />
				问题ID：<input id="qid" name="qid" type="text"  />
			</div>
			<div id="ANSWER" class="param">
				<br />参数：<br />
				问题ID：<input id="qid" name="qid" type="text"  /><br />
				回答ID：<input id="aid" name="aid" type="text"  /><br />
			</div>
			<div id="NONE" class="param"></div>
		</div><br />
		<input type="submit" value="点我添加" />
	</form><br />
	
	<hr />
	<br />
	<input id="btn_save_order" type="button" value="保存当前顺序"/>
	<!-- 列表 -->
	<table id="table_banner" border="1">
		<tr>
			<td>拖我</td>
			<td>id</td>
			<td>图片</td>
			<td width="150">类型</td>
			<td>data</td>
			<td width="50">状态</td>
			<td width="100">上线时间</td>
			<td width="100">下线时间</td>
			<td>操作</td>
		</tr>
		<c:forEach items="${ banners.list}" var="banner">
			<tr id="${banner.id }" banner-status="${banner.status }" class="${banner.status == 1 || banner.status == 2 ? 'offline' : ''}">
				<td><div class="rec_dragging"></div></td>
				<td>${banner.id }</td>
				<td><img alt="" src="${banner.image.getURL(130, 70) }"></td>
				<td>${henum:gotoPageType(banner.gotoPageType) }</td>
				<td>TODO</td>
				<td>${henum:bannerStatus(banner.status) }</td>
				<td>${hweb:dateTime(banner.startTime) }</td>
				<td>${hweb:dateTime(banner.endTime) }</td>
				<td>
					<a class="a_op" banner-id="${banner.id }" banner-status="${banner.status }" href="javascript:void(0);">
						${banner.status == 1 ? '上线':'下线'}</a>|
					<a class="a_del"  banner-id="${banner.id }" href="javascript:void(0);">删掉</a>
				</td>
			</tr>
		</c:forEach>
	</table>
<script type="text/javascript">
	var scripts = []
	ace.load_ajax_scripts(scripts);
</script>