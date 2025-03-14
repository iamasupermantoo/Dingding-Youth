(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);throw new Error("Cannot find module '"+o+"'")}var f=n[o]={exports:{}};t[o][0].call(f.exports,function(e){var n=t[o][1][e];return s(n?n:e)},f,f.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
"use strict";

/**
 * @param page[string] 传入需要绑定滑动事件的元素
 * @param handlers[object] {
 *  start:   [function] 开始滑动时的回调函数
 *  update:  [function] 滑动时的回调函数，会传入当前滑动位移的百分比[1-100]
 *  complete:[function] 滑动结束时的回调函数，会传入本次滑动手势为向上还是向下[isup:true or false] 
 *  cancel:  [function] 滑动取消时的回调函数，无参数传入
 * }
 */
function SlidePage(page, handlers) {
    this._page = $(page);
    this._handlers = {
        start: handlers.start || $.noop,
        update: handlers.update || $.noop,
        complete: handlers.complete || $.noop,
        cancel: handlers.cancel || $.noop
    };
    this._max = this._page.height();
    this._threshold = 0.15;
    this.bind();
}

SlidePage.prototype = {
    bind: function bind() {
        var me = this,
            moved = false,
            start,
            last;
        this._page.on("touchstart", function (event) {
            start = event.originalEvent.touches[0].clientY;
            $(me._page).on("touchmove", function (event) {
                last = event.originalEvent.touches[0].clientY;
                if (!moved) {
                    me._handlers.start(start);
                };
                me._handlers.update(-(last - start) / me._max * 100);
                moved = true;
            });
            event.preventDefault();
        });
        function unregister(event) {
            $(me._page).off("touchmove");
            if (moved) {
                var _delta = last - start;
                if (Math.abs(_delta) > me._max * me._threshold) {
                    me._handlers.complete(_delta < 0);
                } else {
                    me._handlers.cancel();
                }
                moved = false;
            } else {
                $(event.target).trigger("click");
            }
        }
        this._page.on("touchend", unregister);
        this._page.on("touchcancel", unregister);
    }
};

window.SlidePage = SlidePage;
/**
 * @param assets[array] 传入需要加载的图片资源
 * @param handlers[object] {
 *  start:   [function] 开始加载时的回调函数
 *  update:  [function] 加载过程中的回调函数，会传入当前滑动位移的百分比[1-100]
 *  complete:[function] 加载结束时的回调函数
 * }
 */
function Preloader(assets, handlers) {
    this._assets = assets;
    //加载进度条最短需要的时间
    this._duration = 0;
    this._handlers = {
        start: handlers.start || $.noop,
        update: handlers.update || $.noop,
        complete: handlers.complete || $.noop
    };
}

Preloader.prototype = {
    load: function load() {
        var loaded = 0,
            assets = this._assets,
            me = this,
            start = new Date().getTime();
        this._handlers.start();
        $.imgpreload(this._assets, {
            each: function each() {
                var status = $(this).data('loaded') ? 'success' : 'error';
                if (status == "success") {
                    var v = (parseFloat(++loaded) / assets.length).toFixed(2);
                    me._handlers.update(v * 60);
                }
            },
            all: function all() {
                var now = new Date().getTime();
                var delta = now - start;
                var duration = me._duration;
                if (delta > duration) {
                    me._handlers.update(100);
                    me._handlers.complete();
                } else {
                    var _start = new Date().getTime();
                    var inter = setInterval(function () {
                        var time = new Date().getTime();
                        var percent = Math.floor((time - _start) / duration * 40 + 60);
                        percent = Math.min(percent, 100);
                        me._handlers.update(percent);
                    }, 200);
                    setTimeout(function () {
                        clearInterval(inter);
                        me._handlers.update(100);
                        me._handlers.complete();
                    }, duration - delta);
                };
            }
        });
    }
};

window.Preloader = Preloader;
/**
 * 
 * @param musicbtn [string] 传入需要绑定的播放按钮
 * @param audio [string] 传入需要播放的audio的id
 */
function Player(musicbtn, audio) {
    this._btn = $(musicbtn);
    this._audio = $(audio)[0];
    this._playing = false;
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
    },
    __onpause: function __onpause() {
        this._btn.removeClass("on");
        this._playing = false;
    },
    stop: function stop() {
        this._audio.pause();
    },
    resume: function resume() {
        this._audio.play();
    },
    bind: function bind() {
        var me = this;
        $(this._audio).on("play", function () {
            me.__onplay();
        });
        $(this._audio).on("pause", function () {
            me.__onpause();
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
            $(document).on("WeixinJSBridgeReady", function () {
                me._audio.play();
                $(document).off("WeixinJSBridgeReady");
            });
        } else {
            this._audio.play();
        }
    }
};

window.Player = Player;

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
            jsApiList: ["onMenuShareTimeline", "onMenuShareAppMessage", "onMenuShareQQ", "onMenuShareWeibo", "startRecord", "stopRecord", "onVoiceRecordEnd", "translateVoice"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
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
            title: this.desc, // 分享标题
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

window.weixinShare = new WeiXinShare();

window.CALL_INIT_WEIXIN_SHARE = function () {
    weixinShare.img = window.location.origin + "/pre_sell_share/img/share-icon.jpg";
    weixinShare.link = SHARELINK;
    weixinShare.title = "《家庭教育》专家年课，限时抢购限量300名！";
    weixinShare.desc = "限时抢购！【家庭教育】课在家上，专家全年每日教学，一天不足一块钱。";
    weixinShare.init();
};
var hm = document.createElement("script");
hm.src = "/presell/share?f=" + encodeURIComponent(window.location.href) + "&t=" + new Date().getTime();
var s = document.getElementsByTagName("script")[0];
s.parentNode.insertBefore(hm, s);
},{}]},{},[1])
//# sourceMappingURL=data:application/json;charset=utf8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbImNvbW1vbi5qcyJdLCJuYW1lcyI6WyJTbGlkZVBhZ2UiLCJwYWdlIiwiaGFuZGxlcnMiLCJfcGFnZSIsIiQiLCJfaGFuZGxlcnMiLCJzdGFydCIsIm5vb3AiLCJ1cGRhdGUiLCJjb21wbGV0ZSIsImNhbmNlbCIsIl9tYXgiLCJoZWlnaHQiLCJfdGhyZXNob2xkIiwiYmluZCIsInByb3RvdHlwZSIsIm1lIiwibW92ZWQiLCJsYXN0Iiwib24iLCJldmVudCIsIm9yaWdpbmFsRXZlbnQiLCJ0b3VjaGVzIiwiY2xpZW50WSIsInByZXZlbnREZWZhdWx0IiwidW5yZWdpc3RlciIsIm9mZiIsIl9kZWx0YSIsIk1hdGgiLCJhYnMiLCJ0YXJnZXQiLCJ0cmlnZ2VyIiwid2luZG93IiwiUHJlbG9hZGVyIiwiYXNzZXRzIiwiX2Fzc2V0cyIsIl9kdXJhdGlvbiIsImxvYWQiLCJsb2FkZWQiLCJEYXRlIiwiZ2V0VGltZSIsImltZ3ByZWxvYWQiLCJlYWNoIiwic3RhdHVzIiwiZGF0YSIsInYiLCJwYXJzZUZsb2F0IiwibGVuZ3RoIiwidG9GaXhlZCIsImFsbCIsIm5vdyIsImRlbHRhIiwiZHVyYXRpb24iLCJfc3RhcnQiLCJpbnRlciIsInNldEludGVydmFsIiwidGltZSIsInBlcmNlbnQiLCJmbG9vciIsIm1pbiIsInNldFRpbWVvdXQiLCJjbGVhckludGVydmFsIiwiUGxheWVyIiwibXVzaWNidG4iLCJhdWRpbyIsIl9idG4iLCJfYXVkaW8iLCJfcGxheWluZyIsIl9fd2VjaGF0IiwidXNlckFnZW50IiwibmF2aWdhdG9yIiwidWEiLCJ0b0xvd2VyQ2FzZSIsIm1hdGNoIiwiX19vbnBsYXkiLCJhZGRDbGFzcyIsIl9fb25wYXVzZSIsInJlbW92ZUNsYXNzIiwic3RvcCIsInBhdXNlIiwicmVzdW1lIiwicGxheSIsImRvY3VtZW50IiwiV2VpWGluU2hhcmUiLCJpbWciLCJsaW5rIiwiZGVzYyIsImFwcGlkIiwidGl0bGUiLCJvblNoYXJlZCIsImluaXQiLCJ3eCIsImNvbmZpZyIsImRlYnVnIiwiYXBwSWQiLCJBUFBJRCIsInRpbWVzdGFtcCIsIlRJTUUiLCJub25jZVN0ciIsIlJBTkRPTSIsInNpZ25hdHVyZSIsIldYVE9LRU4iLCJqc0FwaUxpc3QiLCJyZWFkeSIsInJlZnJlc2giLCJlcnJvciIsInJlcyIsImFsZXJ0IiwiSlNPTiIsInN0cmluZ2lmeSIsInNoYXJlRnJpZW5kIiwic2hhcmVUaW1lbGluZSIsInNoYXJlV2VpYm8iLCJzaGFyZVFRIiwib25NZW51U2hhcmVBcHBNZXNzYWdlIiwiaW1nVXJsIiwidHlwZSIsInN1Y2Nlc3MiLCJzaGFyZWRTdWNjZXNzQ2FsbEJhY2siLCJtYWtlTG9nIiwib25NZW51U2hhcmVUaW1lbGluZSIsIm9uTWVudVNoYXJlV2VpYm8iLCJvbk1lbnVTaGFyZVFRIiwiYXJncyIsIk1ha2VMb2ciLCJ3ZWl4aW5TaGFyZSIsIkNBTExfSU5JVF9XRUlYSU5fU0hBUkUiLCJsb2NhdGlvbiIsIm9yaWdpbiIsIlNIQVJFTElOSyIsImhtIiwiY3JlYXRlRWxlbWVudCIsInNyYyIsImVuY29kZVVSSUNvbXBvbmVudCIsImhyZWYiLCJzIiwiZ2V0RWxlbWVudHNCeVRhZ05hbWUiLCJwYXJlbnROb2RlIiwiaW5zZXJ0QmVmb3JlIl0sIm1hcHBpbmdzIjoiOztBQUFBOzs7Ozs7Ozs7QUFTQSxTQUFTQSxTQUFULENBQW9CQyxJQUFwQixFQUEwQkMsUUFBMUIsRUFBb0M7QUFDaEMsU0FBS0MsS0FBTCxHQUFrQkMsRUFBRUgsSUFBRixDQUFsQjtBQUNBLFNBQUtJLFNBQUwsR0FBa0I7QUFDZEMsZUFBY0osU0FBU0ksS0FBVCxJQUFxQkYsRUFBRUcsSUFEdkI7QUFFZEMsZ0JBQWNOLFNBQVNNLE1BQVQsSUFBcUJKLEVBQUVHLElBRnZCO0FBR2RFLGtCQUFjUCxTQUFTTyxRQUFULElBQXFCTCxFQUFFRyxJQUh2QjtBQUlkRyxnQkFBY1IsU0FBU1EsTUFBVCxJQUFxQk4sRUFBRUc7QUFKdkIsS0FBbEI7QUFNQSxTQUFLSSxJQUFMLEdBQWtCLEtBQUtSLEtB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VixFQUFnQjtBQUN0QkMsZ0JBQVEsT0FBUixFQUFnQkQsSUFBaEI7QUFDSCxLQTVGbUI7QUE2RnBCTCwyQkFBdUIsaUNBQVc7QUFDOUIsWUFBSSxLQUFLN0IsUUFBVCxFQUFtQjtBQUNmLGlCQUFLQSxRQUFMO0FBQ0g7QUFDSjtBQWpHbUIsQ0FBeEI7O0FBb0dBekQsT0FBTzZGLFdBQVAsR0FBcUIsSUFBSTFDLFdBQUosRUFBckI7O0FBRUFuRCxPQUFPOEYsc0JBQVAsR0FBZ0MsWUFBVztBQUN2Q0QsZ0JBQVl6QyxHQUFaLEdBQW1CcEQsT0FBTytGLFFBQVAsQ0FBZ0JDLE1BQWhCLEdBQXlCLHFCQUE1QztBQUNBSCxnQkFBWXhDLElBQVosR0FBb0I0QyxTQUFwQjtBQUNBSixnQkFBWXJDLEtBQVosR0FBc0Isd0JBQXRCO0FBQ0FxQyxnQkFBWXZDLElBQVosR0FBc0IsbUNBQXRCO0FBQ0F1QyxnQkFBWW5DLElBQVo7QUFDSCxDQU5EO0FBT0EsSUFBSXdDLEtBQUtoRCxTQUFTaUQsYUFBVCxDQUF1QixRQUF2QixDQUFUO0FBQ0FELEdBQUdFLEdBQUgsR0FBUyx5Q0FBeUNDLG1CQUFtQnJHLE9BQU8rRixRQUFQLENBQWdCTyxJQUFuQyxDQUF6QyxHQUFvRixLQUFwRixHQUE0RixJQUFJL0YsSUFBSixHQUFXQyxPQUFYLEVBQXJHO0FBQ0EsSUFBSStGLElBQUlyRCxTQUFTc0Qsb0JBQVQsQ0FBOEIsUUFBOUIsRUFBd0MsQ0FBeEMsQ0FBUjtBQUNBRCxFQUFFRSxVQUFGLENBQWFDLFlBQWIsQ0FBMEJSLEVBQTFCLEVBQThCSyxDQUE5QiIsImZpbGUiOiJjb21tb24uanMiLCJzb3VyY2VzQ29udGVudCI6WyIvKipcbiAqIEBwYXJhbSBwYWdlW3N0cmluZ10g5Lyg5YWl6ZyA6KaB57uR5a6a5ruR5Yqo5LqL5Lu255qE5YWD57SgXG4gKiBAcGFyYW0gaGFuZGxlcnNbb2JqZWN0XSB7XG4gKiAgc3RhcnQ6ICAgW2Z1bmN0aW9uXSDlvIDlp4vmu5Hliqjml7bnmoTlm57osIPlh73mlbBcbiAqICB1cGRhdGU6ICBbZnVuY3Rpb25dIOa7keWKqOaXtueahOWbnuiwg+WHveaVsO+8jOS8muS8oOWFpeW9k+WJjea7keWKqOS9jeenu+eahOeZvuWIhuavlFsxLTEwMF1cbiAqICBjb21wbGV0ZTpbZnVuY3Rpb25dIOa7keWKqOe7k+adn+aXtueahOWbnuiwg+WHveaVsO+8jOS8muS8oOWFpeacrOasoea7keWKqOaJi+WKv+S4uuWQkeS4iui/mOaYr+WQkeS4i1tpc3VwOnRydWUgb3IgZmFsc2VdIFxuICogIGNhbmNlbDogIFtmdW5jdGlvbl0g5ruR5Yqo5Y+W5raI5pe255qE5Zue6LCD5Ye95pWw77yM5peg5Y+C5pWw5Lyg5YWlXG4gKiB9XG4gKi9cbmZ1bmN0aW9uIFNsaWRlUGFnZSAocGFnZSwgaGFuZGxlcnMpIHtcbiAgICB0aGlzLl9wYWdlICAgICAgPSAkKHBhZ2UpO1xuICAgIHRoaXMuX2hhbmRsZXJzICA9IHtcbiAgICAgICAgc3RhcnQgICAgICAgOiBoYW5kbGVycy5zdGFydCAgICB8fCAkLm5vb3AsXG4gICAgICAgIHVwZGF0ZSAgICAgIDogaGFuZGxlcnMudXBkYXRlICAgfHwgJC5ub29wLFxuICAgICAgICBjb21wbGV0ZSAgICA6IGhhbmRsZXJzLmNvbXBsZXRlIHx8ICQubm9vcCxcbiAgICAgICAgY2FuY2VsICAgICAgOiBoYW5kbGVycy5jYW5jZWwgICB8fCAkLm5vb3BcbiAgICB9O1xuICAgIHRoaXMuX21heCAgICAgICA9IHRoaXMuX3BhZ2UuaGVpZ2h0KCk7XG4gICAgdGhpcy5fdGhyZXNob2xkID0gMC4xNTtcbiAgICB0aGlzLmJpbmQoKTtcbn1cblxuU2xpZGVQYWdlLnByb3RvdHlwZSA9IHtcbiAgICBiaW5kIDogZnVuY3Rpb24oKSB7XG4gICAgICAgIHZhciBtZSA9IHRoaXMsIG1vdmVkID0gZmFsc2UsIHN0YXJ0LCBsYXN0O1xuICAgICAgICB0aGlzLl9wYWdlLm9uKFwidG91Y2hzdGFydFwiLGZ1bmN0aW9uKGV2ZW50KXtcbiAgICAgICAgICAgIHN0YXJ0ID0gZXZlbnQub3JpZ2luYWxFdmVudC50b3VjaGVzWzBdLmNsaWVudFk7XG4gICAgICAgICAgICAkKG1lLl9wYWdlKS5vbihcInRvdWNobW92ZVwiLCBmdW5jdGlvbihldmVudCkge1xuICAgICAgICAgICAgICAgIGxhc3QgPSBldmVudC5vcmlnaW5hbEV2ZW50LnRvdWNoZXNbMF0uY2xpZW50WTtcbiAgICAgICAgICAgICAgICBpZiAoIW1vdmVkKSB7XG4gICAgICAgICAgICAgICAgICAgIG1lLl9oYW5kbGVycy5zdGFydChzdGFydCk7XG4gICAgICAgICAgICAgICAgfTtcbiAgICAgICAgICAgICAgICBtZS5faGFuZGxlcnMudXBkYXRlKFxuICAgICAgICAgICAgICAgICAgICAtKGxhc3QgLSBzdGFydCkvbWUuX21heCAqIDEwMFxuICAgICAgICAgICAgICAgICk7XG4gICAgICAgICAgICAgICAgbW92ZWQgPSB0cnVlO1xuICAgICAgICAgICAgfSk7XG4gICAgICAgICAgICBldmVudC5wcmV2ZW50RGVmYXVsdCgpO1xuICAgICAgICB9KTtcbiAgICAgICAgZnVuY3Rpb24gdW5yZWdpc3RlcihldmVudCkge1xuICAgICAgICAgICAgJChtZS5fcGFnZSkub2ZmKFwidG91Y2htb3ZlXCIpO1xuICAgICAgICAgICAgaWYgKG1vdmVkKSB7XG4gICAgICAgICAgICAgICAgdmFyIF9kZWx0YSA9IGxhc3QgLSBzdGFydDtcbiAgICAgICAgICAgICAgICBpZiAoTWF0aC5hYnMoX2RlbHRhKSA+IG1lLl9tYXgqbWUuX3RocmVzaG9sZCkge1xuICAgICAgICAgICAgICAgICAgICBtZS5faGFuZGxlcnMuY29tcGxldGUoX2RlbHRhIDwgMCk7XG4gICAgICAgICAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICAgICAgICAgICAgbWUuX2hhbmRsZXJzLmNhbmNlbCgpO1xuICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgICAgICBtb3ZlZCA9IGZhbHNlO1xuICAgICAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICAgICAgICAkKGV2ZW50LnRhcmdldCkudHJpZ2dlcihcImNsaWNrXCIpO1xuICAgICAgICAgICAgfVxuICAgICAgICB9XG4gICAgICAgIHRoaXMuX3BhZ2Uub24oXCJ0b3VjaGVuZFwiLCAgICB1bnJlZ2lzdGVyKTtcbiAgICAgICAgdGhpcy5fcGFnZS5vbihcInRvdWNoY2FuY2VsXCIsIHVucmVnaXN0ZXIpO1xuICAgIH1cbn07XG5cbndpbmRvdy5TbGlkZVBhZ2UgPSBTbGlkZVBhZ2U7XG4vKipcbiAqIEBwYXJhbSBhc3NldHNbYXJyYXldIOS8oOWFpemcgOimgeWKoOi9veeahOWbvueJh+i1hOa6kFxuICogQHBhcmFtIGhhbmRsZXJzW29iamVjdF0ge1xuICogIHN0YXJ0OiAgIFtmdW5jdGlvbl0g5byA5aeL5Yqg6L295pe255qE5Zue6LCD5Ye95pWwXG4gKiAgdXBkYXRlOiAgW2Z1bmN0aW9uXSDliqDovb3ov4fnqIvkuK3nmoTlm57osIPlh73mlbDvvIzkvJrkvKDlhaXlvZPliY3mu5HliqjkvY3np7vnmoTnmb7liIbmr5RbMS0xMDBdXG4gKiAgY29tcGxldGU6W2Z1bmN0aW9uXSDliqDovb3nu5PmnZ/ml7bnmoTlm57osIPlh73mlbBcbiAqIH1cbiAqL1xuZnVuY3Rpb24gUHJlbG9hZGVyKGFzc2V0cywgaGFuZGxlcnMpIHtcbiAgICB0aGlzLl9hc3NldHMgICAgPSBhc3NldHM7XG4gICAgLy/liqDovb3ov5vluqbmnaHmnIDnn63pnIDopoHnmoTml7bpl7RcbiAgICB0aGlzLl9kdXJhdGlvbiAgPSAwO1xuICAgIHRoaXMuX2hhbmRsZXJzICA9IHtcbiAgICAgICAgc3RhcnQgICAgICAgOiBoYW5kbGVycy5zdGFydCAgICB8fCAkLm5vb3AsXG4gICAgICAgIHVwZGF0ZSAgICAgIDogaGFuZGxlcnMudXBkYXRlICAgfHwgJC5ub29wLFxuICAgICAgICBjb21wbGV0ZSAgICA6IGhhbmRsZXJzLmNvbXBsZXRlIHx8ICQubm9vcFxuICAgIH07XG59XG5cblByZWxvYWRlci5wcm90b3R5cGUgPSB7XG4gICAgbG9hZDogZnVuY3Rpb24oKSB7XG4gICAgICAgIHZhciBsb2FkZWQgPSAwLCBhc3NldHMgPSB0aGlzLl9hc3NldHMsIG1lID0gdGhpcyxcbiAgICAgICAgICAgIHN0YXJ0ICA9IG5ldyBEYXRlKCkuZ2V0VGltZSgpO1xuICAgICAgICB0aGlzLl9oYW5kbGVycy5zdGFydCgpO1xuICAgICAgICAkLmltZ3ByZWxvYWQodGhpcy5fYXNzZXRzLCB7XG4gICAgICAgICAgICBlYWNoOiBmdW5jdGlvbiAoKSB7XG4gICAgICAgICAgICAgICAgdmFyIHN0YXR1cyA9ICQodGhpcykuZGF0YSgnbG9hZGVkJykgPyAnc3VjY2VzcycgOiAnZXJyb3InO1xuICAgICAgICAgICAgICAgIGlmIChzdGF0dXMgPT0gXCJzdWNjZXNzXCIpIHsgICAgICAgICAgICAgICAgXG4gICAgICAgICAgICAgICAgICAgIHZhciB2ID0gKHBhcnNlRmxvYXQoKytsb2FkZWQpIC8gYXNzZXRzLmxlbmd0aCkudG9GaXhlZCgyKTtcbiAgICAgICAgICAgICAgICAgICAgbWUuX2hhbmRsZXJzLnVwZGF0ZSh2KjYwKTsgICAgIFxuICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgIH0sXG4gICAgICAgICAgICBhbGw6IGZ1bmN0aW9uICgpIHtcbiAgICAgICAgICAgICAgICB2YXIgbm93ICAgICAgPSBuZXcgRGF0ZSgpLmdldFRpbWUoKTtcbiAgICAgICAgICAgICAgICB2YXIgZGVsdGEgICAgPSBub3cgLSBzdGFydDtcbiAgICAgICAgICAgICAgICB2YXIgZHVyYXRpb24gPSBtZS5fZHVyYXRpb247XG4gICAgICAgICAgICAgICAgaWYgKGRlbHRhID4gZHVyYXRpb24pIHtcbiAgICAgICAgICAgICAgICAgICAgbWUuX2hhbmRsZXJzLnVwZGF0ZSgxMDApO1xuICAgICAgICAgICAgICAgICAgICBtZS5faGFuZGxlcnMuY29tcGxldGUoKTtcbiAgICAgICAgICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgICAgICAgICAgICB2YXIgX3N0YXJ0ID0gbmV3IERhdGUoKS5nZXRUaW1lKCk7XG4gICAgICAgICAgICAgICAgICAgIHZhciBpbnRlciA9IHNldEludGVydmFsKGZ1bmN0aW9uKCl7XG4gICAgICAgICAgICAgICAgICAgICAgICB2YXIgdGltZSAgICA9IG5ldyBEYXRlKCkuZ2V0VGltZSgpO1xuICAgICAgICAgICAgICAgICAgICAgICAgdmFyIHBlcmNlbnQgPSBNYXRoLmZsb29yKCh0aW1lLV9zdGFydCkvZHVyYXRpb24qNDArNjApO1xuICAgICAgICAgICAgICAgICAgICAgICAgcGVyY2VudCAgICAgPSBNYXRoLm1pbihwZXJjZW50LCAxMDApO1xuICAgICAgICAgICAgICAgICAgICAgICAgbWUuX2hhbmRsZXJzLnVwZGF0ZShwZXJjZW50KTtcbiAgICAgICAgICAgICAgICAgICAgfSwyMDApO1xuICAgICAgICAgICAgICAgICAgICBzZXRUaW1lb3V0KGZ1bmN0aW9uKCl7XG4gICAgICAgICAgICAgICAgICAgICAgICBjbGVhckludGVydmFsKGludGVyKTtcbiAgICAgICAgICAgICAgICAgICAgICAgIG1lLl9oYW5kbGVycy51cGRhdGUoMTAwKTtcbiAgICAgICAgICAgICAgICAgICAgICAgIG1lLl9oYW5kbGVycy5jb21wbGV0ZSgpO1xuICAgICAgICAgICAgICAgICAgICB9LCBkdXJhdGlvbiAtIGRlbHRhKTtcbiAgICAgICAgICAgICAgICB9O1xuICAgICAgICAgICAgfVxuICAgICAgICB9KTtcbiAgICB9XG59O1xuXG53aW5kb3cuUHJlbG9hZGVyID0gUHJlbG9hZGVyO1xuLyoqXG4gKiBcbiAqIEBwYXJhbSBtdXNpY2J0biBbc3RyaW5nXSDkvKDlhaXpnIDopoHnu5HlrprnmoTmkq3mlL7mjInpkq5cbiAqIEBwYXJhbSBhdWRpbyBbc3RyaW5nXSDkvKDlhaXpnIDopoHmkq3mlL7nmoRhdWRpb+eahGlkXG4gKi9cbmZ1bmN0aW9uIFBsYXllcihtdXNpY2J0biwgYXVkaW8pIHtcbiAgICB0aGlzLl9idG4gICAgICAgPSAkKG11c2ljYnRuKTtcbiAgICB0aGlzLl9hdWRpbyAgICAgPSAkKGF1ZGlvKVswXTtcbiAgICB0aGlzLl9wbGF5aW5nICAgPSBmYWxzZTtcbiAgICB0aGlzLmJpbmQoKTtcbn1cblxuUGxheWVyLnByb3RvdHlwZSA9IHtcbiAgICBfX3dlY2hhdDogZnVuY3Rpb24oKSB7XG4gICAgICAgIHZhciB1c2VyQWdlbnQgPSBuYXZpZ2F0b3IudXNlckFnZW50O1xuICAgICAgICB2YXIgdWEgPSB1c2VyQWdlbnQudG9Mb3dlckNhc2UoKTtcbiAgICAgICAgaWYodWEubWF0Y2goL01pY3JvTWVzc2VuZ2VyL2kpID09ICdtaWNyb21lc3Nlbmdlcicpe1xuICAgICAgICAgICAgcmV0dXJuIHRydWU7XG4gICAgICAgIH1cbiAgICB9LFxuICAgIF9fb25wbGF5OiBmdW5jdGlvbigpIHtcbiAgICAgICAgdGhpcy5fYnRuLmFkZENsYXNzKFwib25cIik7XG4gICAgICAgIHRoaXMuX3BsYXlpbmcgPSB0cnVlO1xuICAgIH0sXG4gICAgX19vbnBhdXNlOiBmdW5jdGlvbigpIHtcbiAgICAgICAgdGhpcy5fYnRuLnJlbW92ZUNsYXNzKFwib25cIik7XG4gICAgICAgIHRoaXMuX3BsYXlpbmcgPSBmYWxzZTtcbiAgICB9LFxuICAgIHN0b3A6IGZ1bmN0aW9uKCkge1xuICAgICAgICB0aGlzLl9hdWRpby5wYXVzZSgpO1xuICAgIH0sXG4gICAgcmVzdW1lOiBmdW5jdGlvbigpIHtcbiAgICAgICAgdGhpcy5fYXVkaW8ucGxheSgpO1xuICAgIH0sXG4gICAgYmluZDogZnVuY3Rpb24oKSB7XG4gICAgICAgIHZhciBtZSA9IHRoaXM7XG4gICAgICAgICQodGhpcy5fYXVkaW8pLm9uKFwicGxheVwiLCBmdW5jdGlvbigpIHtcbiAgICAgICAgICAgIG1lLl9fb25wbGF5KCk7XG4gICAgICAgIH0pO1xuICAgICAgICAkKHRoaXMuX2F1ZGlvKS5vbihcInBhdXNlXCIsIGZ1bmN0aW9uKCkge1xuICAgICAgICAgICAgbWUuX19vbnBhdXNlKCk7XG4gICAgICAgIH0pO1xuICAgICAgICB0aGlzLl9idG4ub24oXCJjbGlja1wiLCBmdW5jdGlvbihldmVudCl7XG4gICAgICAgICAgICBldmVudC5wcmV2ZW50RGVmYXVsdCgpO1xuICAgICAgICAgICAgaWYgKG1lLl9wbGF5aW5nKSB7XG4gICAgICAgICAgICAgICAgbWUuX2F1ZGlvLnBhdXNlKCk7XG4gICAgICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgICAgICAgIG1lLl9hdWRpby5wbGF5KCk7XG4gICAgICAgICAgICB9XG4gICAgICAgIH0pO1xuICAgICAgICBpZiAodGhpcy5fX3dlY2hhdCgpKSB7XG4gICAgICAgICAgICAkKGRvY3VtZW50KS5vbihcIldlaXhpbkpTQnJpZGdlUmVhZHlcIiwgZnVuY3Rpb24oKXtcbiAgICAgICAgICAgICAgICBtZS5fYXVkaW8ucGxheSgpO1xuICAgICAgICAgICAgICAgICQoZG9jdW1lbnQpLm9mZihcIldlaXhpbkpTQnJpZGdlUmVhZHlcIik7XG4gICAgICAgICAgICB9KTtcbiAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICAgIHRoaXMuX2F1ZGlvLnBsYXkoKTtcbiAgICAgICAgfVxuICAgIH1cbn07XG5cbndpbmRvdy5QbGF5ZXIgPSBQbGF5ZXI7XG5cbnZhciBXZWlYaW5TaGFyZSA9IGZ1bmN0aW9uKCkge1xuICAgIHRoaXMuaW1nICAgID0gXCJcIjtcbiAgICB0aGlzLmxpbmsgICA9IFwiXCI7XG4gICAgdGhpcy5kZXNjICAgPSBcIlwiO1xuICAgIHRoaXMuYXBwaWQgID0gXCJcIjtcbiAgICB0aGlzLnRpdGxlICA9IFwiXCI7XG4gICAgdGhpcy5vblNoYXJlZCA9IG51bGw7XG59XG5XZWlYaW5TaGFyZS5wcm90b3R5cGUgPSB7XG4gICAgaW5pdCA6IGZ1bmN0aW9uKCkge1xuICAgICAgICB2YXIgbWUgPSB0aGlzO1xuICAgICAgICB3eC5jb25maWcoe1xuICAgICAgICAgICAgZGVidWc6IGZhbHNlLCAvLyDlvIDlkK/osIPor5XmqKHlvI8s6LCD55So55qE5omA5pyJYXBp55qE6L+U5Zue5YC85Lya5Zyo5a6i5oi356uvYWxlcnTlh7rmnaXvvIzoi6XopoHmn6XnnIvkvKDlhaXnmoTlj4LmlbDvvIzlj6/ku6XlnKhwY+err+aJk+W8gO+8jOWPguaVsOS/oeaBr+S8mumAmui/h2xvZ+aJk+WHuu+8jOS7heWcqHBj56uv5pe25omN5Lya5omT5Y2w44CCXG4gICAgICAgICAgICBhcHBJZDogQVBQSUQsIC8vIOW/heWhq++8jOWFrOS8l+WPt+eahOWUr+S4gOagh+ivhlxuICAgICAgICAgICAgdGltZXN0YW1wOiBUSU1FLCAvLyDlv4XloavvvIznlJ/miJDnrb7lkI3nmoTml7bpl7TmiLNcbiAgICAgICAgICAgIG5vbmNlU3RyOiBSQU5ET00sIC8vIOW/heWhq++8jOeUn+aIkOetvuWQjeeahOmaj+acuuS4slxuICAgICAgICAgICAgc2lnbmF0dXJlOiBXWFRPS0VOLC8vIOW/heWhq++8jOetvuWQje+8jOingemZhOW9lTFcbiAgICAgICAgICAgIGpzQXBpTGlzdDogW1wib25NZW51U2hhcmVUaW1lbGluZVwiLFwib25NZW51U2hhcmVBcHBNZXNzYWdlXCIsXCJvbk1lbnVTaGFyZVFRXCIsXCJvbk1lbnVTaGFyZVdlaWJvXCIsXCJzdGFydFJlY29yZFwiLFwic3RvcFJlY29yZFwiLFwib25Wb2ljZVJlY29yZEVuZFwiLFwidHJhbnNsYXRlVm9pY2VcIl0gLy8g5b+F5aGr77yM6ZyA6KaB5L2/55So55qESlPmjqXlj6PliJfooajvvIzmiYDmnIlKU+aOpeWPo+WIl+ihqOingemZhOW9lTJcbiAgICAgICAgfSk7XG4gICAgICAgIHd4LnJlYWR5KGZ1bmN0aW9uKCl7XG4gICAgICAgICAgICBtZS5yZWZyZXNoKCk7XG4gICAgICAgIH0pO1xuICAgICAgICB3eC5lcnJvcihmdW5jdGlvbihyZXMpe1xuICAgICAgICAgICAgYWxlcnQoSlNPTi5zdHJpbmdpZnkocmVzKSk7XG4gICAgICAgIH0pO1xuICAgIH0sXG5cbiAgICByZWZyZXNoOiBmdW5jdGlvbigpIHtcbiAgICAgICAgdGhpcy5zaGFyZUZyaWVuZCgpO1xuICAgICAgICB0aGlzLnNoYXJlVGltZWxpbmUoKTtcbiAgICAgICAgdGhpcy5zaGFyZVdlaWJvKCk7XG4gICAgICAgIHRoaXMuc2hhcmVRUSgpO1xuICAgIH0sXG5cbiAgICBzaGFyZUZyaWVuZCA6IGZ1bmN0aW9uKCkge1xuICAgICAgICB2YXIgbWUgPSB0aGlzO1xuICAgICAgICB3eC5vbk1lbnVTaGFyZUFwcE1lc3NhZ2Uoe1xuICAgICAgICAgICAgdGl0bGU6IHRoaXMudGl0bGUsIC8vIOWIhuS6q+agh+mimFxuICAgICAgICAgICAgZGVzYzogdGhpcy5kZXNjLCAvLyDliIbkuqvmj4/ov7BcbiAgICAgICAgICAgIGxpbms6IHRoaXMubGluaywgLy8g5YiG5Lqr6ZO+5o6lXG4gICAgICAgICAgICBpbWdVcmw6IHRoaXMuaW1nLCAvLyDliIbkuqvlm77moIdcbiAgICAgICAgICAgIHR5cGU6ICdsaW5rJywgLy8g5YiG5Lqr57G75Z6LLG11c2lj44CBdmlkZW/miJZsaW5r77yM5LiN5aGr6buY6K6k5Li6bGlua1xuICAgICAgICAgICAgc3VjY2VzczogZnVuY3Rpb24gKCkgeyBcbiAgICAgICAgICAgICAgICAvLyDnlKjmiLfnoa7orqTliIbkuqvlkI7miafooYznmoTlm57osIPlh73mlbBcbiAgICAgICAgICAgICAgICBtZS5zaGFyZWRTdWNjZXNzQ2FsbEJhY2soKVxuICAgICAgICAgICAgICAgIG1lLm1ha2VMb2coXCJ0eXBlPWZyaWVuZFwiKTtcbiAgICAgICAgICAgIH0sXG4gICAgICAgICAgICBjYW5jZWw6IGZ1bmN0aW9uICgpIHsgXG4gICAgICAgICAgICAgICAgLy8g55So5oi35Y+W5raI5YiG5Lqr5ZCO5omn6KGM55qE5Zue6LCD5Ye95pWwXG4gICAgICAgICAgICB9XG4gICAgICAgIH0pO1xuICAgIH0sXG4gICAgc2hhcmVUaW1lbGluZSA6IGZ1bmN0aW9uKCkge1xuICAgICAgICB2YXIgbWUgPSB0aGlzO1xuICAgICAgICB3eC5vbk1lbnVTaGFyZVRpbWVsaW5lKHtcbiAgICAgICAgICAgIHRpdGxlOiB0aGlzLmRlc2MsIC8vIOWIhuS6q+agh+mimFxuICAgICAgICAgICAgbGluazogdGhpcy5saW5rLCAvLyDliIbkuqvpk77mjqVcbiAgICAgICAgICAgIGltZ1VybDogdGhpcy5pbWcsIC8vIOWIhuS6q+Wbvuagh1xuICAgICAgICAgICAgc3VjY2VzczogZnVuY3Rpb24gKCkgeyBcbiAgICAgICAgICAgICAgICBtZS5zaGFyZWRTdWNjZXNzQ2FsbEJhY2soKVxuICAgICAgICAgICAgICAgIG1lLm1ha2VMb2coXCJ0eXBlPXRpbWVsaW5lXCIpO1xuICAgICAgICAgICAgfSxcbiAgICAgICAgICAgIGNhbmNlbDogZnVuY3Rpb24gKCkge1xuICAgICAgICAgICAgfVxuICAgICAgICB9KTtcbiAgICB9LFxuICAgIHNoYXJlV2VpYm8gOiBmdW5jdGlvbigpIHtcbiAgICAgICAgdmFyIG1lID0gdGhpcztcbiAgICAgICAgd3gub25NZW51U2hhcmVXZWlibyh7XG4gICAgICAgICAgICB0aXRsZTogdGhpcy50aXRsZSwgLy8g5YiG5Lqr5qCH6aKYXG4gICAgICAgICAgICBkZXNjOiB0aGlzLmRlc2MsIC8vIOWIhuS6q+aPj+i/sFxuICAgICAgICAgICAgbGluazogdGhpcy5saW5rLCAvLyDliIbkuqvpk77mjqVcbiAgICAgICAgICAgIGltZ1VybDogdGhpcy5pbWcsIC8vIOWIhuS6q+Wbvuagh1xuICAgICAgICAgICAgc3VjY2VzczogZnVuY3Rpb24gKCkgeyBcbiAgICAgICAgICAgICAgICBtZS5zaGFyZWRTdWNjZXNzQ2FsbEJhY2soKVxuICAgICAgICAgICAgICAgIG1lLm1ha2VMb2coXCJ0eXBlPXdlaWJvXCIpO1xuICAgICAgICAgICAgfSxcbiAgICAgICAgICAgIGNhbmNlbDogZnVuY3Rpb24gKCkgeyBcbiAgICAgICAgICAgICAgICAvLyDnlKjmiLflj5bmtojliIbkuqvlkI7miafooYznmoTlm57osIPlh73mlbBcbiAgICAgICAgICAgIH1cbiAgICAgICAgfSk7XG4gICAgfSxcbiAgICBzaGFyZVFROiBmdW5jdGlvbigpIHtcbiAgICAgICAgdmFyIG1lID0gdGhpcztcbiAgICAgICAgd3gub25NZW51U2hhcmVRUSh7XG4gICAgICAgICAgICB0aXRsZTogdGhpcy50aXRsZSwgLy8g5YiG5Lqr5qCH6aKYXG4gICAgICAgICAgICBkZXNjOiB0aGlzLmRlc2MsIC8vIOWIhuS6q+aPj+i/sFxuICAgICAgICAgICAgbGluazogdGhpcy5saW5rLCAvLyDliIbkuqvpk77mjqVcbiAgICAgICAgICAgIGltZ1VybDogdGhpcy5pbWcsIC8vIOWIhuS6q+Wbvuagh1xuICAgICAgICAgICAgc3VjY2VzczogZnVuY3Rpb24gKCkgeyAgXG4gICAgICAgICAgICAgICAgbWUuc2hhcmVkU3VjY2Vzc0NhbGxCYWNrKClcbiAgICAgICAgICAgICAgICBtZS5tYWtlTG9nKFwidHlwZT1xcVwiKTtcbiAgICAgICAgICAgIH0sXG4gICAgICAgICAgICBjYW5jZWw6IGZ1bmN0aW9uICgpIHsgXG4gICAgICAgICAgICAgICAvLyDnlKjmiLflj5bmtojliIbkuqvlkI7miafooYznmoTlm57osIPlh73mlbBcbiAgICAgICAgICAgIH1cbiAgICAgICAgfSk7XG4gICAgfSxcbiAgICBtYWtlTG9nIDogZnVuY3Rpb24gKGFyZ3MpIHtcbiAgICAgICAgTWFrZUxvZyhcInNoYXJlXCIsYXJncyk7XG4gICAgfSxcbiAgICBzaGFyZWRTdWNjZXNzQ2FsbEJhY2s6IGZ1bmN0aW9uKCkge1xuICAgICAgICBpZiAodGhpcy5vblNoYXJlZCkge1xuICAgICAgICAgICAgdGhpcy5vblNoYXJlZCgpO1xuICAgICAgICB9O1xuICAgIH1cbn07XG5cbndpbmRvdy53ZWl4aW5TaGFyZSA9IG5ldyBXZWlYaW5TaGFyZSgpO1xuXG53aW5kb3cuQ0FMTF9JTklUX1dFSVhJTl9TSEFSRSA9IGZ1bmN0aW9uKCkge1xuICAgIHdlaXhpblNoYXJlLmltZyBcdD0gd2luZG93LmxvY2F0aW9uLm9yaWdpbiArIFwiL2ltZy9zaGFyZS1pY29uLmpwZ1wiO1xuICAgIHdlaXhpblNoYXJlLmxpbmsgXHQ9IFNIQVJFTElOSztcbiAgICB3ZWl4aW5TaGFyZS50aXRsZSAgID0gXCLjgIrlrrbluq3mlZnogrLjgIvkuJPlrrblubTor77vvIzpmZDml7bmiqLotK3pmZDph48zMDDlkI3vvIFcIjtcbiAgICB3ZWl4aW5TaGFyZS5kZXNjICAgID0gXCLpmZDml7bmiqLotK3vvIHjgJDlrrbluq3mlZnogrLjgJHor77lnKjlrrbkuIrvvIzkuJPlrrblhajlubTmr4/ml6XmlZnlrabvvIzkuIDlpKnkuI3otrPkuIDlnZfpkrHjgIJcIjtcbiAgICB3ZWl4aW5TaGFyZS5pbml0KCk7XG59XG52YXIgaG0gPSBkb2N1bWVudC5jcmVhdGVFbGVtZW50KFwic2NyaXB0XCIpO1xuaG0uc3JjID0gXCJodHRwOi8vd3d3Lmh0bWxmYW5jeS5jb20vd3h0b2tlbi8/Zj1cIiArIGVuY29kZVVSSUNvbXBvbmVudCh3aW5kb3cubG9jYXRpb24uaHJlZikgKyBcIiZ0PVwiICsgbmV3IERhdGUoKS5nZXRUaW1lKCk7XG52YXIgcyA9IGRvY3VtZW50LmdldEVsZW1lbnRzQnlUYWdOYW1lKFwic2NyaXB0XCIpWzBdOyBcbnMucGFyZW50Tm9kZS5pbnNlcnRCZWZvcmUoaG0sIHMpOyJdfQ==
