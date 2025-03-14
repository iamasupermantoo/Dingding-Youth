"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

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
		var Utils = function () {
			function Utils() {
				_classCallCheck(this, Utils);

				var QueryParams = {};
				window.location.search.replace(/^\?/, "").split("&").forEach(function (item) {
					var parsed = item.split("=");
					QueryParams[parsed[0]] = parsed[1];
				});
				this.QueryParams = QueryParams;
			}

			_createClass(Utils, [{
				key: "timeToStr",
				value: function timeToStr(second) {
					var minutes = second / 60 >> 0,
					    seconds = second - minutes * 60 >> 0;
					var T = function T(v) {
						return v <= 9 ? "0" + v : v;
					};
					return T(minutes) + ":" + T(seconds);
				}
			}, {
				key: "timeToDotStr",
				value: function timeToDotStr(second) {
					var minutes = second / 60 >> 0,
					    seconds = second - minutes * 60 >> 0;
					var T = function T(v) {
						return v <= 9 ? "0" + v : v;
					};
					if (second == 60) {
						return "60\"";
					} else if (minutes > 0) {
						return T(minutes) + "'" + T(seconds) + "\"";
					} else {
						return T(seconds) + "\"";
					}
				}
			}, {
				key: "resetTitle",
				value: function resetTitle(title) {
					document.title = title;
					var $iframe = $('<iframe src="/img/null.gif"></iframe>').on('load', function () {
						setTimeout(function () {
							$iframe.off('load').remove();
						}, 0);
					}).appendTo("body");
				}
			}]);

			return Utils;
		}();

		module.exports = new Utils();
	}, {}] }, {}, [1]);
//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbInV0aWxzLmpzIl0sIm5hbWVzIjpbImUiLCJ0IiwibiIsInIiLCJzIiwibyIsInUiLCJhIiwicmVxdWlyZSIsImkiLCJFcnJvciIsImYiLCJleHBvcnRzIiwiY2FsbCIsImxlbmd0aCIsIm1vZHVsZSIsIlV0aWxzIiwiUXVlcnlQYXJhbXMiLCJ3aW5kb3ciLCJsb2NhdGlvbiIsInNlYXJjaCIsInJlcGxhY2UiLCJzcGxpdCIsImZvckVhY2giLCJpdGVtIiwicGFyc2VkIiwic2Vjb25kIiwibWludXRlcyIsInNlY29uZHMiLCJUIiwidiIsInRpdGxlIiwiZG9jdW1lbnQiLCIkaWZyYW1lIiwiJCIsIm9uIiwic2V0VGltZW91dCIsIm9mZiIsInJlbW92ZSIsImFwcGVuZFRvIl0sIm1hcHBpbmdzIjoiOzs7Ozs7QUFBQSxDQUFDLFNBQVNBLENBQVQsQ0FBV0MsQ0FBWCxFQUFhQyxDQUFiLEVBQWVDLENBQWYsRUFBaUI7QUFBQyxVQUFTQyxDQUFULENBQVdDLENBQVgsRUFBYUMsQ0FBYixFQUFlO0FBQUMsTUFBRyxDQUFDSixFQUFFRyxDQUFGLENBQUosRUFBUztBQUFDLE9BQUcsQ0FBQ0osRUFBRUksQ0FBRixDQUFKLEVBQVM7QUFBQyxRQUFJRSxJQUFFLE9BQU9DLE9BQVAsSUFBZ0IsVUFBaEIsSUFBNEJBLE9BQWxDLENBQTBDLElBQUcsQ0FBQ0YsQ0FBRCxJQUFJQyxDQUFQLEVBQVMsT0FBT0EsRUFBRUYsQ0FBRixFQUFJLENBQUMsQ0FBTCxDQUFQLENBQWUsSUFBR0ksQ0FBSCxFQUFLLE9BQU9BLEVBQUVKLENBQUYsRUFBSSxDQUFDLENBQUwsQ0FBUCxDQUFlLE1BQU0sSUFBSUssS0FBSixDQUFVLHlCQUF1QkwsQ0FBdkIsR0FBeUIsR0FBbkMsQ0FBTjtBQUE4QyxRQUFJTSxJQUFFVCxFQUFFRyxDQUFGLElBQUssRUFBQ08sU0FBUSxFQUFULEVBQVgsQ0FBd0JYLEVBQUVJLENBQUYsRUFBSyxDQUFMLEVBQVFRLElBQVIsQ0FBYUYsRUFBRUMsT0FBZixFQUF1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QTNDMFosQ0FBSCxFQUF6WixFQTJDTyxFQTNDUCxFQTJDVSxDQUFDLENBQUQsQ0EzQ1YiLCJmaWxlIjoidXRpbHMuanMiLCJzb3VyY2VzQ29udGVudCI6WyIoZnVuY3Rpb24gZSh0LG4scil7ZnVuY3Rpb24gcyhvLHUpe2lmKCFuW29dKXtpZighdFtvXSl7dmFyIGE9dHlwZW9mIHJlcXVpcmU9PVwiZnVuY3Rpb25cIiYmcmVxdWlyZTtpZighdSYmYSlyZXR1cm4gYShvLCEwKTtpZihpKXJldHVybiBpKG8sITApO3Rocm93IG5ldyBFcnJvcihcIkNhbm5vdCBmaW5kIG1vZHVsZSAnXCIrbytcIidcIil9dmFyIGY9bltvXT17ZXhwb3J0czp7fX07dFtvXVswXS5jYWxsKGYuZXhwb3J0cyxmdW5jdGlvbihlKXt2YXIgbj10W29dWzFdW2VdO3JldHVybiBzKG4/bjplKX0sZixmLmV4cG9ydHMsZSx0LG4scil9cmV0dXJuIG5bb10uZXhwb3J0c312YXIgaT10eXBlb2YgcmVxdWlyZT09XCJmdW5jdGlvblwiJiZyZXF1aXJlO2Zvcih2YXIgbz0wO288ci5sZW5ndGg7bysrKXMocltvXSk7cmV0dXJuIHN9KSh7MTpbZnVuY3Rpb24ocmVxdWlyZSxtb2R1bGUsZXhwb3J0cyl7XG5jbGFzcyBVdGlscyB7XG5cdGNvbnN0cnVjdG9yKCkge1xuXHRcdHZhciBRdWVyeVBhcmFtcyA9IHt9O1xuXHRcdHdpbmRvdy5sb2NhdGlvbi5zZWFyY2gucmVwbGFjZSgvXlxcPy8sXCJcIikuc3BsaXQoXCImXCIpLmZvckVhY2goKGl0ZW0pPT57XG5cdFx0XHR2YXIgcGFyc2VkID0gaXRlbS5zcGxpdChcIj1cIik7XG5cdFx0XHRRdWVyeVBhcmFtc1twYXJzZWRbMF1dID0gcGFyc2VkWzFdO1xuXHRcdH0pO1xuXHRcdHRoaXMuUXVlcnlQYXJhbXMgPSBRdWVyeVBhcmFtcztcblx0fVxuXG5cdHRpbWVUb1N0ciAoc2Vjb25kKSB7XG5cdFx0bGV0IG1pbnV0ZXMgPSBzZWNvbmQvNjAgPj4gMCxcblx0XHRcdHNlY29uZHMgPSAoc2Vjb25kIC0gbWludXRlcyo2MCkgPj4gMDtcblx0XHRsZXQgVCBcdFx0PSB2PT52PD05PyhcIjBcIit2KTp2O1xuXHRcdHJldHVybiBgJHtUKG1pbnV0ZXMpfToke1Qoc2Vjb25kcyl9YDtcblx0fVxuXG5cdHRpbWVUb0RvdFN0ciAoc2Vjb25kKSB7XG5cdFx0bGV0IG1pbnV0ZXMgPSBzZWNvbmQvNjAgPj4gMCxcblx0XHRcdHNlY29uZHMgPSAoc2Vjb25kIC0gbWludXRlcyo2MCkgPj4gMDtcblx0XHRsZXQgVCBcdFx0PSB2PT52PD05PyhcIjBcIit2KTp2O1xuXHRcdGlmIChzZWNvbmQgPT0gNjApIHtcblx0XHRcdHJldHVybiBgNjBcImA7XG5cdFx0fSBlbHNlIGlmIChtaW51dGVzID4gMCkge1xuXHRcdFx0cmV0dXJuIGAke1QobWludXRlcyl9JyR7VChzZWNvbmRzKX1cImA7XG5cdFx0fSBlbHNlIHtcblx0XHRcdHJldHVybiBgJHtUKHNlY29uZHMpfVwiYDtcblx0XHR9XG5cdFx0XG5cdH1cblxuXHRyZXNldFRpdGxlICh0aXRsZSkge1xuICAgICAgICBkb2N1bWVudC50aXRsZSA9IHRpdGxlO1xuICAgICAgICB2YXIgJGlmcmFtZSA9ICQoJzxpZnJhbWUgc3JjPVwiL2ltZy9udWxsLmdpZlwiPjwvaWZyYW1lPicpLm9uKCdsb2FkJywgZnVuY3Rpb24oKSB7XG4gICAgICAgICAgICBzZXRUaW1lb3V0KGZ1bmN0aW9uKCkge1xuICAgICAgICAgICAgICAgICRpZnJhbWUub2ZmKCdsb2FkJykucmVtb3ZlKClcbiAgICAgICAgICAgIH0sIDApO1xuICAgICAgICB9KS5hcHBlbmRUbyhcImJvZHlcIik7XG4gICAgfVxufVxuXG5tb2R1bGUuZXhwb3J0cyA9IG5ldyBVdGlscztcbn0se31dfSx7fSxbMV0pIl19
