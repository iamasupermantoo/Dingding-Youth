
$(document).ready(function() {
	// 支持排序
	$( "#tbody-drag" ).sortable({
		placeholder: "ui-state-highlight"
	});
	$( "#tbody-drag" ).disableSelection();
	
	
	// tab切换
	$('.banner-a').click(function() {
		var href = $(this).attr('href');
		location.href = href;
		return false;
	});
	
	
	/*
	 * 手动下线/上线
	 * 
	 * banner的状态：
	 * Online(0, "在线"),
	 * Offline(1, "下线"),
	 * Deleted(2, "已删除"),
	 */
	$('.a_op').click(function() {
		var _this = $(this);
		var bannerId = _this.attr('banner-id');
		var status = _this.attr('banner-status');
		if(status == 0) {
			offline(bannerId);
		} else if(status == 1){
			online(bannerId);
		}
		return false;
		
	});
	
	// 删除，除非真的不想要了，否则请使用“下线功能”
	$('.a_del').click(function() {
		var _this = $(this);
		var bannerId = _this.attr('banner-id');
		
		var result = confirm("确认删掉吗？");
		if(!result) {
			return;
		}
		
		$.ajax({
			url: '/recommend/admin/delBanner',
			type: 'post',
			data: {'id': bannerId},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					alert('删除成功');
					location.reload();
				} else {
					alert(result.desc||result.errorInfo);
				}
			}
		});
	});
	
	
	// 保存当前的顺序
	$('#btn_save_order').click(function() {
		var ids = [];
		$('#table_banner tr:gt(0)').each(function(idx, ele) {
			var eleJq = $(ele);
			if(eleJq.attr('banner-status') == '0') {		// 0是online，只保存online的
				ids.push(eleJq.attr('id'));
			}
		});
		
		$.ajax({
			url: '/recommend/admin/saveOrder',
			type: 'post',
			data: {'ids[]': ids},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					alert('保存顺序成功');
					location.reload();
				} else {
					alert(result.desc||result.errorInfo);
				}
			}
		});
	});
	return false;
	
});

function online(bannerId) {
	var result = confirm("确认重新上线吗？");
	if(!result) {
		return;
	}
	
	$.ajax({
		url: '/recommend/admin/onlineBanner',
		type: 'post',
		data: {'id': bannerId},
		success: function(result, status, xhr) {
			if(result.meta.code == 1) {
				alert('重新上线成功');
				location.reload();
			} else {
				alert(result.desc||result.errorInfo);
			}
		}
	});
}


function offline(bannerId) {
	var result = confirm("确认下线吗？");
	if(!result) {
		return;
	}
	
	$.ajax({
		url: '/recommend/admin/offlineBanner',
		type: 'post',
		data: {'id': bannerId},
		success: function(result, status, xhr) {
			if(result.meta.code == 1) {
				alert('下线成功');
				location.reload();
			} else {
				alert(result.desc||result.errorInfo);
			}
		}
	});
}


