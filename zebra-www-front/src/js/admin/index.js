$(function() {
	var z = window.localStorage.getItem('z');
	if (!z) {
		location.href = COMMON_CONFIG.index;
	}
	// 测评答案
	var testAnswer = [];
	var user = JSON.parse(window.localStorage.getItem('userInfo'));
	var tel = JSON.parse(window.localStorage.getItem('tel'));
	$('#userId').html(tel);
	// 登出
	if ($('#logout').length > 0) {
		$('#logout').on('click', function (e) {
			// COMMON_CONFIG.ajax('/mock/login/logout.json', 'get', {
			COMMON_CONFIG.ajax('/logout', 'post', {
				z: z
			}, function (flag, data) {
				if (flag) {
					window.localStorage.clear();
					location.href = COMMON_CONFIG.index;
				}
			});
		});
	}

	// 重置密码
	if ($('#resetPwdForm').length > 0) {
		$('#resetPwdForm').on('submit', function (e) {
			var me = this;
			var oldPwd = $('#oldPwd').val();
			var newPwd = $('#newPwd').val();
			var rePwd = $('#rePwd').val();
			if (COMMON_CONFIG.isNull(oldPwd)) {
				$('#oldPwd').addClass('error');
				$('#successTip').addClass('error-tip').html('旧密码不可为空！').css('visibility', 'visible');
				return false;
			}
			else if (COMMON_CONFIG.isNull(newPwd)) {
				$('#oldPwd').removeClass('error');
				$('#newPwd').addClass('error');
				$('#successTip').addClass('error-tip').html('新密码不可为空！').css('visibility', 'visible');
				return false;
			}
			else if (COMMON_CONFIG.isNull(rePwd)) {
				$('#oldPwd').removeClass('error');
				$('#newPwd').removeClass('error');
				$('#rePwd').addClass('error');
				$('#successTip').addClass('error-tip').html('确认密码不可为空！').css('visibility', 'visible');
				return false;
			}
			else if (newPwd !== rePwd) {
				$('#oldPwd').removeClass('error');
				$('#newPwd').removeClass('error');
				$('#rePwd').addClass('error');
				$('#successTip').addClass('error-tip').html('确认密码与密码不一致！').css('visibility', 'visible');
				return false;
			}
			$('#oldPwd').removeClass('error');
			$('#newPwd').removeClass('error');
			$('#rePwd').removeClass('error');
			// COMMON_CONFIG.ajax('/mock/login/getCode.json', 'get', {
			COMMON_CONFIG.ajax('/password/change', 'post', {
				oldpasswd: oldPwd,
				newpasswd: newPwd,
				confirm: rePwd,
				z: z
			}, function (flag, data, meta) {
				if (flag) {
					COMMON_CONFIG.error('更新成功!');
				}
				else {
					COMMON_CONFIG.error(meta.desc);
				}
			});
			return false;
		});
	}

	// 用户信息
	if ($('#saveUserInfo').length > 0) {
		COMMON_CONFIG.ajax('/mine/info', 'get', {
		    z: z
		}, function (flag, data, meta) {
			if (flag) {
				data = data.info;
				// data.gender = COMMON_CONFIG.genderMap[data.gender];
				$('#userImg').attr('src', manageImg(data.headImage.pattern, 86, 86));
				COMMON_CONFIG.deserialize('#userInfoForm', data);
				$('#saveUserInfo').on('click', function (e) {
					var $this = this;
					var state = $($this).attr('data-state');
					if (state !== 'save') {
						$('#successTip').css('visibility', 'hidden');
						$('#userInfoForm').find('input').removeAttr('disabled');
						$('#userInfoForm').find('select').removeAttr('disabled');
						$($this).attr('data-state', 'save').html('保存');
						return;
					}
					var formData = COMMON_CONFIG.serializeObject($('#userInfoForm'));
					formData.z = z;
					// for (var i in COMMON_CONFIG.genderMap) {
					// 	if (COMMON_CONFIG.genderMap[i] === formData.gender) {
					// 		formData.gender = i;
					// 	}
					// }
					formData.province = data.province;
					formData.region = data.region;
					COMMON_CONFIG.ajax('/mine/update', 'post', formData, function (formFlag, fData, fMeta) {
						if (formFlag) {
							$.extend(user, formData);
							window.localStorage.setItem('userInfo', JSON.stringify(user));
							$('#successTip').addClass('success-tip').removeClass('error-tip').html('用户信息更新成功').css('visibility', 'visible');
							$('#userInfoForm').find('input').attr('disabled', 'disabled');
							$('#userInfoForm').find('select').attr('disabled', 'disabled');
							$($this).attr('data-state', 'default').html('编辑');
						}
						else {
							$('#successTip').addClass('error-tip').removeClass('success-tip').html(meta.desc).css('visibility', 'visible');
						}
					});
				});
			}
			else {
				COMMON_CONFIG.error(meta.desc);
			}
		});
	}

	// 我的订单
	if ($('#orderList').length > 0) {
		// 渲染用户信息
		var userTpl = juicer('#userTpl', {
			username: user.name,
			portraitUrl: manageImg(user.headImage.pattern, 86, 86)
		});
		var learnStepTpl = juicer('#learnStepTpl', {
			finishedCnt: user.finishedCnt,
			courseCnt: user.courseCnt,
			duration: user.duration
		});
		$('#stepHolder').append(learnStepTpl);
		$('#portrait').append(userTpl);

		COMMON_CONFIG.ajax('/order/list', 'get', {
			z: z
		}, function (flag, data) {
			if (flag) {
				var order = data.orders.list;
				$.each(order, function (index, item) {
					item.image.pattern = manageImg(item.image.pattern, 102, 130);
				});
				var orderTpl = juicer('#orderListTpl', {data: order});
				$('#orderList').append(orderTpl);
			}
		});
		$('#orderList').delegate('.pay-trigger', 'click', function (e) {
			var id = $(e.target).attr('data-id');
			var payDlgTpl = juicer('#payTpl', {id: id});
			COMMON_CONFIG.error(payDlgTpl, 520, 280, true);
		});	
	}

	// 公开课
	if ($('#discoveryList').length > 0) {
		$('.nav').find('li').removeClass('active');
		$('#navDiscovery').addClass('active');
		COMMON_CONFIG.ajax('/course/meta/list', 'get', {
		}, function (flag, data, meta) {
			if (flag) {
				var list = data.courses.list;
				$.each(list, function (index, item) {
					item.image.pattern = manageImg(item.image.pattern, 236, 305);
				});
				var discoveryListTpl = juicer('#discoveryListTpl', {data: list});
				$('#discoveryList').append(discoveryListTpl);
				$('#discoveryList').delegate('.appointment-class', 'click', function (e) {
					var $target = $(e.target);
					var id = $target.attr('data-id');
					COMMON_CONFIG.ajax('/order/commit', 'post', {
						product: id
					}, function (courseFlag, courseData, courseMeta) {
						if (courseFlag) {
							var str = '<p style="font-size: 20px;margin: 20px;">约课成功！<p>'
							 		+ '<p>会有助管老师电话和您沟通约课细节</p>';
							COMMON_CONFIG.error(str); 
						}
						else {
							COMMON_CONFIG.error(courseMeta.desc);
						}
					});
				});
			}
			else {
				COMMON_CONFIG.error(meta.desc);
			}
		});
	}

	// 日历
	if ($('#indexHolder').length > 0) {
		// 渲染用户信息
		var userTpl = juicer('#userTpl', {
			username: user.name,
			portraitUrl: manageImg(user.headImage.pattern, 86, 86)
		});
		var learnStepTpl = juicer('#learnStepTpl', {
			finishedCnt: user.finishedCnt,
			courseCnt: user.courseCnt,
			duration: user.duration
		});
		$('#stepHolder').append(learnStepTpl);
		$('#portrait').append(userTpl);

		var month = moment().format('YYYY-MM');
		var date = moment().format('YYYY-MM-DD');
		var eventArray = [];
		var clndr1;
		refreshCalendar(month);
		freshListen(date);
	}

	function refreshCalendar(monthStr, year, month){
		COMMON_CONFIG.ajax('/calendar/points', 'get', {
			month: monthStr,
			z: z
		}, function (flag, data) {
			if (flag) {
				eventArray = [];
				$.each(data.days, function (index, item) {
					// 兼容小于10的补全'0'
					if (item < 10) {
						item = '0' + item;
					}
					var tmp = {
						date: monthStr + '-' + item,
	            		title: '上课'
					};
					eventArray.push(tmp);
				});
				if (!clndr1) {
					clndr1 = $('.calendar-holder').clndr({
				        events: eventArray,
				        daysOfTheWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
				        clickEvents: {
				            click: function (target) {
				            	var $el = $(target.element);
				            	$('.day').removeClass('active');
				            	$el.addClass('active');
				            	freshListen(moment(target.date).format('YYYY-MM-DD'));
				            },
				            today: function (e) {
				            	var m = e.format('M');
				            	var year = e.format('YYYY');
				            	$('.clndr-controls .month').html(year + ' - ' + m + '月');
				            },
				            nextMonth: function (e) {
				            	var m = e.format('M');
				            	var year = e.format('YYYY');
				            	
				            	refreshCalendar(year + '-' + m, year, m);
				            },
				            previousMonth: function (e) {
				            	var m = e.format('M');
				            	var year = e.format('YYYY');
				            	$('.clndr-controls .month').html(year + ' - ' + m + '月');
				            	refreshCalendar(year + '-' + m, year, m);
				            }
				        },
				        ready: function () {
				         	var m = moment().format('M');
				            var year = moment().format('YYYY');
				            $('.clndr-controls .month').html(year + ' - ' + m + '月');
				            $('.today').addClass('active');
					    },
				        multiDayEvents: {
				            singleDay: 'date',
				            endDate: 'endDate',
				            startDate: 'startDate'
				        },
				        showAdjacentMonths: true,
				        adjacentDaysChangeMonth: false
				    });
				}
				else {
					clndr1.setEvents(eventArray);
					$('.clndr-controls .month').html(year + ' - ' + month + '月');
				}
			}
		});
	}

	// 课程
	if ($('#courseList').length > 0) {
		// 渲染用户信息
		var userTpl = juicer('#userTpl', {
			username: user.name,
			portraitUrl: manageImg(user.headImage.pattern, 86, 86)
		});
		var learnStepTpl = juicer('#learnStepTpl', {
			finishedCnt: user.finishedCnt,
			courseCnt: user.courseCnt,
			duration: user.duration
		});
		$('#stepHolder').append(learnStepTpl);
		$('#portrait').append(userTpl);

		COMMON_CONFIG.ajax('/course/list', 'get', {
			z: z
		}, function (flag, data) {
			if (flag) {
				$.each(data.courses.list, function (index, les) {
					les.image.pattern = manageImg(les.image.pattern, 102, 130);
				});
			   	var couserTpl = juicer('#couserTpl', {data: data.courses.list});
				$('#courseList').empty().append(couserTpl);
			}
		});
		$('#courseList').delegate('.more', 'click', function (e) {
			var $target = $(e.target).parent('.more').length > 0
			              ? $(e.target).parent('.more')
			              : $(e.target);
			if (!$target.hasClass('launch')) {
				$target.prev('.course-list').find('li').removeClass('hidden');
				$target.addClass('launch').find('span').html('收起');
			}
			else {
				var lis = $target.prev('.course-list').find('li');
				$.each(lis, function (index, item) {
					if (index > 4) {
						$(item).addClass('hidden');
					}
				});
				$target.removeClass('launch').find('span').html('点击展开全部');
			}
		});
	}


	// 测评
	if ($('#testsHolder').length > 0) {
		var userTpl = juicer('#userTpl', {
			username: user.name,
			portraitUrl: manageImg(user.headImage.pattern, 86, 86)
		});
		$('#portrait').append(userTpl);
		var isTest = parseInt(window.localStorage.getItem('examLevel'));
		if (isTest) {
			hideTest(isTest);
			return;
		}
		var testList = COMMON_CONFIG.questionList;
		var testIndex = parseInt($('#testTmp').attr('data-test-index'), 10);
		// 开始测评
		$('.start-btn').on('click', function (e) {
			$('.start').hide();
			$('#testTmp').show();
			$('#nextBtn').show();
			renderTestById(testIndex, testList);
		});
		// 选择题目
		$('#testTmp').delegate('#chooseType li', 'click', function (e) {
			$('.choose').removeClass('choose');
			$(this).addClass('choose');
		});

		// 下一题
		$('#nextBtn').on('click', function (e) {
			var answer = $('.choose').find('a').attr('data-id');
			if ($('.choose').length === 0) {
				return;
			}
			testAnswer.push(answer.toUpperCase());
			testIndex++;
			$('#testTmp').attr('data-test-index', testIndex);
			if (testIndex > 9) {
				// 结束测评
				endTest(testAnswer);
			}
			renderTestById(testIndex, testList);
		});
	}


	function freshListen(date) {
		COMMON_CONFIG.ajax('/calendar/reminders', 'get', {
			day: date,
			z: z
		}, function (flag, data) {
			if (flag) {
				$.each(data.lessons, function (index, les) {
					les.courseImage.pattern = manageImg(les.courseImage.pattern, 102, 130);
				});
				var remindersTpl = juicer('#remindersTpl', {data: data.lessons});
				$('#myCouseList').empty().append(remindersTpl);
			}
		});
	}

	function manageImg(str, w, h) {
		str = str.replace(/{w}/, w);
		str = str.replace(/{h}/, h);
		return str;
	}
	
	/**
	 * 结束测评
	 * @param  {[type]} testAnswer [description]
	 */
	function endTest (testAnswer) {
		// TODO: 添加接口
		COMMON_CONFIG.ajax('/exam/answers', 'post', {
			answers: testAnswer.join(),
			z: z
		}, function (flag, data) {
			if (flag) {
				hideTest(data.level);
				window.localStorage.setItem('examLevel', data.level);
			}
		});
		// hideTest();
		// window.localStorage.setItem('isTest', true);
	}
	
	/**
	 * 隐藏测评题目
	 */
	function hideTest(level) {
		$('.start').hide();
		$('#testTmp').hide();
		$('#nextBtn').hide();
		$('#level').html('level' + level);
		$('.end').show();
	}
	/**
	 * 渲染测评题目
	 * @param  {[type]} id       [题目id]
	 * @param  {[type]} testList [题目内容]
	 */
	function renderTestById(id, testList) {
		var remindersTpl = juicer('#remindersTpl', {data: testList[id]});
		$('#testTmp').empty().append(remindersTpl);
	}




});