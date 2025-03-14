var WEBSITE = 'http://web.z.ziduan.com';
var questionList = [{
	id: 1,
	title: 'Look at the picture and choose the right answer.',
	img: '/src/img/admin/question/step1/1.png',
	list: [{
		name: 'a',
		value: 'Apple'
	},{
		name: 'b',
		value: 'Cat'
	}]
}, {
	id: 2,
	title: 'Look at the picture and answer the question. How many arms?',
	img: '/src/img/admin/question/step1/2.png',
	list: [{
		name: 'a',
		value: 'Three'
	},{
		name: 'b',
		value: 'Five'
	}]
}, {
	id: 3,
	title: 'Look at the picture and answer the question. I am from _________ ?',
	img: '/src/img/admin/question/step1/3.jpg',
	list: [{
		name: 'a',
		value: 'China'
	},{
		name: 'b',
		value: 'America'
	}]
}, {
	id: 4,
	title: 'What would you like ?(    )',
	img: '/src/img/admin/question/step1/4.png',
	list: [{
		name: 'a',
		value: 'I like a coffee.'
	},{
		name: 'b',
		value: 'I would like a cup of coffee.'
	}]
}, {
	id: 5,
	title: 'How do you feel today?(    )',
	img: '/src/img/admin/question/step1/5.png',
	list: [{
		name: 'a',
		value: 'I feel happy.'
	},{
		name: 'b',
		value: 'I feel sad. '
	}]
}, {
	id: 6,
	title: 'Look at he picture and choose the right word.',
	img: '/src/img/admin/question/step1/6.png',
	list: [{
		name: 'a',
		value: 'salad'
	},{
		name: 'b',
		value: 'soup'
	},{
		name: 'c',
		value: 'chicken'
	},{
		name: 'd',
		value: 'sandwich'
	}]
}, {
	id: 7,
	title: '-What are you doing ? -__________ ',
	img: '/src/img/admin/question/step1/7.png',
	list: [{
		name: 'a',
		value: 'I am doing my homework.'
	},{
		name: 'b',
		value: 'I  am  watching TV.'
	}]
}, {
	id: 8,
	title: 'Look at the picture and choose the right answer.What do you do in the morning ?(   )',
	img: '/src/img/admin/question/step1/8.png',
	list: [{
		name: 'a',
		value: 'I comb my hair.'
	},{
		name: 'b',
		value: 'I eat  breakfast.'
	}]
}, {
	id: 9,
	title: 'Look at the picture and choose the right answer.',
	img: '/src/img/admin/question/step1/9.png',
	list: [{
		name: 'a',
		value: 'Turn left.'
	},{
		name: 'b',
		value: 'Turn right.'
	}]
}, {
	id: 10,
	title: 'Look at the picture and answer the questions.How do you go to school ?  (  )',
	img: '/src/img/admin/question/step1/10.png',
	list: [{
		name: 'a',
		value: 'I go to school by bus.'
	},{
		name: 'b',
		value: 'I go to school by taxi.'
	}]
}];
var COMMON_CONFIG = {
	index: '/pages/front/index.html',
	genderMap: {
		0: '男',
		1: '女'
	},
	identifyCodeTime: 60,
	ajax: function (url, type, data, cb) {
		$.ajax({
			// url: url,
			url: WEBSITE + url,
			type: type,
			data: data,
			cache: false,
			success: function (data) {
				// data = JSON.parse(data);
		        cb(data.meta.code === 1, data.data, data.meta);
			}
		});
	},
	serializeObject: function(form) {
	    var o = {};
	    $.each(form.serializeArray(), function(index) {  
	        if (o[this['name']]) {  
	            o[this['name']] = o[this['name']] + "," + this['value'];  
	        } else {  
	            o[this['name']] = this['value'];  
	        }  
	    });  
	    return o;  
	},
	deserialize: function (form, obj){    
	    // 开始遍历     
	    for(var p in obj){        
	       // 方法    
	       $('[name=' + p + ']', form).val(obj[p]);         
	    }        
	},
	error: function (errMsg, w, h, hasBtn) {
		var area = [(w||400) + 'px', (h||200) + 'px'];
		var wHeight = (document.body.clientHeight - (h||200)) / 2;
		// layer.open({
		//     type: 1,
  		// 	title: false,
  		// 	closeBtn: 1,
  		// 	skin: 'dlg-class',
  		// 	shade: [0.5, '#000'],
		//     area: area,
		//     offset: wHeight + 'px',
		//     content: errMsg,
		//     btn: !hasBtn ? ['确定'] : false
		//   });
	},
	isTelephone: function (mobile) {
		return /^1[34578]\d{9}$/.test(mobile);
	},
	isNull: function (str) {
		return (str === '');
	},
	questionList: questionList
};



