$(document).ready(function() {
	$('#query-btn').click(function() {
		var url = '#/course/admin/list'
		var params = $('#query-form').serialize();
		location.href = url + '?' + params;
	});
});