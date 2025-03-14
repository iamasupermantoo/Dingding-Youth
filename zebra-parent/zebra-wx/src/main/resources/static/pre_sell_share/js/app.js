(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);throw new Error("Cannot find module '"+o+"'")}var f=n[o]={exports:{}};t[o][0].call(f.exports,function(e){var n=t[o][1][e];return s(n?n:e)},f,f.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
"use strict";

(function () {
	function Init() {
		$(".share").on("touchstart", function (event) {
			event.preventDefault();
			$(".share").addClass("active");
		});
		$(".share").on("touchend", function (event) {
			$(".share").removeClass("active");
			$(".mask").fadeIn();
		});
		$(".mask").on("click", function (event) {
			$(".mask").fadeOut();
		});
		$(".dialog").on("click", ".close", function (event) {
			HideDialog();
		});

		function ShowDialog() {
			$(".dialog").show();
			setTimeout(function () {
				$(".dialog").addClass("show");
			}, 100);
		}

		function HideDialog() {
			$(".dialog").fadeOut(function () {
				$(".dialog").removeClass("show");
			});
		}

		weixinShare.onShared = function () {
			ShowDialog();
		};
	}
	Init();
})();
},{}]},{},[1])
//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbImFwcC5qcyJdLCJuYW1lcyI6WyJJbml0IiwiJCIsIm9uIiwiZXZlbnQiLCJwcmV2ZW50RGVmYXVsdCIsImFkZENsYXNzIiwicmVtb3ZlQ2xhc3MiLCJmYWRlSW4iLCJmYWRlT3V0IiwiSGlkZURpYWxvZyIsIlNob3dEaWFsb2ciLCJzaG93Iiwic2V0VGltZW91dCIsIndlaXhpblNoYXJlIiwib25TaGFyZWQiXSwibWFwcGluZ3MiOiI7O0FBQUEsQ0FBQyxZQUFVO0FBQ1YsVUFBU0EsSUFBVCxHQUFnQjtBQUNmQyxJQUFFLFFBQUYsRUFBWUMsRUFBWixDQUFlLFlBQWYsRUFBNEIsVUFBU0MsS0FBVCxFQUFlO0FBQ2pDQSxTQUFNQyxjQUFOO0FBQ0FILEtBQUUsUUFBRixFQUFZSSxRQUFaLENBQXFCLFFBQXJCO0FBQ0gsR0FIUDtBQUlNSixJQUFFLFFBQUYsRUFBWUMsRUFBWixDQUFlLFVBQWYsRUFBMEIsVUFBU0MsS0FBVCxFQUFlO0FBQ3JDRixLQUFFLFFBQUYsRUFBWUssV0FBWixDQUF3QixRQUF4QjtBQUNUTCxLQUFFLE9BQUYsRUFBV00sTUFBWDtBQUNNLEdBSEQ7QUFJTk4sSUFBRSxPQUFGLEVBQVdDLEVBQVgsQ0FBYyxPQUFkLEVBQXNCLFVBQVNDLEtBQVQsRUFBZTtBQUNwQ0YsS0FBRSxPQUFGLEVBQVdPLE9BQVg7QUFDQSxHQUZEO0FBR0FQLElBQUUsU0FBRixFQUFhQyxFQUFiLENBQWdCLE9BQWhCLEVBQXdCLFFBQXhCLEVBQWlDLFVBQVNDLEtBQVQsRUFBZTtBQUMvQ007QUFDQSxHQUZEOztBQUlBLFdBQVNDLFVBQVQsR0FBc0I7QUFDckJULEtBQUUsU0FBRixFQUFhVSxJQUFiO0FBQ0FDLGNBQVcsWUFBVTtBQUNwQlgsTUFBRSxTQUFGLEVBQWFJLFFBQWIsQ0FBc0IsTUFBdEI7QUFDQSxJQUZELEVBRUUsR0FGRjtBQUdBOztBQUVELFdBQVNJLFVBQVQsR0FBc0I7QUFDckJSLEtBQUUsU0FBRixFQUFhTyxPQUFiLENBQXFCLFlBQVU7QUFDOUJQLE1BQUUsU0FBRixFQUFhSyxXQUFiLENBQXlCLE1BQXpCO0FBQ0EsSUFGRDtBQUdBOztBQUVETyxjQUFZQyxRQUFaLEdBQXVCLFlBQVc7QUFDakNKO0FBQ0EsR0FGRDtBQUdBO0FBQ0RWO0FBQ0EsQ0FuQ0QiLCJmaWxlIjoiYXBwLmpzIiwic291cmNlc0NvbnRlbnQiOlsiKGZ1bmN0aW9uKCl7XG5cdGZ1bmN0aW9uIEluaXQoKSB7XG5cdFx0JChcIi5zaGFyZVwiKS5vbihcInRvdWNoc3RhcnRcIixmdW5jdGlvbihldmVudCl7XG4gICAgICAgICAgICBldmVudC5wcmV2ZW50RGVmYXVsdCgpO1xuICAgICAgICAgICAgJChcIi5zaGFyZVwiKS5hZGRDbGFzcyhcImFjdGl2ZVwiKTtcbiAgICAgICAgfSk7XG4gICAgICAgICQoXCIuc2hhcmVcIikub24oXCJ0b3VjaGVuZFwiLGZ1bmN0aW9uKGV2ZW50KXtcbiAgICAgICAgICAgICQoXCIuc2hhcmVcIikucmVtb3ZlQ2xhc3MoXCJhY3RpdmVcIik7XG5cdFx0XHQkKFwiLm1hc2tcIikuZmFkZUluKCk7XG4gICAgICAgIH0pO1xuXHRcdCQoXCIubWFza1wiKS5vbihcImNsaWNrXCIsZnVuY3Rpb24oZXZlbnQpe1xuXHRcdFx0JChcIi5tYXNrXCIpLmZhZGVPdXQoKTtcblx0XHR9KTtcblx0XHQkKFwiLmRpYWxvZ1wiKS5vbihcImNsaWNrXCIsXCIuY2xvc2VcIixmdW5jdGlvbihldmVudCl7XG5cdFx0XHRIaWRlRGlhbG9nKCk7XG5cdFx0fSk7XG5cblx0XHRmdW5jdGlvbiBTaG93RGlhbG9nKCkge1xuXHRcdFx0JChcIi5kaWFsb2dcIikuc2hvdygpO1xuXHRcdFx0c2V0VGltZW91dChmdW5jdGlvbigpe1xuXHRcdFx0XHQkKFwiLmRpYWxvZ1wiKS5hZGRDbGFzcyhcInNob3dcIik7XG5cdFx0XHR9LDEwMCk7XG5cdFx0fVxuXG5cdFx0ZnVuY3Rpb24gSGlkZURpYWxvZygpIHtcblx0XHRcdCQoXCIuZGlhbG9nXCIpLmZhZGVPdXQoZnVuY3Rpb24oKXtcblx0XHRcdFx0JChcIi5kaWFsb2dcIikucmVtb3ZlQ2xhc3MoXCJzaG93XCIpO1xuXHRcdFx0fSlcblx0XHR9XG5cblx0XHR3ZWl4aW5TaGFyZS5vblNoYXJlZCA9IGZ1bmN0aW9uKCkge1xuXHRcdFx0U2hvd0RpYWxvZygpO1xuXHRcdH1cblx0fVxuXHRJbml0KCk7XG59KSgpOyJdfQ==
