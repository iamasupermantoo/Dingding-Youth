<%@ page language="java" contentType="text/html; charset=UTF-8" session="false"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/inc/taglib.inc" %>
<!DOCTYPE html>
<html>
<head>
<title>推荐用户</title>
<%@ include file="/WEB-INF/jsp/inc/header.inc" %>
<link rel="stylesheet" type="text/css" href="/user/user.css"/>
<script type="text/javascript" src="/user/user.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	// 添加用户
	$('#add_form').submit(function() {
		var result = confirm("确认添加用户吗？");
		if(!result) {
			return false;
		}
		var uid = $('#userId').val();
		
		$.ajax({
			url: '/recommend/admin/addUser',
			type: 'post',
			data: {'user': uid},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					alert('推荐用户成功');
					location.reload();
				} else {
					alert(result.desc||result.errorInfo);
				}
			}
		});
		return false;
	});
	
	// 删除用户
	$('.a_remove').click(function() {
		var result = confirm("确认移除用户吗？");
		if(!result) {
			return;
		}
		var uid = $(this).attr('uid');
		
		$.ajax({
			url: '/recommend/admin/removeUser',
			type: 'post',
			data: {'user': uid},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					alert('移除用户成功');
					location.reload();
				} else {
					alert(result.desc||result.errorInfo);
				}
			}
		});
		return false;
	});
});


</script>


</head>
<body>
<fieldset>
	
	<!-- 添加一个用户到首页 -->
	<h4>添加一个用户到首页</h4>
	<form id="add_form">
		用户id：<input id="userId" name="user" type="text"  /><br />
		<input type="submit" value="点我添加" />
	</form><br /><br />
	
	<!-- 列表 -->
	<table border="1">
		<tr>
			<td>用户id</td>
			<td>昵称</td>
			<td>头像</td>
			<td>类型</td>
			<td>性别</td>
			<!-- <td>是否在线</td> -->
			<td>家乡</td>
			<!-- <td>注册地</td> -->
			<td>注册时间</td>
			<!-- <td>手机号</td> -->
			<td>操作</td>
		</tr>
		<c:forEach items="${ users.list}" var="user">
			<tr id="${user.id }" class="${hweb:isDisabled(user.status) ? 'deleted':''}">
				<td>${user.id }</td>
				<td width="100">${user.name }</td>
				<td><img alt="" src="${user.headImage.getURL(70, 70) }"></td>
				<td width="50">${henum:userType(user.type) }</td>
				<td>${henum:gender(user.gender) }</td>
				<!-- <td>在线</td> -->
				<td width="100">${user.province }&nbsp;${user.region }</td>
				<!-- <td>新疆</td> -->
				<td>${hweb:dateTime(user.createTime) }</td>
				<%-- <td>${user.mobile }</td> --%>
				<td>
				<a class="a_remove" uid=${user.id } href="javascript:void(0);">取消推荐</a>
				
				</td>
			</tr>
		</c:forEach>
	</table>
</fieldset>
</body>
</html>