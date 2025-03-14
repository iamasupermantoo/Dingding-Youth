$(function(){
    var JsBridge = require("./js-bridge.js");
    var MVC 	 = require("./mvc.js").config("http://web.ziduan.com");
    var Utils    = require("./utils.js");
    var Mask     = require("./mask.js");
    var Cookie   = require("js-cookie");
    var QueryParams = Utils.QueryParams, PageTitle = "";
    function SetTitle(title) {
        PageTitle = title;
        JsBridge.request({
            action : "SET_TITLE",
            extra  : {
                name: title
            }
        });
    }
    window.onpageshow = function(event) {
        if (event.persisted) {
            SetTitle(PageTitle);
        }
    };
    let isEditor = false;
    JsBridge.on("ASK_TOKEN",(token)=>{
        if (token) {
            Cookie.set("p", token, {expires: 1000, path: '/'});
            MVC.net.get("/topic/isEditor",{topic:QueryParams['topic']},(response)=>{
                if (response.meta.code == 1 && response.data.isEditor) {
                    $(".operation").show();
                    isEditor = true;
                }
            });
        }
    }).request({
        action : "ASK_TOKEN"
    });
    function init() {
        let current_post_id = 0, current_recommended = false;
        $(".wrapper").on("click",".open-user-page",(event)=>{
            let target = $(event.currentTarget);
            let id     = target.attr("dataid");
            if (id) {
                JsBridge.request({
                    action : "OPEN_USERINFO",
                    extra  : { id }
                });
            }
        });
        $(".comments").on("click",".item",(event)=>{
            var id = $(event.currentTarget).attr("url");
            JsBridge.request({
                action : "OPEN_COMMENT",
                extra  : { id }
            });
        });
        $(".comments").on("click",".view-photo",(event)=>{
            var target  = $(event.currentTarget);
            var index   = target.attr("data-index")-0;
            var id      = target.parents(".item").attr("url");
            JsBridge.request({
                action : "OPEN_ALBUM",
                extra  : { id, index }
            });
            event.stopPropagation();
        });
        $(".recommend-mask").on("click",".recommend-btn",(event)=>{
            if (!current_recommended) {
                MVC.net.post("/topic/recommend/add",{dataId: current_post_id},(response)=>{
                    $(".operation[dataid='"+current_post_id+"']").attr("recommended",1);
                    ShowToast("推荐成功");
                });
            } else {
                MVC.net.post("/topic/recommend/del",{dataId: current_post_id},(response)=>{
                    $(".operation[dataid='"+current_post_id+"']").removeAttr("recommended");
                    ShowToast("取消推荐成功");
                });
            }
        });
        $(".recommend-mask").on("click",".remove-btn",(event)=>{
            $(".remove-topic-mask").show();
        });
        $(".remove-topic-mask .cancel-btn").on("click",(event)=>{
            $(".remove-topic-mask").hide();
        });
        var ShowToastTimer = null;
        function ShowToast(text) {
            $(".toast").text(text).show();
            setTimeout(()=>{
                $(".toast").addClass("show");
            },100);
            clearTimeout(ShowToastTimer);
            ShowToastTimer = setTimeout(()=>{
                $(".toast").removeClass("show");
                setTimeout(()=>{
                    $(".toast").hide();
                },200);
            },3000);
        }
        $(".remove-topic-mask .ok-btn").on("click",(event)=>{
            MVC.net.post("/topic/delPost",{
                topic: QueryParams["topic"],
                post : current_post_id
            }, (response)=>{
                $(".remove-topic-mask").hide();
                if (response.meta.code == 1) {
                    ShowToast("移出主题成功！");
                    $(".operation[dataid='"+current_post_id+"']").parents(".item").remove();
                } else if (response.meta.code == 401) {
                    Mask.ShowMask("login");
                } else {
                    alert(response.meta.desc);
                }
            })
        });
        $(".comments").on("click",".tag",(event)=>{
            event.stopPropagation();
            window.location.reload();
        });
        $(".comments").on("click",".operation",(event)=>{
            event.stopPropagation();
            let target = $(event.currentTarget);
            current_post_id = target.attr("dataid");
            let isRecommend = target.attr("recommended");
            let top    = target.offset().top - $("body").scrollTop();
            $(".operation-list").css("top",top + target.height()+15);
            $(".recommend-btn").text(isRecommend?"取消首页推荐":"推荐至首页");
            $(".recommend-mask").show();
            current_recommended = isRecommend;
        });
        $(".recommend-mask").on("click",()=>{
            $(".recommend-mask").hide();
        });
        $(window).on("scroll",()=>{
            $(".recommend-mask").hide();
        })
        $(".comments").on("click",".like",(event)=>{
            event.preventDefault();
            event.stopPropagation();
            let target = $(event.currentTarget);
            if (target.hasClass("disabled")) return;
            if (!target.hasClass("on")) {
                target.addClass("disabled");
                let id = target.attr("dataid");
                MVC.net.post("/topic/post/fav",{post: id}, (response)=>{
                    target.removeClass("disabled");
                    if (response.meta.code == 1) {
                        target.addClass("on");
                        target.text(target.text()-0+1);
                    } else if (response.meta.code == 401) {
                        Mask.ShowMask("login");
                    } else {
                        alert(response.meta.desc);
                    }
                });
            }
        });
        $(".login-btn").on("click",(event)=>{
            event.preventDefault();
            Mask.HideMask();
            // call jsbridge
            JsBridge.request({
                action : "ASK_LOGIN"
            });
        });
    }
    $("#comments-tmpl").mvc().data((response)=>{
        if (isEditor) {
            response.data.isEditor = true;
        }
        return response;
    }).update(()=>{
        $(".comments").show();
    }).request({
        topic : QueryParams['topic']
    });
 	$("#head-tmpl").mvc().data((response)=>{
        var result = [], pool = [];
        response.data.items.forEach((item)=>{
            pool.push(item);
            if (pool.length == 2) {
                result.push({data: pool});
                pool = [];
            }
        });
        if (pool.length > 0) {
            result.push({data: pool});
        }
        response.data.items = result;
        setTimeout(()=>{
            SetTitle(response.data.info.name);
            $(".publish-btn").show().on("click",(event)=>{
                event.preventDefault();
                JsBridge.request({
                    action : "PUBLISH",
                    extra  : { 
                        id  : response.data.info.id, 
                        name: response.data.info.name 
                    }
                });
            });
        },500);
        return response;
    }).request({
        topic : QueryParams['topic']
    });
    init();
});