<%@page import="com.dorado.framework.utils.HostInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" session="false"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta charset="utf-8">
<title>登录</title>

<meta name="description" content="User login page">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

<!-- bootstrap & fontawesome -->
<link rel="stylesheet" href="/assets/css/bootstrap.min.css">
<link rel="stylesheet" href="/assets/css/font-awesome.min.css">

<!-- text fonts -->
<link rel="stylesheet" href="/assets/css/ace-fonts.css">

<!-- ace styles -->
<link rel="stylesheet" href="/assets/css/ace.min.css">

<!--[if lte IE 9]>
	<link rel="stylesheet" href="/assets/css/ace-part2.min.css" />
<![endif]-->
<link rel="stylesheet" href="/assets/css/ace-rtl.min.css">

<!--[if lte IE 9]>
  <link rel="stylesheet" href="/assets/css/ace-ie.min.css" />
<![endif]-->
<link rel="stylesheet" href="/assets/css/ace.onpage-help.css">

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

<!--[if lt IE 9]>
<script src="/assets/js/html5shiv.js"></script>
<script src="/assets/js/respond.min.js"></script>
<![endif]-->

<script src="/assets/js/jquery.min.js"></script>
<script type="text/javascript">
$(function(){
	$(":submit").click(function(){
		var username = $("#username").val().trim();
		var password = $("#password").val().trim();
		var code = $("#code").val().trim();
		if(!username || !password){
			alert("用户名或密码不能为空！");
			return false;
		}
		if(!code) {
			alert("验证码不能为空！");
			return false;
		}
		
	});
	
	$('#picture').click(function() {
		$(this).attr('src', '/login/picture?t=' + new Date().getTime());
		return false;
	});
});
</script>
</head>

<body class="login-layout blur-login">
	<div class="main-container">
		<div class="main-content">
			<div class="row">
				<div class="col-sm-10 col-sm-offset-1">
					<div class="login-container">
						<div class="center">
							<h1>
								<span class="red">鼎鼎少年</span>
								<span class="white" id="id-text2">管理平台</span>
							</h1>
							<!-- <h4 class="light-blue" id="id-company-text">请输入您的账号</h4> -->
						</div>

						<div class="space-6"></div>

						<div class="position-relative">
							<div id="login-box" class="login-box visible widget-box no-border">
								<div class="widget-body">
									<div class="widget-main">
										<h4 class="header blue lighter bigger">
										<i class="ace-icon fa fa-coffee green"></i>用户登录</h4>
										<div class="space-6"></div>

										<form action="/login" method="post">
											<fieldset>
												<label class="block clearfix">
													<span class="block input-icon input-icon-right">
														<input type="text" class="form-control" autocomplete="off" name="username" id="username" placeholder="用户名">
														<i class="ace-icon fa fa-user"></i>
													</span>
												</label>

												<label class="block clearfix">
													<span class="block input-icon input-icon-right">
														<input type="password" class="form-control" autocomplete="off" name="password" id="password" placeholder="密码">
														<i class="ace-icon fa fa-lock"></i>
													</span>
												</label>
												
												<label class="block clearfix" style="width: 150px;">
													<span class="block input-icon input-icon-right">
														<input type="text" class="form-control" autocomplete="off" name="code" id="code" placeholder="验证码">
													</span>
													<img title="点击刷新" id="picture" src="/login/picture" 
														style="position:fixed; bottom: 78px; left: 200px" />
												</label>
												
												<span id="error-tips-span" style="position:fixed; bottom: 35px; left: 45px"><font color="red">${errorTips }</font></span>
												
												<div class="clearfix">
													<button type="submit" class="width-35 pull-right btn btn-sm btn-primary">
														<i class="ace-icon fa fa-key"></i>
														<span class="bigger-110">登录</span>
													</button>
												</div>

												<div class="space-4"></div>
											</fieldset>
										</form>
									</div><!-- /.widget-main -->
							</div><!-- /.widget-body -->
						</div><!-- /.login-box -->

						<div id="forgot-box" class="forgot-box widget-box no-border">
							<div class="widget-body">
								<div class="widget-main">
									<h4 class="header red lighter bigger">
										<i class="ace-icon fa fa-key"></i>
										Retrieve Password
									</h4>

									<div class="space-6"></div>
									<p>
										Enter your email and to receive instructions
									</p>

									<form>
										<fieldset>
											<label class="block clearfix">
												<span class="block input-icon input-icon-right">
													<input type="email" class="form-control" placeholder="Email">
													<i class="ace-icon fa fa-envelope"></i>
												</span>
											</label>

											<div class="clearfix">
												<button type="button" class="width-35 pull-right btn btn-sm btn-danger">
													<i class="ace-icon fa fa-lightbulb-o"></i>
													<span class="bigger-110">Send Me!</span>
												</button>
											</div>
										</fieldset>
									</form>
								</div><!-- /.widget-main -->

								<div class="toolbar center">
									<a href="#" data-target="#login-box" class="back-to-login-link">
										Back to login
										<i class="ace-icon fa fa-arrow-right"></i>
									</a>
								</div>
							</div><!-- /.widget-body -->
						</div><!-- /.forgot-box -->

						<div id="signup-box" class="signup-box widget-box no-border">
							<div class="widget-body">
								<div class="widget-main">
									<h4 class="header green lighter bigger">
										<i class="ace-icon fa fa-users blue"></i>
										New User Registration
									</h4>

									<div class="space-6"></div>
									<p> Enter your details to begin: </p>

									<form>
										<fieldset>
											<label class="block clearfix">
												<span class="block input-icon input-icon-right">
													<input type="email" class="form-control" placeholder="Email">
													<i class="ace-icon fa fa-envelope"></i>
												</span>
											</label>

											<label class="block clearfix">
												<span class="block input-icon input-icon-right">
													<input type="text" class="form-control" placeholder="Username">
													<i class="ace-icon fa fa-user"></i>
												</span>
											</label>

											<label class="block clearfix">
												<span class="block input-icon input-icon-right">
													<input type="password" class="form-control" placeholder="Password">
													<i class="ace-icon fa fa-lock"></i>
												</span>
											</label>

											<label class="block clearfix">
												<span class="block input-icon input-icon-right">
													<input type="password" class="form-control" placeholder="Repeat password">
													<i class="ace-icon fa fa-retweet"></i>
												</span>
											</label>

											<label class="block">
												<input type="checkbox" class="ace">
												<span class="lbl">
													I accept the
													<a href="#">User Agreement</a>
												</span>
											</label>

											<div class="space-24"></div>

											<div class="clearfix">
												<button type="reset" class="width-30 pull-left btn btn-sm">
													<i class="ace-icon fa fa-refresh"></i>
													<span class="bigger-110">Reset</span>
												</button>

												<button type="button" class="width-65 pull-right btn btn-sm btn-success">
													<span class="bigger-110">Register</span>

													<i class="ace-icon fa fa-arrow-right icon-on-right"></i>
												</button>
											</div>
										</fieldset>
									</form>
								</div>

								<div class="toolbar center">
									<a href="#" data-target="#login-box" class="back-to-login-link">
										<i class="ace-icon fa fa-arrow-left"></i>
										Back to login
									</a>
								</div>
							</div><!-- /.widget-body -->
						</div><!-- /.signup-box -->
					</div><!-- /.position-relative -->

					
				</div>
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.main-content -->
</div><!-- /.main-container -->

<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
	window.jQuery || document.write("<script src='/assets/js/jquery.min.js'>"+"<"+"/script>");
</script><script src="/assets/js/jquery.min.js"></script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='/assets/js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<![endif]-->
<script type="text/javascript">
	if('ontouchstart' in document.documentElement) document.write("<script src='/assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
</script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
	jQuery(function($) {
	 $(document).on('click', '.toolbar a[data-target]', function(e) {
		e.preventDefault();
		var target = $(this).data('target');
		$('.widget-box.visible').removeClass('visible');//hide others
		$(target).addClass('visible');//show target
	 });
	});
	
	
	
	//you don't need this, just used for changing background
	jQuery(function($) {
	 $('#btn-login-dark').on('click', function(e) {
		$('body').attr('class', 'login-layout');
		$('#id-text2').attr('class', 'white');
		$('#id-company-text').attr('class', 'blue');
		
		e.preventDefault();
	 });
	 $('#btn-login-light').on('click', function(e) {
		$('body').attr('class', 'login-layout light-login');
		$('#id-text2').attr('class', 'grey');
		$('#id-company-text').attr('class', 'blue');
		
		e.preventDefault();
	 });
	 $('#btn-login-blur').on('click', function(e) {
		$('body').attr('class', 'login-layout blur-login');
		$('#id-text2').attr('class', 'white');
		$('#id-company-text').attr('class', 'light-blue');
		
		e.preventDefault();
	 });
	 
	});
</script>
	

</body></html>