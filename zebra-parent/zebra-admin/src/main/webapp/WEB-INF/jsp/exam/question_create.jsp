<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="zenum" uri="http://zebra.com/tags/zenum"%>
<%@ taglib prefix="zweb" uri="http://zebra.com/tags/zweb"%>
<title>新建问题</title>
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
		<a href="#/exam/admin/question/createUI" class="blue">加题干</a>
	</button>
	
	<button class="btn btn-white btn-default btn-round">
		<a href="#/exam/admin/question/createUI" class="blue">加选项</a>
	</button>
</p>


<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<form class="form-horizontal" id="form-1" role="form" action="/book/add" method="post">
		
			
		
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right" for="level-sel"><font color="red">*</font> 阅读层级:</label>
				
				<div class="col-sm-9">
					<select id="level-sel" name="level">
					   <option value="-1">-- 请选择 --</option>
					   <option value="0">学前阶段</option>
					   <option value="1">小学阶段</option>
					   <option value="2">中学阶段</option>
					   <option value="3">高中阶段</option>
					 </select>
				 </div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right" for="cover-image-file"><font color="red">*</font> 文件: </label>

				<div class="col-sm-3">
					<input type="file" id="cover-image-file" name="image" />
				</div>
			</div>
			
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
</script>