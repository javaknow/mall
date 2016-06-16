var page = pagesize;//页面
var curpage = 1;//当前页
var hasmore = true;//是否还有下一页
var footer = false;//是否显示底部
var keyword = decodeURIComponent(getQueryString("keyword"));//搜索的关键字
var gc_id = getQueryString("gc_id");//分类id
var b_id = getQueryString("b_id");//品牌id
var key = getQueryString("key");
var order = getQueryString("order");
var area_id = getQueryString("area_id");
var startPrice = getQueryString("startPrice");
var endPrice = getQueryString("endPrice");
var own_shop = getQueryString("own_shop");
var gift = getQueryString("gift");
var groupbuy = getQueryString("groupbuy");
var xianshi = getQueryString("xianshi");
var virtual = getQueryString("virtual");
var ci = getQueryString("ci");
var myDate = new Date;
var searchTimes = myDate.getTime();
$(function() {
    $.animationLeft({
        valve: "#search_adv",
        wrapper: ".nctouch-full-mask",
        scroll: "#list-items-scroll"
    });
    //点击头部跳转到搜索页面
    $("#header").on("click", ".header-inp",function() {
        location.href = WapSiteUrl + "/search.jhtml?keyword=" + keyword
    });
    if (keyword != "") {
        $("#keyword").html(keyword)
    }
    
    $("#show_style").click(function() {
        if ($("#product_list").hasClass("grid")) {
            $(this).find("span").removeClass("browse-grid").addClass("browse-list");
            $("#product_list").removeClass("grid").addClass("list")
        } else {
            $(this).find("span").addClass("browse-grid").removeClass("browse-list");
            $("#product_list").addClass("grid").removeClass("list")
        }
    });
    
    //排序样式
    $("#sort_default").click(function() {
        if ($("#sort_inner").hasClass("hide")) {
            $("#sort_inner").removeClass("hide")
        } else {
            $("#sort_inner").addClass("hide")
        }
    });
    
    $("#nav_ul").find("a").click(function() {
        $(this).addClass("current").parent().siblings().find("a").removeClass("current");
        if (!$("#sort_inner").hasClass("hide") && $(this).parent().index() > 0) {
            $("#sort_inner").addClass("hide")
        }
    });
    
    $("#sort_inner").find("a").click(function() {
        $("#sort_inner").addClass("hide").find("a").removeClass("cur");
        var e = $(this).addClass("cur").text();
        $("#sort_default").html(e + "<i></i>")
    });
    
    //点击获取店铺的评分信息
    $("#product_list").on("click", ".goods-store a",function() {
        var e = $(this);
        var r = $(this).attr("data-id");
        var i = $(this).text();
        $.getJSON(ApiUrl + "/shop/store_credit.jhtml?act=store&op=store_credit", {
            shopId: r
        },
        function(t) {
            var a = "<dl>" + '<dt><a href="store.html?store_id=' + r + '">' + i 
            + '<span class="arrow-r"></span></a></dt>' 
            + '<dd class="' + t.store_desccredit.percent_class + 
            '">描述相符：<em>' + 
            t.store_desccredit.credit + 
            "</em><i></i></dd>" + '<dd class="' 
            + t.store_servicecredit.percent_class + 
            '">服务态度：<em>' 
            + t.store_servicecredit.credit 
            + "</em><i></i></dd>" + '<dd class="' 
            + t.store_deliverycredit.percent_class 
            + '">发货速度：<em>' 
            + t.store_deliverycredit.credit 
            + "</em><i></i></dd>" + "</dl>";
            e.next().html(a).show()
        })
    }).on("click", ".sotre-creidt-layout",function() {
        $(this).hide()
    });
    
    get_list();
    
    $(window).scroll(function() {
        if ($(window).scrollTop() + $(window).height() > $(document).height() - 1) {
            get_list()
        }
    });
    search_adv()
});

//产品列表
function get_list() {
    $(".loading").remove();
    if (!hasmore) {
        return false
    }
    hasmore = false;
    param = {};
    param.page = page;
    param.curpage = curpage;
    if (gc_id != "") {
        param.gc_id = gc_id
    }/* else if (keyword != "") {
        param.keyword = keyword
    } */else if (b_id != "") {
        param.b_id = b_id
    }
    if (key != "") {
        param.key = key
    }
    if (order != "") {
        param.order = order
    }
    
    //产品的ajax搜索
    $.getJSON(ApiUrl + "/product/search.jhtml?act=goods&op=goods_list" + window.location.search.replace("?", "&"), param,function(e) {
    	console.info(e);
        if (!e) {
            e = [];
            e.datas = [];
            e.datas.goods_list = []
        }
        $(".loading").remove();
        curpage++;
        var r = template.render("home_body", e);
        $("#product_list .goods-secrch-list").append(r);
        hasmore = e.hasmore
    })
}

//搜索条件
function search_adv() {
    $.getJSON(ApiUrl + "/search/search_adv.jhtml?act=index&op=search_adv",
    function(e) {
        var r = e;
console.info(r);
        $("#list-items-scroll").html(template.render("search_items", r));
        if (area_id) {
            $("#area_id").val(area_id)
        }
        if (startPrice) {
            $("#startPrice").val(startPrice)
        }
        if (endPrice) {
            $("#endPrice").val(endPrice)
        }
        if (own_shop) {
            $("#own_shop").addClass("current")
        }
        if (gift) {
            $("#gift").addClass("current")
        }
        if (groupbuy) {
            $("#groupbuy").addClass("current")
        }
        if (xianshi) {
            $("#xianshi").addClass("current")
        }
        if (virtual) {
            $("#virtual").addClass("current")
        }
        if (ci) {
            var i = ci.split("_");
            for (var t in i) {
                $('a[name="ci"]').each(function() {
                    if ($(this).attr("value") == i[t]) {
                        $(this).addClass("current")
                    }
                })
            }
        }
        $("#search_submit").click(function() {
            var e = "?keyword=" + keyword,
            r = "";
            e += "&area_id=" + $("#area_id").val();
            if ($("#startPrice").val() != "") {
                e += "&startPrice=" + $("#startPrice").val()
            }
            if ($("#endPrice").val() != "") {
                e += "&endPrice=" + $("#endPrice").val()
            }
            if ($("#own_shop")[0].className == "current") {
                e += "&own_shop=1"
            }
            if ($("#gift")[0].className == "current") {
                e += "&gift=1"
            }
            if ($("#groupbuy")[0].className == "current") {
                e += "&groupbuy=1"
            }
            if ($("#xianshi")[0].className == "current") {
                e += "&xianshi=1"
            }
            if ($("#virtual")[0].className == "current") {
                e += "&virtual=1"
            }
            $('a[name="ci"]').each(function() {
                if ($(this)[0].className == "current") {
                    r += $(this).attr("value") + "_"
                }
            });
            if (r != "") {
                e += "&ci=" + r
            }
            window.location.href = WapSiteUrl + "/product/list.jhtml" + e
        });
        $('a[nctype="items"]').click(function() {
            var e = new Date;
            if (e.getTime() - searchTimes > 300) {
                $(this).toggleClass("current");
                searchTimes = e.getTime()
            }
        });
        $('input[nctype="price"]').on("blur",
        function() {
            if ($(this).val() != "" && !/^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test($(this).val())) {
                $(this).val("")
            }
        });
        $("#reset").click(function() {
            $('a[nctype="items"]').removeClass("current");
            $('input[nctype="price"]').val("");
            $("#area_id").val("")
        })
    })
}
function init_get_list(e, r) {
    order = e;
    key = r;
    curpage = 1;
    hasmore = true;
    $("#product_list .goods-secrch-list").html("");
    $("#footer").removeClass("posa");
    get_list()
}