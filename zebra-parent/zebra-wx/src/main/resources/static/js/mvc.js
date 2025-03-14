"use strict";

var _get = function get(object, property, receiver) { if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { return get(parent, property, receiver); } } else if ("value" in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function e(t, n, r) {
	function s(o, u) {
		if (!n[o]) {
			if (!t[o]) {
				var a = typeof require == "function" && require;if (!u && a) return a(o, !0);if (i) return i(o, !0);throw new Error("Cannot find module '" + o + "'");
			}var f = n[o] = { exports: {} };t[o][0].call(f.exports, function (e) {
				var n = t[o][1][e];return s(n ? n : e);
			}, f, f.exports, e, t, n, r);
		}return n[o].exports;
	}var i = typeof require == "function" && require;for (var o = 0; o < r.length; o++) {
		s(r[o]);
	}return s;
})({ 1: [function (require, module, exports) {
		var UrlBase = "",
		    UUID = 1;
		var Loading = require("./loading.js");

		var MVC = function () {
			function MVC(target) {
				_classCallCheck(this, MVC);

				this.$id = "MVC_" + new Date().getTime() + "_" + ++UUID;
				this.$target = $(target);
				this.$path = this.$target.attr("action");
				this.$url = /^http/.test(this.$path) ? this.$path : UrlBase + this.$path;
				this.$method = (this.$target.attr("method") || "GET").toUpperCase();
				this.$template = $.templates(this.$target[0]);
				this.$data = this.$target.attr("data");
				this.$manual = this.$target.attr("manual");
				if (this.$data) {
					try {
						this.$data = this.$data.replace(/'/g, "\"");
						this.$data = JSON.parse(this.$data);
					} catch (e) {
						this.$data = {};
						console.error(e);
					}
				} else {
					this.$data = {};
				}
				this.$result = null;
				this.$handlers = {};
			}

			_createClass(MVC, [{
				key: "manually",
				value: function manually() {
					return this.$manual;
				}
			}, {
				key: "request",
				value: function request(data) {
					var _this = this;

					var $method = $.get;
					if (this.$method == "POST") {
						$method = $.post;
					}
					data = $.extend($.extend({}, this.$data), data);
					data.t = new Date().getTime();
					this.$data = data;
					clearTimeout(this.$loadingtimer);
					Loading.hide();
					this.$loadingtimer = setTimeout(function () {
						Loading.show("正在加载数据...");
					}, 500);
					$method(this.$url, data, function (response) {
						clearTimeout(_this.$loadingtimer);
						Loading.hide();
						if (response.meta.code == 1) {
							_this.__request_done(response);
						} else {
							if (response.meta.desc) {
								alert(response.meta.desc);
							}
						}
					});
				}
			}, {
				key: "__request_done",
				value: function __request_done(response) {
					if (this.$handlers.ondata) {
						this.$handlers.ondata(response);
					}
					this.$result = response;
					this.refresh();
				}
			}, {
				key: "refresh",
				value: function refresh(data) {
					this.$result = data || this.$result;
					if (!this.$result) return;
					var html = $.trim(this.$template.render(this.$result));
					var dom = $("." + this.$id);
					if (!html && dom.length != 0) {
						dom.remove();
					}
					var $dom = $(html).addClass(this.$id);
					if (dom.length == 0) {
						this.$target.after($dom);
					} else {
						dom.replaceWith($dom);
					}
					if (this.$handlers.update) {
						this.$handlers.update(this.$result);
					}
				}
			}, {
				key: "replace",
				value: function replace(target, filter) {
					var $key = $(target).attr("binding");
					var $data = filter(this.$result, $key);
					var $html = $.trim(this.$template.render($data));
					$(target).replaceWith($html);
				}
			}, {
				key: "update",
				value: function update(handler) {
					this.$handlers['update'] = handler;
					return this;
				}
			}, {
				key: "data",
				value: function data(handler) {
					this.$handlers['ondata'] = handler;
					return this;
				}
			}]);

			return MVC;
		}();

		var LoadMore = function (_MVC) {
			_inherits(LoadMore, _MVC);

			// @param url http://xxx.com/?curpage={{page}}
			function LoadMore(target) {
				_classCallCheck(this, LoadMore);

				var _this2 = _possibleConstructorReturn(this, (LoadMore.__proto__ || Object.getPrototypeOf(LoadMore)).call(this, target));

				_this2.$holder = _this2.$target.parent();
				_this2.$datakey = _this2.$target.attr("datakey") || "list";
				_this2.$page = null;
				_this2.$ended = false;
				_this2.$loading = false;
				_this2.$tmpUrl = _this2.$url;
				_this2.__bind();
				return _this2;
			}

			_createClass(LoadMore, [{
				key: "__bind",
				value: function __bind() {
					var _this3 = this;

					var $holder = this.$target.parents(".scroll-holder"),
					    $maxH = void 0;
					if ($holder.length == 0) {
						$holder = $(window);
						$maxH = function $maxH() {
							return $("body").height();
						};
					} else {
						var _maxH = $holder.prop("scrollHeight");
						$maxH = function $maxH() {
							return _maxH;
						};
					}
					var $viewport = $holder.height();
					$holder.on("scroll", function (event) {
						if (_this3.$ended) return;
						var top = $holder.scrollTop();
						var max = $maxH() - $viewport;
						if (max - top < 20) {
							_this3.request();
						}
					});
				}
			}, {
				key: "__url",
				value: function __url() {
					if (this.$page) {
						return this.$tmpUrl.replace("{{cursor}}", "cursor=" + this.$page + "&");
					} else {
						return this.$tmpUrl.replace("{{cursor}}", "");
					}
				}
			}, {
				key: "__request_done",
				value: function __request_done(response) {
					this.$loading = false;
					if (!response.data[this.$datakey].hasNext) {
						this.$ended = true;
					} else {
						this.$page = response.data[this.$datakey].nextCursor;
					}
					_get(LoadMore.prototype.__proto__ || Object.getPrototypeOf(LoadMore.prototype), "__request_done", this).call(this, response);
				}
			}, {
				key: "reload",
				value: function reload() {
					this.$ended = false;
					this.$page = null;
					$("." + this.$id).remove();
					this.request();
				}
			}, {
				key: "request",
				value: function request(data) {
					if (this.$ended || this.$loading) return;
					this.$url = this.__url();
					this.$loading = true;
					_get(LoadMore.prototype.__proto__ || Object.getPrototypeOf(LoadMore.prototype), "request", this).call(this, data);
				}
			}, {
				key: "refresh",
				value: function refresh(data) {
					this.$result = data || this.$result;
					if (!this.$result) return;
					var html = $.trim(this.$template.render(this.$result));
					$(html).addClass(this.$id).appendTo(this.$holder);
					if (this.$handlers.update) {
						this.$handlers.update(this.$result);
					}
				}
			}]);

			return LoadMore;
		}(MVC);

		$.fn.mvc = function () {
			var instance = $(this).data("MVC_INSTANCE");
			if (instance) {
				return instance;
			} else {
				if ($(this).attr("loadmore")) {
					instance = new LoadMore(this);
				} else {
					instance = new MVC(this);
				}
				$(this).data("MVC_INSTANCE", instance);
				return instance;
			}
		};

		$(function () {
			$("script[type='text/x-jsrender']").each(function (idx, target) {
				var mvc = $(target).mvc();
				if (!mvc.manually()) {
					mvc.request();
				}
			});
		});
		var Output = {
			config: function config(url) {
				if (location.host.indexOf("udan.cn") == -1) {
					UrlBase = url || UrlBase;
				}
				return Output;
			},
			net: {
				get: function get(url, data, handler, error) {
					$.get(UrlBase + url, data, handler).fail(error);
				},
				post: function post(url, data, handler, error) {
					$.post(UrlBase + url, data, handler).fail(error);
				}
			}
		};
		module.exports = Output;
	}, { "./loading.js": 2 }], 2: [function (require, module, exports) {
		var Loading = function () {
			function Loading() {
				_classCallCheck(this, Loading);

				this.$dom = $(this.__template());
				var holder = $(".wrapper");
				if (holder.length == 0) {
					holder = $("body");
				}
				this.$dom.appendTo(holder);
				this.$text = $(".txt", this.$dom);
			}

			_createClass(Loading, [{
				key: "__template",
				value: function __template() {
					return $.trim("\n\t\t\t<div class=\"mask loading\" style=\"display:none;\">\n\t\t\t\t<span class=\"ld ld-spinner ld-spin\"></span>\n\t\t\t\t<div class=\"txt\"></div>\n\t\t\t</div>\n\t\t");
				}
			}, {
				key: "show",
				value: function show(text) {
					if (text) {
						this.$text.html(text).show();
					} else {
						this.$text.hide();
					}
					this.$dom.show();
				}
			}, {
				key: "hide",
				value: function hide() {
					this.$dom.hide();
				}
			}]);

			return Loading;
		}();

		module.exports = new Loading();
	}, {}] }, {}, [1]);
//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIm12Yy5qcyJdLCJuYW1lcyI6WyJlIiwidCIsIm4iLCJyIiwicyIsIm8iLCJ1IiwiYSIsInJlcXVpcmUiLCJpIiwiRXJyb3IiLCJmIiwiZXhwb3J0cyIsImNhbGwiLCJsZW5ndGgiLCJtb2R1bGUiLCJVcmxCYXNlIiwiVVVJRCIsIkxvYWRpbmciLCJNVkMiLCJ0YXJnZXQiLCIkaWQiLCJEYXRlIiwiZ2V0VGltZSIsIiR0YXJnZXQiLCIkIiwiJHBhdGgiLCJhdHRyIiwiJHVybCIsInRlc3QiLCIkbWV0aG9kIiwidG9VcHBlckNhc2UiLCIkdGVtcGxhdGUiLCJ0ZW1wbGF0ZXMiLCIkZGF0YSIsIiRtYW51YWwiLCJyZXBsYWNlIiwiSlNPTiIsInBhcnNlIiwiY29uc29sZSIsImVycm9yIiwiJHJlc3VsdCIsIiRoYW5kbGVycyIsImRhdGEiLCJnZXQiLCJwb3N0IiwiZXh0ZW5kIiwiY2xlYXJUaW1lb3V0IiwiJGxvYWRpbmd0aW1lciIsImhpZGUiLCJzZXRUaW1lb3V0Iiwic2hvdyIsInJlc3BvbnNlIiwibWV0YSIsImNvZGUiLCJfX3JlcXVlc3RfZG9uZSIsImRlc2MiLCJhbGVydCIsIm9uZGF0YSIsInJlZnJlc2giLCJodG1sIiwidHJpbSIsInJlbmRlciIsImRvbSIsInJlbW92ZSIsIiRkb20iLCJhZGRDbGFzcyIsImFmdGVyIiwicmVwbGFjZVdpdGgiLCJ1cGRhdGUiLCJmaWx0ZXIiLCIka2V5IiwiJGh0bWwiLCJoYW5kbGVyIiwiTG9hZE1vcmUiLCIkaG9sZGVyIiwicGFyZW50IiwiJGRhdGFrZXkiLCIkcGFnZSIsIiRlbmRlZCIsIiRsb2FkaW5nIiwiJHRtcFVybCIsIl9fYmluZCIsInBhcmVudHMiLCIkbWF4SCIsIndpbmRvdyIsImhlaWdodCIsIl9tYXhIIiwicHJvcCIsIiR2aWV3cG9ydCIsIm9uIiwiZXZlbnQiLCJ0b3AiLCJzY3JvbGxUb3AiLCJtYXgiLCJyZXF1ZXN0IiwiaGFzTmV4dCIsIm5leHRDdXJzb3IiLCJfX3VybCIsImFwcGVuZFRvIiwiZm4iLCJtdmMiLCJpbnN0YW5jZSIsImVhY2giLCJpZHgiLCJtYW51YWxseSIsIk91dHB1dCIsImNvbmZpZyIsInVybCIsImxvY2F0aW9uIiwiaG9zdCIsImluZGV4T2YiLCJuZXQiLCJmYWlsIiwiX190ZW1wbGF0ZSIsImhvbGRlciIsIiR0ZXh0IiwidGV4dCJdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7O0FBQUEsQ0FBQyxTQUFTQSxDQUFULENBQVdDLENBQVgsRUFBYUMsQ0FBYixFQUFlQyxDQUFmLEVBQWlCO0FBQUMsVUFBU0MsQ0FBVCxDQUFXQyxDQUFYLEVBQWFDLENBQWIsRUFBZTtBQUFDLE1BQUcsQ0FBQ0osRUFBRUcsQ0FBRixDQUFKLEVBQVM7QUFBQyxPQUFHLENBQUNKLEVBQUVJLENBQUYsQ0FBSixFQUFTO0FBQUMsUUFBSUUsSUFBRSxPQUFPQyxPQUFQLElBQWdCLFVBQWhCLElBQTRCQSxPQUFsQyxDQUEwQyxJQUFHLENBQUNGLENBQUQsSUFBSUMsQ0FBUCxFQUFTLE9BQU9BLEVBQUVGLENBQUYsRUFBSSxDQUFDLENBQUwsQ0FBUCxDQUFlLElBQUdJLENBQUgsRUFBSyxPQUFPQSxFQUFFSixDQUFGLEVBQUksQ0FBQyxDQUFMLENBQVAsQ0FBZSxNQUFNLElBQUlLLEtBQUosQ0FBVSx5QkFBdUJMLENBQXZCLEdBQXlCLEdBQW5DLENBQU47QUFBOEMsUUFBSU0sSUFBRVQsRUFBRUcsQ0FBRixJQUFLLEVBQUNPLFNBQVEsRUFBVCxFQUFYLENBQXdCWCxFQUFFSSxDQUFGLEVBQUssQ0FBTCxFQUFRUSxJQUFSLENBQWFGLEVBQUVDLE9BQWYsRUFBdUIsVUFBU1osQ0FBVCxFQUFXO0FBQUMsUUFBSUUsSUFBRUQsRUFBRUksQ0FBRixFQUFLLENBQUwsRUFBUUwsQ0FBUixDQUFOLENBQWlCLE9BQU9JLEVBQUVGLElBQUVBLENBQUYsR0FBSUYsQ0FBTixDQUFQO0FBQWdCLElBQXBFLEVBQXFFVyxDQUFyRSxFQUF1RUEsRUFBRUMsT0FBekUsRUFBaUZaLENBQWpGLEVBQW1GQyxDQUFuRixFQUFxRkMs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LENBQWI7QUFDQTs7QUFWdUQ7QUFBQTtBQUFBLGlDQVkzQztBQUNaLFlBQU94QyxFQUFFb0MsSUFBRiw4S0FBUDtBQU1BO0FBbkJ1RDtBQUFBO0FBQUEseUJBcUJuRHdELElBckJtRCxFQXFCN0M7QUFDVixTQUFJQSxJQUFKLEVBQVU7QUFDVCxXQUFLRCxLQUFMLENBQVd4RCxJQUFYLENBQWdCeUQsSUFBaEIsRUFBc0JsRSxJQUF0QjtBQUNBLE1BRkQsTUFFTztBQUNOLFdBQUtpRSxLQUFMLENBQVduRSxJQUFYO0FBQ0E7QUFDRCxVQUFLZ0IsSUFBTCxDQUFVZCxJQUFWO0FBQ0E7QUE1QnVEO0FBQUE7QUFBQSwyQkE4QmpEO0FBQ04sVUFBS2MsSUFBTCxDQUFVaEIsSUFBVjtBQUNBO0FBaEN1RDs7QUFBQTtBQUFBOztBQW1DekRsQyxTQUFPSCxPQUFQLEdBQWlCLElBQUlNLE9BQUosRUFBakI7QUFDQyxFQXBDdUIsRUFvQ3RCLEVBcENzQixDQWhPaVksRUFBelosRUFvUU8sRUFwUVAsRUFvUVUsQ0FBQyxDQUFELENBcFFWIiwiZmlsZSI6Im12Yy5qcyIsInNvdXJjZXNDb250ZW50IjpbIihmdW5jdGlvbiBlKHQsbixyKXtmdW5jdGlvbiBzKG8sdSl7aWYoIW5bb10pe2lmKCF0W29dKXt2YXIgYT10eXBlb2YgcmVxdWlyZT09XCJmdW5jdGlvblwiJiZyZXF1aXJlO2lmKCF1JiZhKXJldHVybiBhKG8sITApO2lmKGkpcmV0dXJuIGkobywhMCk7dGhyb3cgbmV3IEVycm9yKFwiQ2Fubm90IGZpbmQgbW9kdWxlICdcIitvK1wiJ1wiKX12YXIgZj1uW29dPXtleHBvcnRzOnt9fTt0W29dWzBdLmNhbGwoZi5leHBvcnRzLGZ1bmN0aW9uKGUpe3ZhciBuPXRbb11bMV1bZV07cmV0dXJuIHMobj9uOmUpfSxmLGYuZXhwb3J0cyxlLHQsbixyKX1yZXR1cm4gbltvXS5leHBvcnRzfXZhciBpPXR5cGVvZiByZXF1aXJlPT1cImZ1bmN0aW9uXCImJnJlcXVpcmU7Zm9yKHZhciBvPTA7bzxyLmxlbmd0aDtvKyspcyhyW29dKTtyZXR1cm4gc30pKHsxOltmdW5jdGlvbihyZXF1aXJlLG1vZHVsZSxleHBvcnRzKXtcbmxldCBVcmxCYXNlIFx0PSBcIlwiLCBVVUlEID0gMTtcbmxldCBMb2FkaW5nICAgICA9IHJlcXVpcmUoXCIuL2xvYWRpbmcuanNcIik7XG5jbGFzcyBNVkMge1xuXHRjb25zdHJ1Y3Rvcih0YXJnZXQpIHtcblx0XHR0aGlzLiRpZCAgICAgXHQ9IGBNVkNfJHtuZXcgRGF0ZSgpLmdldFRpbWUoKX1fJHsrK1VVSUR9YDtcblx0XHR0aGlzLiR0YXJnZXQgXHQ9ICQodGFyZ2V0KTtcblx0XHR0aGlzLiRwYXRoICAgXHQ9IHRoaXMuJHRhcmdldC5hdHRyKFwiYWN0aW9uXCIpO1xuXHRcdHRoaXMuJHVybCAgICAgICA9IC9eaHR0cC8udGVzdCh0aGlzLiRwYXRoKT90aGlzLiRwYXRoOihVcmxCYXNlK3RoaXMuJHBhdGgpO1xuXHRcdHRoaXMuJG1ldGhvZCBcdD0gKHRoaXMuJHRhcmdldC5hdHRyKFwibWV0aG9kXCIpIHx8IFwiR0VUXCIpLnRvVXBwZXJDYXNlKCk7XG5cdFx0dGhpcy4kdGVtcGxhdGUgID0gJC50ZW1wbGF0ZXModGhpcy4kdGFyZ2V0WzBdKTtcblx0XHR0aGlzLiRkYXRhICAgXHQ9IHRoaXMuJHRhcmdldC5hdHRyKFwiZGF0YVwiKTtcblx0XHR0aGlzLiRtYW51YWwgICAgPSB0aGlzLiR0YXJnZXQuYXR0cihcIm1hbnVhbFwiKTtcblx0XHRpZiAodGhpcy4kZGF0YSkge1xuXHRcdFx0dHJ5IHtcblx0XHRcdFx0dGhpcy4kZGF0YSA9IHRoaXMuJGRhdGEucmVwbGFjZSgvJy9nLFwiXFxcIlwiKTtcblx0XHRcdFx0dGhpcy4kZGF0YSA9IEpTT04ucGFyc2UodGhpcy4kZGF0YSk7XG5cdFx0XHR9IGNhdGNoKGUpIHtcblx0XHRcdFx0dGhpcy4kZGF0YSA9IHt9O1xuXHRcdFx0XHRjb25zb2xlLmVycm9yKGUpO1xuXHRcdFx0fVxuXHRcdH0gZWxzZSB7XG5cdFx0XHR0aGlzLiRkYXRhICA9IHt9O1xuXHRcdH1cblx0XHR0aGlzLiRyZXN1bHQgICAgPSBudWxsO1xuXHRcdHRoaXMuJGhhbmRsZXJzICA9IHt9O1xuXHR9XG5cblx0bWFudWFsbHkoKSB7XG5cdFx0cmV0dXJuIHRoaXMuJG1hbnVhbDtcblx0fVxuXG5cdHJlcXVlc3QoZGF0YSkge1xuXHRcdGxldCAkbWV0aG9kID0gJC5nZXQ7XG5cdFx0aWYgKHRoaXMuJG1ldGhvZCA9PSBcIlBPU1RcIikge1xuXHRcdFx0JG1ldGhvZCA9ICQucG9zdDtcblx0XHR9XG5cdFx0ZGF0YSA9ICQuZXh0ZW5kKCQuZXh0ZW5kKHt9LCB0aGlzLiRkYXRhKSwgZGF0YSk7XG5cdFx0ZGF0YS50ID0gbmV3IERhdGUoKS5nZXRUaW1lKCk7XG5cdFx0dGhpcy4kZGF0YSA9IGRhdGE7XG5cdFx0Y2xlYXJUaW1lb3V0KHRoaXMuJGxvYWRpbmd0aW1lcik7XG5cdFx0TG9hZGluZy5oaWRlKCk7XG5cdFx0dGhpcy4kbG9hZGluZ3RpbWVyID0gc2V0VGltZW91dCgoKT0+e1xuXHRcdFx0TG9hZGluZy5zaG93KFwi5q2j5Zyo5Yqg6L295pWw5o2uLi4uXCIpO1xuXHRcdH0sNTAwKTtcblx0XHQkbWV0aG9kKHRoaXMuJHVybCwgZGF0YSwgKHJlc3BvbnNlKT0+e1xuXHRcdFx0Y2xlYXJUaW1lb3V0KHRoaXMuJGxvYWRpbmd0aW1lcik7XG5cdFx0XHRMb2FkaW5nLmhpZGUoKTtcblx0XHRcdGlmIChyZXNwb25zZS5tZXRhLmNvZGUgPT0gMSkge1xuXHRcdFx0XHR0aGlzLl9fcmVxdWVzdF9kb25lKHJlc3BvbnNlKTtcblx0XHRcdH0gZWxzZSB7XG5cdFx0XHRcdGlmIChyZXNwb25zZS5tZXRhLmRlc2MpIHtcblx0XHRcdFx0XHRhbGVydChyZXNwb25zZS5tZXRhLmRlc2MpO1xuXHRcdFx0XHR9XG5cdFx0XHR9XG5cdFx0fSk7XG5cdH1cblxuXHRfX3JlcXVlc3RfZG9uZShyZXNwb25zZSkge1xuXHRcdGlmICh0aGlzLiRoYW5kbGVycy5vbmRhdGEpIHtcblx0XHRcdHRoaXMuJGhhbmRsZXJzLm9uZGF0YShyZXNwb25zZSk7XG5cdFx0fVxuXHRcdHRoaXMuJHJlc3VsdCA9IHJlc3BvbnNlO1xuXHRcdHRoaXMucmVmcmVzaCgpO1xuXHR9XG5cblx0cmVmcmVzaChkYXRhKSB7XG5cdFx0dGhpcy4kcmVzdWx0ID0gZGF0YSB8fCB0aGlzLiRyZXN1bHQ7XG5cdFx0aWYgKCF0aGlzLiRyZXN1bHQpIHJldHVybjtcblx0XHRsZXQgaHRtbCA9ICQudHJpbSh0aGlzLiR0ZW1wbGF0ZS5yZW5kZXIodGhpcy4kcmVzdWx0KSk7XG5cdFx0bGV0IGRvbSAgPSAkKGAuJHt0aGlzLiRpZH1gKTtcblx0XHRpZiAoIWh0bWwgJiYgZG9tLmxlbmd0aCAhPSAwKSB7XG5cdFx0XHRkb20ucmVtb3ZlKCk7XG5cdFx0fVxuXHRcdGxldCAkZG9tID0gJChodG1sKS5hZGRDbGFzcyh0aGlzLiRpZCk7XG5cdFx0aWYgKGRvbS5sZW5ndGggPT0gMCkge1xuXHRcdFx0dGhpcy4kdGFyZ2V0LmFmdGVyKCRkb20pO1xuXHRcdH0gZWxzZSB7XG5cdFx0XHRkb20ucmVwbGFjZVdpdGgoJGRvbSk7XG5cdFx0fVxuXHRcdGlmICh0aGlzLiRoYW5kbGVycy51cGRhdGUpIHtcblx0XHRcdHRoaXMuJGhhbmRsZXJzLnVwZGF0ZSh0aGlzLiRyZXN1bHQpO1xuXHRcdH1cblx0fVxuXG5cdHJlcGxhY2UodGFyZ2V0LCBmaWx0ZXIpIHtcblx0XHRsZXQgJGtleSAgICA9ICQodGFyZ2V0KS5hdHRyKFwiYmluZGluZ1wiKTtcblx0XHRsZXQgJGRhdGEgXHQ9IGZpbHRlcih0aGlzLiRyZXN1bHQsICRrZXkpO1xuXHRcdGxldCAkaHRtbCAgID0gJC50cmltKHRoaXMuJHRlbXBsYXRlLnJlbmRlcigkZGF0YSkpO1xuXHRcdCQodGFyZ2V0KS5yZXBsYWNlV2l0aCgkaHRtbCk7XG5cdH1cblxuXG5cdHVwZGF0ZSAoaGFuZGxlcikge1xuXHRcdHRoaXMuJGhhbmRsZXJzWyd1cGRhdGUnXSA9IGhhbmRsZXI7XG5cdFx0cmV0dXJuIHRoaXM7XG5cdH1cblxuXHRkYXRhIChoYW5kbGVyKSB7XG5cdFx0dGhpcy4kaGFuZGxlcnNbJ29uZGF0YSddID0gaGFuZGxlcjtcblx0XHRyZXR1cm4gdGhpcztcblx0fVxufVxuXG5jbGFzcyBMb2FkTW9yZSBleHRlbmRzIE1WQyB7XG5cdC8vIEBwYXJhbSB1cmwgaHR0cDovL3h4eC5jb20vP2N1cnBhZ2U9e3twYWdlfX1cblx0Y29uc3RydWN0b3IodGFyZ2V0KSB7XG5cdFx0c3VwZXIodGFyZ2V0KTtcblx0XHR0aGlzLiRob2xkZXIgXHQ9IHRoaXMuJHRhcmdldC5wYXJlbnQoKTtcblx0XHR0aGlzLiRkYXRha2V5ICAgPSB0aGlzLiR0YXJnZXQuYXR0cihcImRhdGFrZXlcIikgfHwgXCJsaXN0XCI7XG5cdFx0dGhpcy4kcGFnZSAgXHQ9IG51bGw7XG5cdFx0dGhpcy4kZW5kZWQgXHQ9IGZhbHNlO1xuXHRcdHRoaXMuJGxvYWRpbmcgICA9IGZhbHNlO1xuXHRcdHRoaXMuJHRtcFVybCAgICA9IHRoaXMuJHVybDtcblx0XHR0aGlzLl9fYmluZCgpO1xuXHR9XG5cblx0X19iaW5kKCkge1xuXHRcdGxldCAkaG9sZGVyICAgPSB0aGlzLiR0YXJnZXQucGFyZW50cyhcIi5zY3JvbGwtaG9sZGVyXCIpLFxuXHRcdFx0JG1heEg7XG5cdFx0aWYgKCRob2xkZXIubGVuZ3RoID09IDApIHtcblx0XHRcdCRob2xkZXIgICA9ICQod2luZG93KTtcblx0XHRcdCRtYXhIICAgICA9ICgpPT57XG5cdFx0XHRcdHJldHVybiAkKFwiYm9keVwiKS5oZWlnaHQoKTtcblx0XHRcdH1cblx0XHR9IGVsc2Uge1xuXHRcdFx0bGV0IF9tYXhIID0gJGhvbGRlci5wcm9wKFwic2Nyb2xsSGVpZ2h0XCIpO1xuXHRcdFx0JG1heEggICAgID0gKCk9Pntcblx0XHRcdFx0cmV0dXJuIF9tYXhIO1xuXHRcdFx0fVxuXHRcdH1cblx0XHRsZXQgJHZpZXdwb3J0ID0gJGhvbGRlci5oZWlnaHQoKTtcblx0XHQkaG9sZGVyLm9uKFwic2Nyb2xsXCIsIChldmVudCk9Pntcblx0XHRcdGlmICh0aGlzLiRlbmRlZCkgcmV0dXJuO1xuXHRcdFx0bGV0IHRvcCA9ICRob2xkZXIuc2Nyb2xsVG9wKCk7XG5cdFx0XHRsZXQgbWF4ID0gJG1heEgoKSAtICR2aWV3cG9ydDtcblx0XHRcdGlmIChtYXggLSB0b3AgPCAyMCkge1xuXHRcdFx0XHR0aGlzLnJlcXVlc3QoKTtcblx0XHRcdH1cblx0XHR9KTtcblx0fVxuXG5cdF9fdXJsKCkge1xuXHRcdGlmICh0aGlzLiRwYWdlKSB7XG5cdFx0XHRyZXR1cm4gdGhpcy4kdG1wVXJsLnJlcGxhY2UoXCJ7e2N1cnNvcn19XCIsYGN1cnNvcj0ke3RoaXMuJHBhZ2V9JmApO1xuXHRcdH0gZWxzZSB7XG5cdFx0XHRyZXR1cm4gdGhpcy4kdG1wVXJsLnJlcGxhY2UoXCJ7e2N1cnNvcn19XCIsXCJcIik7XG5cdFx0fVxuXHR9XG5cblx0X19yZXF1ZXN0X2RvbmUocmVzcG9uc2UpIHtcblx0XHR0aGlzLiRsb2FkaW5nID0gZmFsc2U7XG5cdFx0aWYoIXJlc3BvbnNlLmRhdGFbdGhpcy4kZGF0YWtleV0uaGFzTmV4dCkge1xuXHRcdFx0dGhpcy4kZW5kZWQgPSB0cnVlO1xuXHRcdH0gZWxzZSB7XG5cdFx0XHR0aGlzLiRwYWdlID0gcmVzcG9uc2UuZGF0YVt0aGlzLiRkYXRha2V5XS5uZXh0Q3Vyc29yO1xuXHRcdH1cblx0XHRzdXBlci5fX3JlcXVlc3RfZG9uZShyZXNwb25zZSk7XG5cdH1cblxuXHRyZWxvYWQoKSB7XG5cdFx0dGhpcy4kZW5kZWQgPSBmYWxzZTtcblx0XHR0aGlzLiRwYWdlICA9IG51bGw7XG5cdFx0JChgLiR7dGhpcy4kaWR9YCkucmVtb3ZlKCk7XG5cdFx0dGhpcy5yZXF1ZXN0KCk7XG5cdH1cblxuXHRyZXF1ZXN0KGRhdGEpIHtcblx0XHRpZiAodGhpcy4kZW5kZWQgfHwgdGhpcy4kbG9hZGluZykgcmV0dXJuO1xuXHRcdHRoaXMuJHVybCBcdFx0PSB0aGlzLl9fdXJsKCk7XG5cdFx0dGhpcy4kbG9hZGluZyBcdD0gdHJ1ZTtcblx0XHRzdXBlci5yZXF1ZXN0KGRhdGEpO1xuXHR9XG5cblx0cmVmcmVzaChkYXRhKSB7XG5cdFx0dGhpcy4kcmVzdWx0ID0gZGF0YSB8fCB0aGlzLiRyZXN1bHQ7XG5cdFx0aWYgKCF0aGlzLiRyZXN1bHQpIHJldHVybjtcblx0XHRsZXQgaHRtbCA9ICQudHJpbSh0aGlzLiR0ZW1wbGF0ZS5yZW5kZXIodGhpcy4kcmVzdWx0KSk7XG5cdFx0JChodG1sKS5hZGRDbGFzcyh0aGlzLiRpZCkuYXBwZW5kVG8odGhpcy4kaG9sZGVyKTtcblx0XHRpZiAodGhpcy4kaGFuZGxlcnMudXBkYXRlKSB7XG5cdFx0XHR0aGlzLiRoYW5kbGVycy51cGRhdGUodGhpcy4kcmVzdWx0KTtcblx0XHR9XG5cdH1cbn1cbiQuZm4ubXZjID0gZnVuY3Rpb24gKCl7XG5cdGxldCBpbnN0YW5jZSA9ICQodGhpcykuZGF0YShcIk1WQ19JTlNUQU5DRVwiKTtcblx0aWYgKGluc3RhbmNlKSB7XG5cdFx0cmV0dXJuIGluc3RhbmNlO1xuXHR9IGVsc2Uge1xuXHRcdGlmICgkKHRoaXMpLmF0dHIoXCJsb2FkbW9yZVwiKSkge1xuXHRcdFx0aW5zdGFuY2UgPSBuZXcgTG9hZE1vcmUodGhpcyk7XG5cdFx0fSBlbHNlIHtcblx0XHRcdGluc3RhbmNlID0gbmV3IE1WQyh0aGlzKTtcblx0XHR9XG5cdFx0JCh0aGlzKS5kYXRhKFwiTVZDX0lOU1RBTkNFXCIsIGluc3RhbmNlKTtcblx0XHRyZXR1cm4gaW5zdGFuY2U7XG5cdH1cbn1cblxuJChmdW5jdGlvbigpe1xuXHQkKFwic2NyaXB0W3R5cGU9J3RleHQveC1qc3JlbmRlciddXCIpLmVhY2goKGlkeCwgdGFyZ2V0KT0+e1xuXHRcdGxldCBtdmMgPSAkKHRhcmdldCkubXZjKCk7XG5cdFx0aWYgKCFtdmMubWFudWFsbHkoKSkge1xuXHRcdFx0bXZjLnJlcXVlc3QoKTtcblx0XHR9XG5cdH0pO1xufSk7XG5sZXQgT3V0cHV0ID0ge1xuXHRjb25maWcgOiAodXJsKT0+e1xuXHRcdGlmIChsb2NhdGlvbi5ob3N0LmluZGV4T2YoXCJ1ZGFuLmNuXCIpID09IC0xKSB7XG5cdFx0XHRVcmxCYXNlID0gdXJsIHx8IFVybEJhc2U7XG5cdFx0fVxuXHRcdHJldHVybiBPdXRwdXQ7XG5cdH0sXG5cdG5ldCA6IHtcblx0XHRnZXQgIDogKHVybCxkYXRhLGhhbmRsZXIsZXJyb3IpPT57XG5cdFx0XHQkLmdldChVcmxCYXNlK3VybCwgZGF0YSwgaGFuZGxlcikuZmFpbChlcnJvcik7XG5cdFx0fSxcblx0XHRwb3N0IDogKHVybCxkYXRhLGhhbmRsZXIsZXJyb3IpPT57XG5cdFx0XHQkLnBvc3QoVXJsQmFzZSt1cmwsIGRhdGEsIGhhbmRsZXIpLmZhaWwoZXJyb3IpO1xuXHRcdH1cblx0fSBcbn07XG5tb2R1bGUuZXhwb3J0cyA9IE91dHB1dDtcbn0se1wiLi9sb2FkaW5nLmpzXCI6Mn1dLDI6W2Z1bmN0aW9uKHJlcXVpcmUsbW9kdWxlLGV4cG9ydHMpe1xuY2xhc3MgTG9hZGluZyB7XG5cdGNvbnN0cnVjdG9yKCkge1xuXHRcdHRoaXMuJGRvbSAgPSAkKHRoaXMuX190ZW1wbGF0ZSgpKTtcblx0XHRsZXQgaG9sZGVyID0gJChcIi53cmFwcGVyXCIpO1xuXHRcdGlmIChob2xkZXIubGVuZ3RoID09IDApIHtcblx0XHRcdGhvbGRlciA9ICQoXCJib2R5XCIpO1xuXHRcdH1cblx0XHR0aGlzLiRkb20uYXBwZW5kVG8oaG9sZGVyKTtcblx0XHR0aGlzLiR0ZXh0ID0gJChcIi50eHRcIix0aGlzLiRkb20pO1xuXHR9XG5cblx0X190ZW1wbGF0ZSgpIHtcblx0XHRyZXR1cm4gJC50cmltKGBcblx0XHRcdDxkaXYgY2xhc3M9XCJtYXNrIGxvYWRpbmdcIiBzdHlsZT1cImRpc3BsYXk6bm9uZTtcIj5cblx0XHRcdFx0PHNwYW4gY2xhc3M9XCJsZCBsZC1zcGlubmVyIGxkLXNwaW5cIj48L3NwYW4+XG5cdFx0XHRcdDxkaXYgY2xhc3M9XCJ0eHRcIj48L2Rpdj5cblx0XHRcdDwvZGl2PlxuXHRcdGApO1xuXHR9XG5cblx0c2hvdyh0ZXh0KSB7XG5cdFx0aWYgKHRleHQpIHtcblx0XHRcdHRoaXMuJHRleHQuaHRtbCh0ZXh0KS5zaG93KCk7XG5cdFx0fSBlbHNlIHtcblx0XHRcdHRoaXMuJHRleHQuaGlkZSgpO1xuXHRcdH1cblx0XHR0aGlzLiRkb20uc2hvdygpO1xuXHR9XG5cblx0aGlkZSgpIHtcblx0XHR0aGlzLiRkb20uaGlkZSgpO1xuXHR9XG59XG5cbm1vZHVsZS5leHBvcnRzID0gbmV3IExvYWRpbmc7XG59LHt9XX0se30sWzFdKSJdfQ==
