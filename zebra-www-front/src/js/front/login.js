$(document).ready(function(){
	$('#loginForm').on('submit', function (e) {
		var validate = 0;
		var formDataArray = $("form").serializeArray();
		var formData = {};
		$.each(formDataArray, function (index, item) {
			if (item.name === 'mobile') {
				if (!COMMON_CONFIG.isTelephone(item.value)) {
					validate ++;
					$('#username').addClass('error');
				}
				else {
					formData.mobile = item.value;
					$('#username').removeClass('error');
				}
			}
			if (item.name === 'password') {
				if (item.value === '') {
					validate ++;
					$('#password').addClass('error');
				}
				else {
					formData.password = item.value;
					$('#password').removeClass('error');
				}
			}
		});
		if (validate === 0) {
			COMMON_CONFIG.ajax('/login/mobile', 'post', formData, function (flag, data, meta) {
				if (flag) {
					window.localStorage.setItem('tel', formData.mobile);
					window.localStorage.setItem('z', data.z);
					window.localStorage.setItem('userInfo', JSON.stringify(data.user));
					window.localStorage.setItem('examLevel', data.user.level);
		        	location.href = $('#loginForm').attr('action');
		        }
		        else {
		        	$('.error-tip').html(meta.desc);
		        }
			})
		}
		
		return false;
	});
});