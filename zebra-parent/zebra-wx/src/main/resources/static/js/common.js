"use strict";

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

        function SlidePage(page, onStart, onUpdate, onComplete, maxWidth, maxHeight) {
            this.page = page;
            this.onStart = onStart;
            this.onUpdate = onUpdate;
            this.onComplete = onComplete;
            this.maxWidth = maxWidth || $(this.page).width();
            this.maxHeight = maxHeight || $(this.page).height();
            this.init();
        }

        SlidePage.prototype = {
            init: function init() {
                var me = this,
                    moved = false,
                    startPos,
                    lastPos;
                $(this.page).on("touchstart", function (event) {
                    startPos = { x: event.clientX, y: event.clientY };
                    $(me.page).on("touchmove", function (event) {
                        me.onUpdate((event.clientX - startPos.x) / me.maxWidth, (event.clientY - startPos.y) / me.maxHeight);
                        lastPos = { x: event.clientX, y: event.clientY };
                        if (!moved) {
                            me.onStart();
                        };
                        moved = true;
                        event.preventDefault();
                    });
                    // event.preventDefault();
                });
                function unregister(event) {
                    $(me.page).off("touchend");
                    if (moved) {
                        me.onComplete((lastPos.x - startPos.x) / me.maxWidth, (lastPos.y - startPos.y) / me.maxHeight);
                        moved = false;
                    };
                    // event.preventDefault();
                }
                $(this.page).on("touchend", unregister);
                $(this.page).on("touchcancel", unregister);
            }
        };

        function MakeLog(name, args) {
            if (args) {
                args += "&t=" + new Date().getTime();
            } else {
                args = "t=" + new Date().getTime();
            };
            $("#log").attr("src", "http://" + window.location.host + "/log/" + name + ".html?" + args);
        }

        var WeiXinShare = function WeiXinShare() {
            this.img = "";
            this.link = "";
            this.desc = "";
            this.appid = "";
            this.title = "";
            this.onShared = null;
        };
        WeiXinShare.prototype = {
            init: function init() {
                var me = this;
                wx.config({
                    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                    appId: APPID, // 必填，公众号的唯一标识
                    timestamp: TIME, // 必填，生成签名的时间戳
                    nonceStr: RANDOM, // 必填，生成签名的随机串
                    signature: WXTOKEN, // 必填，签名，见附录1
                    jsApiList: ["onMenuShareTimeline", "onMenuShareAppMessage", "onMenuShareQQ", "onMenuShareWeibo"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                });
                wx.ready(function () {
                    me.refresh();
                });
                wx.error(function (res) {
                    alert(JSON.stringify(res));
                });
            },

            refresh: function refresh() {
                this.shareFriend();
                this.shareTimeline();
                this.shareWeibo();
                this.shareQQ();
            },

            shareFriend: function shareFriend() {
                var me = this;
                wx.onMenuShareAppMessage({
                    title: this.title, // 分享标题
                    desc: this.desc, // 分享描述
                    link: this.link, // 分享链接
                    imgUrl: this.img, // 分享图标
                    type: 'link', // 分享类型,music、video或link，不填默认为link
                    success: function success() {
                        // 用户确认分享后执行的回调函数
                        me.sharedSuccessCallBack();
                        me.makeLog("type=friend");
                    },
                    cancel: function cancel() {
                        // 用户取消分享后执行的回调函数
                    }
                });
            },
            shareTimeline: function shareTimeline() {
                var me = this;
                wx.onMenuShareTimeline({
                    title: this.title, // 分享标题
                    link: this.link, // 分享链接
                    imgUrl: this.img, // 分享图标
                    success: function success() {
                        me.sharedSuccessCallBack();
                        me.makeLog("type=timeline");
                    },
                    cancel: function cancel() {}
                });
            },
            shareWeibo: function shareWeibo() {
                var me = this;
                wx.onMenuShareWeibo({
                    title: this.title, // 分享标题
                    desc: this.desc, // 分享描述
                    link: this.link, // 分享链接
                    imgUrl: this.img, // 分享图标
                    success: function success() {
                        me.sharedSuccessCallBack();
                        me.makeLog("type=weibo");
                    },
                    cancel: function cancel() {
                        // 用户取消分享后执行的回调函数
                    }
                });
            },
            shareQQ: function shareQQ() {
                var me = this;
                wx.onMenuShareQQ({
                    title: this.title, // 分享标题
                    desc: this.desc, // 分享描述
                    link: this.link, // 分享链接
                    imgUrl: this.img, // 分享图标
                    success: function success() {
                        me.sharedSuccessCallBack();
                        me.makeLog("type=qq");
                    },
                    cancel: function cancel() {
                        // 用户取消分享后执行的回调函数
                    }
                });
            },
            makeLog: function makeLog(args) {
                MakeLog("share", args);
            },
            sharedSuccessCallBack: function sharedSuccessCallBack() {
                if (this.onShared) {
                    this.onShared();
                };
            }
        };
        $(document).on("WeixinJSBridgeReady", function () {
            $(document).off("WeixinJSBridgeReady");
        });

        window.weixinShare = new WeiXinShare();

        window.CALL_INIT_WEIXIN_SHARE = function () {
            weixinShare.img = window.location.origin + "/share/img/share-icon.jpg";
            weixinShare.link = window.location.href;
            weixinShare.title = "优蛋";
            weixinShare.desc = "优蛋";
            weixinShare.init();
        };
        var ua = window.navigator.userAgent;
        var isWeixin = ua.toLowerCase().match(/MicroMessenger/i) == 'micromessenger';
        if (isWeixin) {
            var hm = document.createElement("script");
            hm.src = "/presell/share/?f=" + encodeURIComponent(window.location.href) + "&t=" + new Date().getTime();
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        }

        function Format(str, data) {
            return str.replace(/\#\{(\d+)\}/g, function (m, d) {
                return data[d];
            });
        }

        function Bind(func, arg1, arg2, arg3, arg4) {
            return function () {
                func(arg1, arg2, arg3, arg4);
            };
        }

        /**
         * 
         * @param musicbtn [string] 传入需要绑定的播放按钮
         * @param audio [string] 传入需要播放的audio的id
         */
        function Player(musicbtn, audio, handlers) {
            this._btn = $(musicbtn);
            this._audio = $(audio)[0];
            this._playing = false;
            this._handlers = handlers || {};
            this.bind();
        }

        Player.prototype = {
            __wechat: function __wechat() {
                var userAgent = navigator.userAgent;
                var ua = userAgent.toLowerCase();
                if (ua.match(/MicroMessenger/i) == 'micromessenger') {
                    return true;
                }
            },
            __onplay: function __onplay() {
                this._btn.addClass("on");
                this._playing = true;
                if (this._handlers.onplay) {
                    this._handlers.onplay();
                }
            },
            __onpause: function __onpause() {
                this._btn.removeClass("on");
                this._playing = false;
                if (this._handlers.onpause) {
                    this._handlers.onpause();
                }
            },
            play: function play(url, handlers) {
                this._handlers = handlers;
                if (this._ready) {
                    this._audio.src = url;
                    this._audio.play();
                } else {
                    this._need_play = url;
                }
            },
            resume: function resume() {
                this._audio.play();
            },
            pause: function pause() {
                this._audio.pause();
            },
            bind: function bind() {
                var me = this;
                $(this._audio).on("play", function () {
                    me.__onplay();
                });
                $(this._audio).on("pause", function () {
                    me.__onpause();
                });
                $(this._audio).on("timeupdate", function () {
                    var current = me._audio.currentTime;
                    if (me._handlers.onupdate) {
                        me._handlers.onupdate(current);
                    }
                });
                this._btn.on("click", function (event) {
                    event.preventDefault();
                    if (me._playing) {
                        me._audio.pause();
                    } else {
                        me._audio.play();
                    }
                });
                if (this.__wechat()) {
                    var OnReady = function OnReady() {
                        if (me._need_play) {
                            me._audio.src = me._need_play;
                            me._need_play = false;
                        }
                        $(document).off("WeixinJSBridgeReady");
                        me._ready = true;
                        if (me._handlers.onready) {
                            me._handlers.onready();
                        }
                    };

                    if (!window.JsBridgeReady) {
                        document.addEventListener("WeixinJSBridgeReady", OnReady);
                    } else {
                        OnReady();
                    }
                } else {
                    this._ready = true;
                    if (me._handlers.onready) {
                        me._handlers.onready();
                    }
                }
            }
        };

        window.Player = Player;
    }, {}] }, {}, [1]);
//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbImNvbW1vbi5qcyJdLCJuYW1lcyI6WyJlIiwidCIsIm4iLCJyIiwicyIsIm8iLCJ1IiwiYSIsInJlcXVpcmUiLCJpIiwiRXJyb3IiLCJmIiwiZXhwb3J0cyIsImNhbGwiLCJsZW5ndGgiLCJtb2R1bGUiLCJTbGlkZVBhZ2UiLCJwYWdlIiwib25TdGFydCIsIm9uVXBkYXRlIiwib25Db21wbGV0ZSIsIm1heFdpZHRoIiwibWF4SGVpZ2h0IiwiJCIsIndpZHRoIiwiaGVpZ2h0IiwiaW5pdCIsInByb3RvdHlwZSIsIm1lIiwibW92ZWQiLCJzdGFydFBvcyIsImxhc3RQb3MiLCJvbiIsImV2ZW50IiwieCIsImNsaWVudFgiLCJ5IiwiY2xpZW50WSIsInByZXZlbnREZWZhdWx0IiwidW5yZWdpc3RlciIsIm9mZiIsIk1ha2VMb2ciLCJuYW1lIiwiYXJncyIsIkRhdGUiLCJnZXRUaW1lIiwiYXR0ciIsIndpbmRvdyIsImxvY2F0aW9uIiwiaG9zdCIsIldlaVhpblNoYXJlIiwiaW1nIiwibGluayIsImRlc2MiLCJhcHBpZCIsInRpdGxlIiwib25TaGFyZWQiLCJ3eCIsImNvbmZpZyIsImRlYnVnIiwiYXBwSWQiLCJBUFBJRCIsInRpbWVzdGFtcCIsIlRJTUUiLCJub25jZVN0ciIsIlJBTkRPTSIsInNpZ25hdHVyZSIsIldYVE9LRU4iLCJqc0FwaUxpc3QiLCJyZWFkeSIsInJlZnJlc2giLCJlcnJvciIsInJlcyIsImFsZXJ0IiwiSlNPTiIsInN0cmluZ2lmeSIsInNoYXJlRnJpZW5kIiwic2hhcmVUaW1lbGluZSIsInNoYXJlV2VpYm8iLCJzaGFyZVFRIiwib25NZW51U2hhcmVBcHBNZXNzYWdlIiwiaW1nVXJsIiwidHlwZSIsInN1Y2Nlc3MiLCJzaGFyZWRTdWNjZXNzQ2FsbEJhY2siLCJtYWtlTG9nIiwiY2FuY2VsIiwib25NZW51U2hhcmVUaW1lbGluZSIsIm9uTWVudVNoYXJlV2VpYm8iLCJvbk1lbnVTaGFyZVFRIiwiZG9jdW1lbnQiLCJ3ZWl4aW5TaGFyZSIsIkNBTExfSU5JVF9XRUlYSU5fU0hBUkUiLCJvcmlnaW4iLCJocmVmIiwidWEiLCJuYXZpZ2F0b3IiLCJ1c2VyQWdlbnQiLCJpc1dlaXhpbiIsInRvTG93ZXJDYXNlIiwibWF0Y2giLCJobSIsImNyZWF0ZUVsZW1lbnQiLCJzcmMiLCJlbmNvZGVVUklDb21wb25lbnQiLCJnZXRFbGVtZW50c0J5VGFnTmFtZSIsInBhcmVudE5vZGUiLCJpbnNlcnRCZWZvcmUiLCJGb3JtYXQiLCJzdHIiLCJkYXRhIiwicmVwbGFjZSIsIm0iLCJkIiwiQmluZCIsImZ1bmMiLCJhcmcxIiwiYXJnMiIsImFyZzMiLCJhcmc0IiwiUGxheWVyIiwibXVzaWNidG4iLCJhdWRpbyIsImhhbmRsZXJzIiwiX2J0biIsIl9hdWRpbyIsIl9wbGF5aW5nIiwiX2hhbmRsZXJzIiwiYmluZCIsIl9fd2VjaGF0IiwiX19vbnBsYXkiLCJhZGRDbGFzcyIsIm9ucGxheSIsIl9fb25wYXVzZSIsInJlbW92ZUNsYXNzIiwib25wYXVzZSIsInBsYXkiLCJ1cmwiLCJfcmVhZHkiLCJfbmVlZF9wbGF5IiwicmVzdW1lIiwicGF1c2UiLCJjdXJyZW50IiwiY3VycmVudFRpbWUiLCJvbnVwZGF0ZSIsIk9uUmVhZHkiLCJvbnJlYWR5IiwiSnNCcmlkZ2VSZWFkeSIsImFkZEV2ZW50TGlzdGVuZXIiXSwibWFwcGluZ3MiOiI7O0FBQUEsQ0FBQyxTQUFTQSxDQUFULENBQVdDLENBQVgsRUFBYUMsQ0FBYixFQUFlQyxDQUFmLEVBQWlCO0FBQUMsYUFBU0MsQ0FBVCxDQUFXQyxDQUFYLEVBQWFDLENBQWIsRUFBZTtBQUFDLFlBQUcsQ0FBQ0osRUFBRUcsQ0FBRixDQUFKLEVBQVM7QUFBQyxnQkFBRyxDQUFDSixFQUFFSSxDQUFGLENBQUosRUFBUztBQUFDLG9CQUFJRSxJQUFFLE9BQU9DLE9BQVAsSUFBZ0IsVUFBaEIsSUFBNEJBLE9BQWxDLENBQTBDLElBQUcsQ0FBQ0YsQ0FBRCxJQUFJQyxDQUFQLEVBQVMsT0FBT0EsRUFBRUYsQ0FBRixFQUFJLENBQUMs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cUJBRkQsTUFFTztBQUNIQTtBQUNIO0FBQ0osaUJBakJELE1BaUJPO0FBQ0gseUJBQUtQLE1BQUwsR0FBYyxJQUFkO0FBQ0Esd0JBQUk5RyxHQUFHbUcsU0FBSCxDQUFhbUIsT0FBakIsRUFBMEI7QUFDdEJ0SCwyQkFBR21HLFNBQUgsQ0FBYW1CLE9BQWI7QUFDSDtBQUNKO0FBQ0o7QUFsRmMsU0FBbkI7O0FBcUZBbkcsZUFBT3lFLE1BQVAsR0FBZ0JBLE1BQWhCO0FBQ0MsS0F4UzJaLEVBd1MxWixFQXhTMFosQ0FBSCxFQUF6WixFQXdTTyxFQXhTUCxFQXdTVSxDQUFDLENBQUQsQ0F4U1YiLCJmaWxlIjoiY29tbW9uLmpzIiwic291cmNlc0NvbnRlbnQiOlsiKGZ1bmN0aW9uIGUodCxuLHIpe2Z1bmN0aW9uIHMobyx1KXtpZighbltvXSl7aWYoIXRbb10pe3ZhciBhPXR5cGVvZiByZXF1aXJlPT1cImZ1bmN0aW9uXCImJnJlcXVpcmU7aWYoIXUmJmEpcmV0dXJuIGEobywhMCk7aWYoaSlyZXR1cm4gaShvLCEwKTt0aHJvdyBuZXcgRXJyb3IoXCJDYW5ub3QgZmluZCBtb2R1bGUgJ1wiK28rXCInXCIpfXZhciBmPW5bb109e2V4cG9ydHM6e319O3Rbb11bMF0uY2FsbChmLmV4cG9ydHMsZnVuY3Rpb24oZSl7dmFyIG49dFtvXVsxXVtlXTtyZXR1cm4gcyhuP246ZSl9LGYsZi5leHBvcnRzLGUsdCxuLHIpfXJldHVybiBuW29dLmV4cG9ydHN9dmFyIGk9dHlwZW9mIHJlcXVpcmU9PVwiZnVuY3Rpb25cIiYmcmVxdWlyZTtmb3IodmFyIG89MDtvPHIubGVuZ3RoO28rKylzKHJbb10pO3JldHVybiBzfSkoezE6W2Z1bmN0aW9uKHJlcXVpcmUsbW9kdWxlLGV4cG9ydHMpe1xuXG5mdW5jdGlvbiBTbGlkZVBhZ2UgKHBhZ2UsIG9uU3RhcnQsIG9uVXBkYXRlLCBvbkNvbXBsZXRlLCBtYXhXaWR0aCwgbWF4SGVpZ2h0KSB7XG4gICAgdGhpcy5wYWdlICAgICAgID0gcGFnZTtcbiAgICB0aGlzLm9uU3RhcnQgICAgPSBvblN0YXJ0O1xuICAgIHRoaXMub25VcGRhdGUgICA9IG9uVXBkYXRlO1xuICAgIHRoaXMub25Db21wbGV0ZSA9IG9uQ29tcGxldGU7XG4gICAgdGhpcy5tYXhXaWR0aCAgID0gbWF4V2lkdGggfHwgJCh0aGlzLnBhZ2UpLndpZHRoKCk7XG4gICAgdGhpcy5tYXhIZWlnaHQgID0gbWF4SGVpZ2h0IHx8ICQodGhpcy5wYWdlKS5oZWlnaHQoKTtcbiAgICB0aGlzLmluaXQoKTtcbn1cblxuU2xpZGVQYWdlLnByb3RvdHlwZSA9IHtcbiAgICBpbml0IDogZnVuY3Rpb24oKSB7XG4gICAgICAgIHZhciBtZSA9IHRoaXMsIG1vdmVkID0gZmFsc2UsIHN0YXJ0UG9zLCBsYXN0UG9zO1xuICAgICAgICAkKHRoaXMucGFnZSkub24oXCJ0b3VjaHN0YXJ0XCIsZnVuY3Rpb24oZXZlbnQpe1xuICAgICAgICAgICAgc3RhcnRQb3MgPSB7eDogZXZlbnQuY2xpZW50WCwgeTogZXZlbnQuY2xpZW50WX07XG4gICAgICAgICAgICAkKG1lLnBhZ2UpLm9uKFwidG91Y2htb3ZlXCIsIGZ1bmN0aW9uKGV2ZW50KSB7XG4gICAgICAgICAgICAgICAgbWUub25VcGRhdGUoXG4gICAgICAgICAgICAgICAgICAgIChldmVudC5jbGllbnRYLXN0YXJ0UG9zLngpL21lLm1heFdpZHRoLFxuICAgICAgICAgICAgICAgICAgICAoZXZlbnQuY2xpZW50WS1zdGFydFBvcy55KS9tZS5tYXhIZWlnaHRcbiAgICAgICAgICAgICAgICApO1xuICAgICAgICAgICAgICAgIGxhc3RQb3MgPSB7eDogZXZlbnQuY2xpZW50WCwgeTogZXZlbnQuY2xpZW50WX07XG4gICAgICAgICAgICAgICAgaWYgKCFtb3ZlZCkge1xuICAgICAgICAgICAgICAgICAgICBtZS5vblN0YXJ0KCk7XG4gICAgICAgICAgICAgICAgfTtcbiAgICAgICAgICAgICAgICBtb3ZlZCA9IHRydWU7XG4gICAgICAgICAgICAgICAgZXZlbnQucHJldmVudERlZmF1bHQoKTtcbiAgICAgICAgICAgIH0pO1xuICAgICAgICAgICAgLy8gZXZlbnQucHJldmVudERlZmF1bHQoKTtcbiAgICAgICAgfSk7XG4gICAgICAgIGZ1bmN0aW9uIHVucmVnaXN0ZXIoZXZlbnQpIHtcbiAgICAgICAgICAgICQobWUucGFnZSkub2ZmKFwidG91Y2hlbmRcIik7XG4gICAgICAgICAgICBpZiAobW92ZWQpIHtcbiAgICAgICAgICAgICAgICBtZS5vbkNvbXBsZXRlKFxuICAgICAgICAgICAgICAgICAgICAobGFzdFBvcy54IC0gc3RhcnRQb3MueCkvbWUubWF4V2lkdGgsXG4gICAgICAgICAgICAgICAgICAgIChsYXN0UG9zLnkgLSBzdGFydFBvcy55KS9tZS5tYXhIZWlnaHRcbiAgICAgICAgICAgICAgICApO1xuICAgICAgICAgICAgICAgIG1vdmVkID0gZmFsc2U7XG4gICAgICAgICAgICB9O1xuICAgICAgICAgICAgLy8gZXZlbnQucHJldmVudERlZmF1bHQoKTtcbiAgICAgICAgfVxuICAgICAgICAkKHRoaXMucGFnZSkub24oXCJ0b3VjaGVuZFwiLCB1bnJlZ2lzdGVyKTtcbiAgICAgICAgJCh0aGlzLnBhZ2UpLm9uKFwidG91Y2hjYW5jZWxcIiwgdW5yZWdpc3Rlcik7XG4gICAgfVxufTtcblxuZnVuY3Rpb24gTWFrZUxvZyhuYW1lLCBhcmdzKSB7XG4gICAgaWYgKGFyZ3MpIHtcbiAgICAgICAgYXJncyArPSBcIiZ0PVwiICsgKG5ldyBEYXRlKCkuZ2V0VGltZSgpKTtcbiAgICB9IGVsc2Uge1xuICAgICAgICBhcmdzID0gXCJ0PVwiICsgKG5ldyBEYXRlKCkuZ2V0VGltZSgpKTtcbiAgICB9O1xuICAgICQoXCIjbG9nXCIpLmF0dHIoXCJzcmNcIiwgXCJodHRwOi8vXCIrd2luZG93LmxvY2F0aW9uLmhvc3QrXCIvbG9nL1wiK25hbWUrXCIuaHRtbD9cIiArIGFyZ3MpO1xufVxuXG52YXIgV2VpWGluU2hhcmUgPSBmdW5jdGlvbigpIHtcbiAgICB0aGlzLmltZyAgICA9IFwiXCI7XG4gICAgdGhpcy5saW5rICAgPSBcIlwiO1xuICAgIHRoaXMuZGVzYyAgID0gXCJcIjtcbiAgICB0aGlzLmFwcGlkICA9IFwiXCI7XG4gICAgdGhpcy50aXRsZSAgPSBcIlwiO1xuICAgIHRoaXMub25TaGFyZWQgPSBudWxsO1xufVxuV2VpWGluU2hhcmUucHJvdG90eXBlID0ge1xuICAgIGluaXQgOiBmdW5jdGlvbigpIHtcbiAgICAgICAgdmFyIG1lID0gdGhpcztcbiAgICAgICAgd3guY29uZmlnKHtcbiAgICAgICAgICAgIGRlYnVnOiBmYWxzZSwgLy8g5byA5ZCv6LCD6K+V5qih5byPLOiwg+eUqOeahOaJgOaciWFwaeeahOi/lOWbnuWAvOS8muWcqOWuouaIt+err2FsZXJ05Ye65p2l77yM6Iul6KaB5p+l55yL5Lyg5YWl55qE5Y+C5pWw77yM5Y+v5Lul5ZyocGPnq6/miZPlvIDvvIzlj4LmlbDkv6Hmga/kvJrpgJrov4dsb2fmiZPlh7rvvIzku4XlnKhwY+err+aXtuaJjeS8muaJk+WNsOOAglxuICAgICAgICAgICAgYXBwSWQ6IEFQUElELCAvLyDlv4XloavvvIzlhazkvJflj7fnmoTllK/kuIDmoIfor4ZcbiAgICAgICAgICAgIHRpbWVzdGFtcDogVElNRSwgLy8g5b+F5aGr77yM55Sf5oiQ562+5ZCN55qE5pe26Ze05oizXG4gICAgICAgICAgICBub25jZVN0cjogUkFORE9NLCAvLyDlv4XloavvvIznlJ/miJDnrb7lkI3nmoTpmo/mnLrkuLJcbiAgICAgICAgICAgIHNpZ25hdHVyZTogV1hUT0tFTiwvLyDlv4XloavvvIznrb7lkI3vvIzop4HpmYTlvZUxXG4gICAgICAgICAgICBqc0FwaUxpc3Q6IFtcIm9uTWVudVNoYXJlVGltZWxpbmVcIixcIm9uTWVudVNoYXJlQXBwTWVzc2FnZVwiLFwib25NZW51U2hhcmVRUVwiLFwib25NZW51U2hhcmVXZWlib1wiXSAvLyDlv4XloavvvIzpnIDopoHkvb/nlKjnmoRKU+aOpeWPo+WIl+ihqO+8jOaJgOaciUpT5o6l5Y+j5YiX6KGo6KeB6ZmE5b2VMlxuICAgICAgICB9KTtcbiAgICAgICAgd3gucmVhZHkoZnVuY3Rpb24oKXtcbiAgICAgICAgICAgIG1lLnJlZnJlc2goKTtcbiAgICAgICAgfSk7XG4gICAgICAgIHd4LmVycm9yKGZ1bmN0aW9uKHJlcyl7XG4gICAgICAgICAgICBhbGVydChKU09OLnN0cmluZ2lmeShyZXMpKTtcbiAgICAgICAgfSk7XG4gICAgfSxcblxuICAgIHJlZnJlc2g6IGZ1bmN0aW9uKCkge1xuICAgICAgICB0aGlzLnNoYXJlRnJpZW5kKCk7XG4gICAgICAgIHRoaXMuc2hhcmVUaW1lbGluZSgpO1xuICAgICAgICB0aGlzLnNoYXJlV2VpYm8oKTtcbiAgICAgICAgdGhpcy5zaGFyZVFRKCk7XG4gICAgfSxcblxuICAgIHNoYXJlRnJpZW5kIDogZnVuY3Rpb24oKSB7XG4gICAgICAgIHZhciBtZSA9IHRoaXM7XG4gICAgICAgIHd4Lm9uTWVudVNoYXJlQXBwTWVzc2FnZSh7XG4gICAgICAgICAgICB0aXRsZTogdGhpcy50aXRsZSwgLy8g5YiG5Lqr5qCH6aKYXG4gICAgICAgICAgICBkZXNjOiB0aGlzLmRlc2MsIC8vIOWIhuS6q+aPj+i/sFxuICAgICAgICAgICAgbGluazogdGhpcy5saW5rLCAvLyDliIbkuqvpk77mjqVcbiAgICAgICAgICAgIGltZ1VybDogdGhpcy5pbWcsIC8vIOWIhuS6q+Wbvuagh1xuICAgICAgICAgICAgdHlwZTogJ2xpbmsnLCAvLyDliIbkuqvnsbvlnossbXVzaWPjgIF2aWRlb+aIlmxpbmvvvIzkuI3loavpu5jorqTkuLpsaW5rXG4gICAgICAgICAgICBzdWNjZXNzOiBmdW5jdGlvbiAoKSB7IFxuICAgICAgICAgICAgICAgIC8vIOeUqOaIt+ehruiupOWIhuS6q+WQjuaJp+ihjOeahOWbnuiwg+WHveaVsFxuICAgICAgICAgICAgICAgIG1lLnNoYXJlZFN1Y2Nlc3NDYWxsQmFjaygpXG4gICAgICAgICAgICAgICAgbWUubWFrZUxvZyhcInR5cGU9ZnJpZW5kXCIpO1xuICAgICAgICAgICAgfSxcbiAgICAgICAgICAgIGNhbmNlbDogZnVuY3Rpb24gKCkgeyBcbiAgICAgICAgICAgICAgICAvLyDnlKjmiLflj5bmtojliIbkuqvlkI7miafooYznmoTlm57osIPlh73mlbBcbiAgICAgICAgICAgIH1cbiAgICAgICAgfSk7XG4gICAgfSxcbiAgICBzaGFyZVRpbWVsaW5lIDogZnVuY3Rpb24oKSB7XG4gICAgICAgIHZhciBtZSA9IHRoaXM7XG4gICAgICAgIHd4Lm9uTWVudVNoYXJlVGltZWxpbmUoe1xuICAgICAgICAgICAgdGl0bGU6IHRoaXMudGl0bGUsIC8vIOWIhuS6q+agh+mimFxuICAgICAgICAgICAgbGluazogdGhpcy5saW5rLCAvLyDliIbkuqvpk77mjqVcbiAgICAgICAgICAgIGltZ1VybDogdGhpcy5pbWcsIC8vIOWIhuS6q+Wbvuagh1xuICAgICAgICAgICAgc3VjY2VzczogZnVuY3Rpb24gKCkgeyBcbiAgICAgICAgICAgICAgICBtZS5zaGFyZWRTdWNjZXNzQ2FsbEJhY2soKVxuICAgICAgICAgICAgICAgIG1lLm1ha2VMb2coXCJ0eXBlPXRpbWVsaW5lXCIpO1xuICAgICAgICAgICAgfSxcbiAgICAgICAgICAgIGNhbmNlbDogZnVuY3Rpb24gKCkge1xuICAgICAgICAgICAgfVxuICAgICAgICB9KTtcbiAgICB9LFxuICAgIHNoYXJlV2VpYm8gOiBmdW5jdGlvbigpIHtcbiAgICAgICAgdmFyIG1lID0gdGhpcztcbiAgICAgICAgd3gub25NZW51U2hhcmVXZWlibyh7XG4gICAgICAgICAgICB0aXRsZTogdGhpcy50aXRsZSwgLy8g5YiG5Lqr5qCH6aKYXG4gICAgICAgICAgICBkZXNjOiB0aGlzLmRlc2MsIC8vIOWIhuS6q+aPj+i/sFxuICAgICAgICAgICAgbGluazogdGhpcy5saW5rLCAvLyDliIbkuqvpk77mjqVcbiAgICAgICAgICAgIGltZ1VybDogdGhpcy5pbWcsIC8vIOWIhuS6q+Wbvuagh1xuICAgICAgICAgICAgc3VjY2VzczogZnVuY3Rpb24gKCkgeyBcbiAgICAgICAgICAgICAgICBtZS5zaGFyZWRTdWNjZXNzQ2FsbEJhY2soKVxuICAgICAgICAgICAgICAgIG1lLm1ha2VMb2coXCJ0eXBlPXdlaWJvXCIpO1xuICAgICAgICAgICAgfSxcbiAgICAgICAgICAgIGNhbmNlbDogZnVuY3Rpb24gKCkgeyBcbiAgICAgICAgICAgICAgICAvLyDnlKjmiLflj5bmtojliIbkuqvlkI7miafooYznmoTlm57osIPlh73mlbBcbiAgICAgICAgICAgIH1cbiAgICAgICAgfSk7XG4gICAgfSxcbiAgICBzaGFyZVFROiBmdW5jdGlvbigpIHtcbiAgICAgICAgdmFyIG1lID0gdGhpcztcbiAgICAgICAgd3gub25NZW51U2hhcmVRUSh7XG4gICAgICAgICAgICB0aXRsZTogdGhpcy50aXRsZSwgLy8g5YiG5Lqr5qCH6aKYXG4gICAgICAgICAgICBkZXNjOiB0aGlzLmRlc2MsIC8vIOWIhuS6q+aPj+i/sFxuICAgICAgICAgICAgbGluazogdGhpcy5saW5rLCAvLyDliIbkuqvpk77mjqVcbiAgICAgICAgICAgIGltZ1VybDogdGhpcy5pbWcsIC8vIOWIhuS6q+Wbvuagh1xuICAgICAgICAgICAgc3VjY2VzczogZnVuY3Rpb24gKCkgeyAgXG4gICAgICAgICAgICAgICAgbWUuc2hhcmVkU3VjY2Vzc0NhbGxCYWNrKClcbiAgICAgICAgICAgICAgICBtZS5tYWtlTG9nKFwidHlwZT1xcVwiKTtcbiAgICAgICAgICAgIH0sXG4gICAgICAgICAgICBjYW5jZWw6IGZ1bmN0aW9uICgpIHsgXG4gICAgICAgICAgICAgICAvLyDnlKjmiLflj5bmtojliIbkuqvlkI7miafooYznmoTlm57osIPlh73mlbBcbiAgICAgICAgICAgIH1cbiAgICAgICAgfSk7XG4gICAgfSxcbiAgICBtYWtlTG9nIDogZnVuY3Rpb24gKGFyZ3MpIHtcbiAgICAgICAgTWFrZUxvZyhcInNoYXJlXCIsYXJncyk7XG4gICAgfSxcbiAgICBzaGFyZWRTdWNjZXNzQ2FsbEJhY2s6IGZ1bmN0aW9uKCkge1xuICAgICAgICBpZiAodGhpcy5vblNoYXJlZCkge1xuICAgICAgICAgICAgdGhpcy5vblNoYXJlZCgpO1xuICAgICAgICB9O1xuICAgIH1cbn07XG4kKGRvY3VtZW50KS5vbihcIldlaXhpbkpTQnJpZGdlUmVhZHlcIiwgZnVuY3Rpb24oKXtcbiAgICAkKGRvY3VtZW50KS5vZmYoXCJXZWl4aW5KU0JyaWRnZVJlYWR5XCIpO1xufSk7XG5cbndpbmRvdy53ZWl4aW5TaGFyZSA9IG5ldyBXZWlYaW5TaGFyZSgpO1xuXG53aW5kb3cuQ0FMTF9JTklUX1dFSVhJTl9TSEFSRSA9IGZ1bmN0aW9uKCkge1xuICAgIHdlaXhpblNoYXJlLmltZyBcdD0gd2luZG93LmxvY2F0aW9uLm9yaWdpbitcIi9zaGFyZS9pbWcvc2hhcmUtaWNvbi5qcGdcIjtcbiAgICB3ZWl4aW5TaGFyZS5saW5rIFx0PSB3aW5kb3cubG9jYXRpb24uaHJlZjtcbiAgICB3ZWl4aW5TaGFyZS50aXRsZSAgID0gXCLkvJjom4tcIjtcbiAgICB3ZWl4aW5TaGFyZS5kZXNjICAgID0gXCLkvJjom4tcIjtcbiAgICB3ZWl4aW5TaGFyZS5pbml0KCk7XG59XG52YXIgdWEgXHRcdCAgPSB3aW5kb3cubmF2aWdhdG9yLnVzZXJBZ2VudDtcbnZhciBpc1dlaXhpbiAgPSB1YS50b0xvd2VyQ2FzZSgpLm1hdGNoKC9NaWNyb01lc3Nlbmdlci9pKSA9PSAnbWljcm9tZXNzZW5nZXInO1xuaWYgKGlzV2VpeGluKSB7XG4gICAgdmFyIGhtID0gZG9jdW1lbnQuY3JlYXRlRWxlbWVudChcInNjcmlwdFwiKTtcbiAgICBobS5zcmMgPSBcIi9wcmVzZWxsL3NoYXJlLz9mPVwiICsgZW5jb2RlVVJJQ29tcG9uZW50KHdpbmRvdy5sb2NhdGlvbi5ocmVmKSArIFwiJnQ9XCIgKyBuZXcgRGF0ZSgpLmdldFRpbWUoKTtcbiAgICB2YXIgcyA9IGRvY3VtZW50LmdldEVsZW1lbnRzQnlUYWdOYW1lKFwic2NyaXB0XCIpWzBdOyBcbiAgICBzLnBhcmVudE5vZGUuaW5zZXJ0QmVmb3JlKGhtLCBzKTtcbn1cblxuZnVuY3Rpb24gRm9ybWF0KHN0ciwgZGF0YSkge1xuICAgIHJldHVybiBzdHIucmVwbGFjZSgvXFwjXFx7KFxcZCspXFx9L2csIGZ1bmN0aW9uKG0sIGQpe1xuICAgICAgICByZXR1cm4gZGF0YVtkXTtcbiAgICB9KTtcbn1cblxuZnVuY3Rpb24gQmluZCAoZnVuYywgYXJnMSwgYXJnMiwgYXJnMywgYXJnNCkge1xuICAgIHJldHVybiBmdW5jdGlvbigpe1xuICAgICAgICBmdW5jKGFyZzEsIGFyZzIsIGFyZzMsIGFyZzQpO1xuICAgIH1cbn1cblxuLyoqXG4gKiBcbiAqIEBwYXJhbSBtdXNpY2J0biBbc3RyaW5nXSDkvKDlhaXpnIDopoHnu5HlrprnmoTmkq3mlL7mjInpkq5cbiAqIEBwYXJhbSBhdWRpbyBbc3RyaW5nXSDkvKDlhaXpnIDopoHmkq3mlL7nmoRhdWRpb+eahGlkXG4gKi9cbmZ1bmN0aW9uIFBsYXllcihtdXNpY2J0biwgYXVkaW8sIGhhbmRsZXJzKSB7XG4gICAgdGhpcy5fYnRuICAgICAgID0gJChtdXNpY2J0bik7XG4gICAgdGhpcy5fYXVkaW8gICAgID0gJChhdWRpbylbMF07XG4gICAgdGhpcy5fcGxheWluZyAgID0gZmFsc2U7XG4gICAgdGhpcy5faGFuZGxlcnMgID0gaGFuZGxlcnMgfHwge307XG4gICAgdGhpcy5iaW5kKCk7XG59XG5cblBsYXllci5wcm90b3R5cGUgPSB7XG4gICAgX193ZWNoYXQ6IGZ1bmN0aW9uKCkge1xuICAgICAgICB2YXIgdXNlckFnZW50ID0gbmF2aWdhdG9yLnVzZXJBZ2VudDtcbiAgICAgICAgdmFyIHVhID0gdXNlckFnZW50LnRvTG93ZXJDYXNlKCk7XG4gICAgICAgIGlmKHVhLm1hdGNoKC9NaWNyb01lc3Nlbmdlci9pKSA9PSAnbWljcm9tZXNzZW5nZXInKXtcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xuICAgICAgICB9XG4gICAgfSxcbiAgICBfX29ucGxheTogZnVuY3Rpb24oKSB7XG4gICAgICAgIHRoaXMuX2J0bi5hZGRDbGFzcyhcIm9uXCIpO1xuICAgICAgICB0aGlzLl9wbGF5aW5nID0gdHJ1ZTtcbiAgICAgICAgaWYgKHRoaXMuX2hhbmRsZXJzLm9ucGxheSkge1xuICAgICAgICAgICAgdGhpcy5faGFuZGxlcnMub25wbGF5KCk7XG4gICAgICAgIH1cbiAgICB9LFxuICAgIF9fb25wYXVzZTogZnVuY3Rpb24oKSB7XG4gICAgICAgIHRoaXMuX2J0bi5yZW1vdmVDbGFzcyhcIm9uXCIpO1xuICAgICAgICB0aGlzLl9wbGF5aW5nID0gZmFsc2U7XG4gICAgICAgIGlmICh0aGlzLl9oYW5kbGVycy5vbnBhdXNlKSB7XG4gICAgICAgICAgICB0aGlzLl9oYW5kbGVycy5vbnBhdXNlKCk7XG4gICAgICAgIH1cbiAgICB9LFxuICAgIHBsYXk6IGZ1bmN0aW9uKHVybCwgaGFuZGxlcnMpIHtcbiAgICAgICAgdGhpcy5faGFuZGxlcnMgPSBoYW5kbGVycztcbiAgICAgICAgaWYgKHRoaXMuX3JlYWR5KSB7XG4gICAgICAgICAgICB0aGlzLl9hdWRpby5zcmMgPSB1cmw7XG4gICAgICAgICAgICB0aGlzLl9hdWRpby5wbGF5KCk7XG4gICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgICB0aGlzLl9uZWVkX3BsYXkgPSB1cmw7XG4gICAgICAgIH1cbiAgICB9LFxuICAgIHJlc3VtZTogZnVuY3Rpb24oKSB7XG4gICAgICAgIHRoaXMuX2F1ZGlvLnBsYXkoKTtcbiAgICB9LFxuICAgIHBhdXNlOiBmdW5jdGlvbigpIHtcbiAgICAgICAgdGhpcy5fYXVkaW8ucGF1c2UoKTtcbiAgICB9LFxuICAgIGJpbmQ6IGZ1bmN0aW9uKCkge1xuICAgICAgICB2YXIgbWUgPSB0aGlzO1xuICAgICAgICAkKHRoaXMuX2F1ZGlvKS5vbihcInBsYXlcIiwgZnVuY3Rpb24oKSB7XG4gICAgICAgICAgICBtZS5fX29ucGxheSgpO1xuICAgICAgICB9KTtcbiAgICAgICAgJCh0aGlzLl9hdWRpbykub24oXCJwYXVzZVwiLCBmdW5jdGlvbigpIHtcbiAgICAgICAgICAgIG1lLl9fb25wYXVzZSgpO1xuICAgICAgICB9KTtcbiAgICAgICAgJCh0aGlzLl9hdWRpbykub24oXCJ0aW1ldXBkYXRlXCIsIGZ1bmN0aW9uKCl7XG4gICAgICAgICAgICB2YXIgY3VycmVudCA9IG1lLl9hdWRpby5jdXJyZW50VGltZTtcbiAgICAgICAgICAgIGlmIChtZS5faGFuZGxlcnMub251cGRhdGUpIHtcbiAgICAgICAgICAgICAgICBtZS5faGFuZGxlcnMub251cGRhdGUoY3VycmVudCk7XG4gICAgICAgICAgICB9XG4gICAgICAgIH0pO1xuICAgICAgICB0aGlzLl9idG4ub24oXCJjbGlja1wiLCBmdW5jdGlvbihldmVudCl7XG4gICAgICAgICAgICBldmVudC5wcmV2ZW50RGVmYXVsdCgpO1xuICAgICAgICAgICAgaWYgKG1lLl9wbGF5aW5nKSB7XG4gICAgICAgICAgICAgICAgbWUuX2F1ZGlvLnBhdXNlKCk7XG4gICAgICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgICAgICAgIG1lLl9hdWRpby5wbGF5KCk7XG4gICAgICAgICAgICB9XG4gICAgICAgIH0pO1xuICAgICAgICBpZiAodGhpcy5fX3dlY2hhdCgpKSB7XG4gICAgICAgICAgICBmdW5jdGlvbiBPblJlYWR5KCkge1xuICAgICAgICAgICAgICAgIGlmIChtZS5fbmVlZF9wbGF5KSB7XG4gICAgICAgICAgICAgICAgICAgIG1lLl9hdWRpby5zcmMgPSBtZS5fbmVlZF9wbGF5O1xuICAgICAgICAgICAgICAgICAgICBtZS5fbmVlZF9wbGF5ID0gZmFsc2U7XG4gICAgICAgICAgICAgICAgfVxuICAgICAgICAgICAgICAgICQoZG9jdW1lbnQpLm9mZihcIldlaXhpbkpTQnJpZGdlUmVhZHlcIik7XG4gICAgICAgICAgICAgICAgbWUuX3JlYWR5ID0gdHJ1ZTtcbiAgICAgICAgICAgICAgICBpZiAobWUuX2hhbmRsZXJzLm9ucmVhZHkpIHtcbiAgICAgICAgICAgICAgICAgICAgbWUuX2hhbmRsZXJzLm9ucmVhZHkoKTtcbiAgICAgICAgICAgICAgICB9XG4gICAgICAgICAgICB9XG4gICAgICAgICAgICBpZiAoIXdpbmRvdy5Kc0JyaWRnZVJlYWR5KSB7XG4gICAgICAgICAgICAgICAgZG9jdW1lbnQuYWRkRXZlbnRMaXN0ZW5lcihcIldlaXhpbkpTQnJpZGdlUmVhZHlcIiwgT25SZWFkeSk7XG4gICAgICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgICAgICAgIE9uUmVhZHkoKTtcbiAgICAgICAgICAgIH1cbiAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICAgIHRoaXMuX3JlYWR5ID0gdHJ1ZTtcbiAgICAgICAgICAgIGlmIChtZS5faGFuZGxlcnMub25yZWFkeSkge1xuICAgICAgICAgICAgICAgIG1lLl9oYW5kbGVycy5vbnJlYWR5KCk7XG4gICAgICAgICAgICB9XG4gICAgICAgIH1cbiAgICB9XG59O1xuXG53aW5kb3cuUGxheWVyID0gUGxheWVyO1xufSx7fV19LHt9LFsxXSkiXX0=
