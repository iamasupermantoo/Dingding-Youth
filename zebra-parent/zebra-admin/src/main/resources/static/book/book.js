$(document).ready(function() {
	$('#add-btn').click(function() {
		var totalCnt = $('#total-cnt-txt').val();
		var name = $('#name-txt').val();
		var desc = $('#desc-txtarea').val();
		
		$.ajax({
			url: '/book/admin/add',
			type: 'post',
			data: {'name':name, 'totalCnt': totalCnt, 'desc': desc},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					$('#modal-form').modal('hide');
					location.href = '/#/book/admin/list?t=' + new Date();
				} else {
					alert(result.desc||result.errorInfo);
				}
			}
		});
		
		return false;
	});
	
	$('#modify-btn').click(function() {
		var bookId = $('#book-hid').val();
		var totalCnt = $('#total-cnt-txt2').val();
		var name = $('#name-txt2').val();
		var desc = $('#desc-txtarea2').val();
		
		$.ajax({
			url: '/book/admin/modify',
			type: 'post',
			data: {'bid': bookId, 'name':name, 'totalCnt': totalCnt, 'desc': desc},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					$('#modify-modal').modal('hide');
					location.href = '/#/book/admin/list?t=' + new Date().getTime();
				} else {
					alert(result.desc||result.errorInfo);
				}
			}
		});
		
		return false;
	});
	
	$('.modify-a').click(function() {
		var bookId = $(this).attr('bid');
		
		var bookTr = $('#tr-'+bookId);
		
		var name = bookTr.find('#span-name-'+bookId).text();
		var totalcnt = bookTr.find('#span-totalcnt-'+bookId).text();
		var desc = bookTr.find('#span-desc-'+bookId).find('input').val();
		
		$('#modify-modal').find('#book-hid').val(bookId);
		$('#modify-modal').find('#name-txt2').val(name);
		$('#modify-modal').find('#total-cnt-txt2').val(totalcnt);
		$('#modify-modal').find('#desc-txtarea2').val(desc);
	});
	
	
	$('.remove-a').click(function() {
		var bookId = $(this).attr('bid');
		var result = confirm("确认删除教材吗？");
		if(!result) {
			return;
		}
		$.ajax({
			url: '/book/admin/remove',
			type: 'post',
			data: {'bid':bookId},
			success: function(result, status, xhr) {
				if(result.meta.code == 1) {
					alert('删除教材成功');
					location.href = '/#/book/admin/list';
				} else {
					alert(result.meta.desc || result.meta.errorInfo);
				}
			}
		});
	});
	
	
});
