<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="zh-CN">
<head>
    <title>图书馆管理系统</title>
    <%@include file="../inc/head.inc"%>
</head>

<body class="no-skin">
<%@include file="../inc/navbar.inc"%>

<div class="main-container" id="main-container">
    <script type="text/javascript">
        try{ace.settings.check('main-container' , 'fixed')}catch(e){}
    </script>

    <%@include file="../inc/sidebar.inc"%>

    <div class="main-content">
        <%@include file="../inc/breadcrumbs.inc"%>
        <div class="page-content">
            <div class="page-content-area">

                权限管理



            </div><!-- /.page-content-area -->
        </div><!-- /.page-content -->
    </div><!-- /.main-content -->

    <%@include file="../inc/footer.inc"%>
    <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>
</div><!-- /.main-container -->
<%@include file="../inc/scripts.inc"%>
</body>
</html>
