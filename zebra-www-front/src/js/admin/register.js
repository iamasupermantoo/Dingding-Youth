$(function () {
		// 注册 step1
	$('#identify').on('click', function (e) {
		var me = this;
		var mobile = $('#mobile').val();
		if (!COMMON_CONFIG.isTelephone(mobile)) {
			$('#mobile').addClass('error');
			return false;
		}
		$('#mobile').removeClass('error');
		if (!$(this).hasClass('disabled')) {
			COMMON_CONFIG.ajax('/register/getCode', 'post', {
			mobile: mobile}, function (flag, data, meta) {
				if (flag) {
					var seconds = COMMON_CONFIG.identifyCodeTime - 1;
					$('#identify').html(seconds + 's后重新获取');
					$(me).addClass('disabled');
					setInterval(function () {
						if (seconds > 0) {
							seconds --;
					  		$('#identify').html(seconds + 's后重新获取');
						}
						else {
							$(me).removeClass('disabled');
							$('#identify').html('获取验证码');
						}
						
					}, 1000);
					COMMON_CONFIG.error('验证码发送成功！');
				}
				else {
					COMMON_CONFIG.error(meta.desc);
				}
			});
		}
	});
	$('#identifyCodeFrom').on('submit', function (e) {
		var me = this;
		var mobile = $('#mobile').val();
		var code = $('#identifyCode').val();
		if (!COMMON_CONFIG.isTelephone(mobile)) {
			$('#mobile').addClass('error');
			return false;
		}
		else if (COMMON_CONFIG.isNull(code)) {
			$('#identifyCode').addClass('error');
			return false;
		}
		$('#mobile').removeClass('error');
		$('#identifyCode').removeClass('error');
		COMMON_CONFIG.ajax('/register/verifyCode', 'post', {
			mobile: mobile,
			code: code
		}, function (flag, data, meta) {
			if (flag) {
				location.href = $(me).attr('action');
			}
			else {
				$('.error-tip').html(meta.desc).css('visibility', 'visible');
			}
		});
		return false;
	});

	// 注册 step2
	$('#pwdForm').on('submit', function (e) {
		var me = this;
		var mobile = $('#mobile').html();
		var pwd = $('#pwd').val();
		var repwd = $('#repwd').val();
		if (COMMON_CONFIG.isNull(pwd)) {
			$('#pwd').addClass('error');
			return false;
		}
		else if (COMMON_CONFIG.isNull(repwd)) {
			$('#pwd').removeClass('error');
			$('#repwd').addClass('error');
			return false;
		}
		else if (pwd !== repwd) {
			$('#pwd').removeClass('error');
			$('#repwd').addClass('error');
			return false;
		}
		$('#pwd').removeClass('error');
		$('#repwd').removeClass('error');
		// COMMON_CONFIG.ajax('/mock/login/login.json', 'get', {
		COMMON_CONFIG.ajax('/register/mobile', 'post', {
			mobile: mobile,
			password: pwd,
			confirm: repwd
		}, function (flag, data) {
			if (flag) {
				window.localStorage.setItem('z', data.z);
				window.localStorage.setItem('userInfo', JSON.stringify(data.user));
				location.href = $(me).attr('action');
			}
			else {
				$('.error-tip').html(meta.desc).css('visibility', 'visible');
			}
		});
		return false;
	});
	// 注册 step3
	if ($('#loginSuccess').length > 0) {
		var defaultTime = parseInt($('#seconds').html(), 10);
		setInterval(function () {
		    if (defaultTime > 0) {
		    	defaultTime--;
		    	$('#seconds').html(defaultTime);
		    }
		    else {
		    	location.href = $('.goto-index').attr('href');
		    }
		}, 1000);
	}
});