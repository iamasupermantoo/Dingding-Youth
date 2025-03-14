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
//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbImxvYWRpbmcuanMiXSwibmFtZXMiOlsiZSIsInQiLCJuIiwiciIsInMiLCJvIiwidSIsImEiLCJyZXF1aXJlIiwiaSIsIkVycm9yIiwiZiIsImV4cG9ydHMiLCJjYWxsIiwibGVuZ3RoIiwibW9kdWxlIiwiTG9hZGluZyIsIiRkb20iLCIkIiwiX190ZW1wbGF0ZSIsImhvbGRlciIsImFwcGVuZFRvIiwiJHRleHQiLCJ0cmltIiwidGV4dCIsImh0bWwiLCJzaG93IiwiaGlkZSJdLCJtYXBwaW5ncyI6Ijs7Ozs7O0FBQUEsQ0FBQyxTQUFTQSxDQUFULENBQVdDLENBQVgsRUFBYUMsQ0FBYixFQUFlQyxDQUFmLEVBQWlCO0FBQUMsVUFBU0MsQ0FBVCxDQUFXQyxDQUFYLEVBQWFDLENBQWIsRUFBZTtBQUFDLE1BQUcsQ0FBQ0osRUFBRUcsQ0FBRixDQUFKLEVBQVM7QUFBQyxPQUFHLENBQUNKLEVBQUVJLENBQUYsQ0FBSixFQUFTO0FBQUMsUUFBSUUsSUFBRSxPQUFPQyxPQUFQLElBQWdCLFVBQWhCLElBQTRCQSxPQUFsQyxDQUEwQyxJQUFHLENBQUNGLENBQUQsSUFBSUMsQ0FBUCxFQUFTLE9BQU9BLEVBQUVGLENBQUYsRUFBSSxDQUFDLENBQUwsQ0FBUCxDQUFlLElBQUdJLENBQUgsRUFBSyxPQUFPQSxFQUFFSixDQUFGLEVBQUksQ0FBQyxDQUFMLENBQVAsQ0FBZSxNQUFNLElBQUlLLEtBQUosQ0FBVSx5QkFBdUJMLENBQXZCLEdBQXlCLEdBQW5DLENBQU47QUFBOEMsUUFBSU0sSUFBRVQsRUFBRUcsQ0FBRixJQUFLLEVBQUNPLFNBQVEsRUFBVCxFQUFYLENBQXdCWCxFQUFFSSxDQUFGLEVBQUssQ0FBTCxFQUFRUSxJQUFSLENBQWFGLEVBQUVDLE9BQWYsRUFBdUIsVUFBU1osQ0FBVCxFQUFXO0FBQUMsUUFBSUUsSUFBRUQsRUFBRUksQ0FBRixFQUFLLENBQUwsRUFBUUwsQ0FBUixDQUFOLENBQWlCLE9BQU9JLEVBQUVGLElBQUVBLENBQUYsR0FBSUYsQ0FBTixDQUFQO0FBQWdCLElBQXBFLEVBQXFFVyxDQUFyRSxFQUF1RUEsRUFBRUMsT0FBekUsRUFBaUZaLENBQWpGLEVBQW1GQyxDQUFuRixFQUFxRkMsQ0FBckYsRUFBdUZDLENBQXZGO0FBQTBGLFVBQU9ELEVBQUVHLENBQUYsRUFBS08sT0FBWjtBQUFvQixNQUFJSCxJQUFFLE9BQU9ELE9BQVAsSUFBZ0IsVUFBaEIsSUFBNEJBLE9BQWxDLENBQTBDLEtBQUksSUFBSUgsSUFBRSxDQUFWLEVBQVlBLElBQUVGLEVBQUVXLE1BQWhCLEVBQXVCVCxHQUF2QjtBQUEyQkQsSUFBRUQsRUFBRUUsQ0FBRixDQUFGO0FBQTNCLEVBQW1DLE9BQU9ELENBQVA7QUFBUyxDQUF2WixFQUF5WixFQUFDLEdBQUUsQ0FBQyxVQUFTSSxPQUFULEVBQWlCTyxNQUFqQixFQUF3QkgsT0FBeEIsRUFBZ0M7QUFBQSxNQUN2YkksT0FEdWI7QUFFNWIsc0JBQWM7QUFBQTs7QUFDYixTQUFLQyxJQUFMLEdBQWFDLEVBQUUsS0FBS0MsVUFBTCxFQUFGLENBQWI7QUFDQSxRQUFJQyxTQUFTRixFQUFFLFVBQUYsQ0FBYjtBQUNBLFFBQUlFLE9BQU9OLE1BQVAsSUFBaUIsQ0FBckIsRUFBd0I7QUFDdkJNLGNBQVNGLEVBQUUsTUFBRixDQUFUO0FBQ0E7QUFDRCxTQUFLRCxJQUFMLENBQVVJLFFBQVYsQ0FBbUJELE1BQW5CO0FBQ0EsU0FBS0UsS0FBTCxHQUFhSixFQUFFLE1BQUYsRUFBUyxLQUFLRCxJQUFkLENBQWI7QUFDQTs7QUFWMmI7QUFBQTtBQUFBLGlDQVkvYTtBQUNaLFlBQU9DLEVBQUVLLElBQUYsOEtBQVA7QUFNQTtBQW5CMmI7QUFBQTtBQUFBLHlCQXFCdmJDLElBckJ1YixFQXFCamI7QUFDVixTQUFJQSxJQUFKLEVBQVU7QUFDVCxXQUFLRixLQUFMLENBQVdHLElBQVgsQ0FBZ0JELElBQWhCLEVBQXNCRSxJQUF0QjtBQUNBLE1BRkQsTUFFTztBQUNOLFdBQUtKLEtBQUwsQ0FBV0ssSUFBWDtBQUNBO0FBQ0QsVUFBS1YsSUFBTCxDQUFVUyxJQUFWO0FBQ0E7QUE1QjJiO0FBQUE7QUFBQSwyQkE4QnJiO0FBQ04sVUFBS1QsSUFBTCxDQUFVVSxJQUFWO0FBQ0E7QUFoQzJiOztBQUFBO0FBQUE7O0FBbUM3YlosU0FBT0gsT0FBUCxHQUFpQixJQUFJSSxPQUFKLEVBQWpCO0FBQ0MsRUFwQzJaLEVBb0MxWixFQXBDMFosQ0FBSCxFQUF6WixFQW9DTyxFQXBDUCxFQW9DVSxDQUFDLENBQUQsQ0FwQ1YiLCJmaWxlIjoibG9hZGluZy5qcyIsInNvdXJjZXNDb250ZW50IjpbIihmdW5jdGlvbiBlKHQsbixyKXtmdW5jdGlvbiBzKG8sdSl7aWYoIW5bb10pe2lmKCF0W29dKXt2YXIgYT10eXBlb2YgcmVxdWlyZT09XCJmdW5jdGlvblwiJiZyZXF1aXJlO2lmKCF1JiZhKXJldHVybiBhKG8sITApO2lmKGkpcmV0dXJuIGkobywhMCk7dGhyb3cgbmV3IEVycm9yKFwiQ2Fubm90IGZpbmQgbW9kdWxlICdcIitvK1wiJ1wiKX12YXIgZj1uW29dPXtleHBvcnRzOnt9fTt0W29dWzBdLmNhbGwoZi5leHBvcnRzLGZ1bmN0aW9uKGUpe3ZhciBuPXRbb11bMV1bZV07cmV0dXJuIHMobj9uOmUpfSxmLGYuZXhwb3J0cyxlLHQsbixyKX1yZXR1cm4gbltvXS5leHBvcnRzfXZhciBpPXR5cGVvZiByZXF1aXJlPT1cImZ1bmN0aW9uXCImJnJlcXVpcmU7Zm9yKHZhciBvPTA7bzxyLmxlbmd0aDtvKyspcyhyW29dKTtyZXR1cm4gc30pKHsxOltmdW5jdGlvbihyZXF1aXJlLG1vZHVsZSxleHBvcnRzKXtcbmNsYXNzIExvYWRpbmcge1xuXHRjb25zdHJ1Y3RvcigpIHtcblx0XHR0aGlzLiRkb20gID0gJCh0aGlzLl9fdGVtcGxhdGUoKSk7XG5cdFx0bGV0IGhvbGRlciA9ICQoXCIud3JhcHBlclwiKTtcblx0XHRpZiAoaG9sZGVyLmxlbmd0aCA9PSAwKSB7XG5cdFx0XHRob2xkZXIgPSAkKFwiYm9keVwiKTtcblx0XHR9XG5cdFx0dGhpcy4kZG9tLmFwcGVuZFRvKGhvbGRlcik7XG5cdFx0dGhpcy4kdGV4dCA9ICQoXCIudHh0XCIsdGhpcy4kZG9tKTtcblx0fVxuXG5cdF9fdGVtcGxhdGUoKSB7XG5cdFx0cmV0dXJuICQudHJpbShgXG5cdFx0XHQ8ZGl2IGNsYXNzPVwibWFzayBsb2FkaW5nXCIgc3R5bGU9XCJkaXNwbGF5Om5vbmU7XCI+XG5cdFx0XHRcdDxzcGFuIGNsYXNzPVwibGQgbGQtc3Bpbm5lciBsZC1zcGluXCI+PC9zcGFuPlxuXHRcdFx0XHQ8ZGl2IGNsYXNzPVwidHh0XCI+PC9kaXY+XG5cdFx0XHQ8L2Rpdj5cblx0XHRgKTtcblx0fVxuXG5cdHNob3codGV4dCkge1xuXHRcdGlmICh0ZXh0KSB7XG5cdFx0XHR0aGlzLiR0ZXh0Lmh0bWwodGV4dCkuc2hvdygpO1xuXHRcdH0gZWxzZSB7XG5cdFx0XHR0aGlzLiR0ZXh0LmhpZGUoKTtcblx0XHR9XG5cdFx0dGhpcy4kZG9tLnNob3coKTtcblx0fVxuXG5cdGhpZGUoKSB7XG5cdFx0dGhpcy4kZG9tLmhpZGUoKTtcblx0fVxufVxuXG5tb2R1bGUuZXhwb3J0cyA9IG5ldyBMb2FkaW5nO1xufSx7fV19LHt9LFsxXSkiXX0=
