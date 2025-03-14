$(function(){
    var MVC     = require("./mvc.js").config("http://wx.u.ziduan.com");
    var Mask    = require("./mask.js");
    var Utils   = require("./utils.js");
    var Native  = require("./nativelink.js");
    var _H      = $(window).height(), $player;
    var QueryParams = Utils.QueryParams, MusicUrl, Sub, Issue;
    function init() {
        let $native = new Native(Mask, `udan://lesson?sub=${Sub}&issue=${Issue}`);
        $native.init();
        $(".full-h").css("height",_H);
        $(".notice,.comment-btn,.reply-btn").on("click",(event)=>{
            event.preventDefault();
            Mask.ShowMask(true);
        });
        if (MusicUrl) {
            $player = new Player("#play-btn", "#backMusic", {
                onupdate: function(time){
                    $(".player span i").text(Utils.timeToStr(time>>0)+"/");
                },
                onready:  function() {
                    $("#backMusic").attr("src",MusicUrl);
                }
            });
        } else {
            $("#play-btn").off("click").on("click",(event)=>{
                event.preventDefault();
                Mask.ShowMask();
            });
        }
    }
    $("#page-tmpl").mvc().data((response)=>{
        if (response.data.issue.audio) {
            let length = response.data.issue.audio.length;
            response.data.issue.audio.lenstr = Utils.timeToStr(length);
            MusicUrl = response.data.issue.audio.url;
        }
        Sub   = response.data.issue.sub;
        Issue = response.data.issue.id;
        weixinShare.title = response.data.issue.title;
        weixinShare.desc  = response.data.issue.content.replace(/(<[^>]+>)|(\&nbsp;)/g,"");
        weixinShare.refresh();
        return response;
    }).update(()=>{
        init();
    }).request({
        card : QueryParams['card']
    });
});