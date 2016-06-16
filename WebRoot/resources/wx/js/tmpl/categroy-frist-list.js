$(function() {
    var e;
    $("#header").on("click", ".header-inp",
    function() {
        location.href = WapSiteUrl + "/search.jhtml"
    });
    
    //获取一级分类
    $.getJSON(ApiUrl + "/productCategory/getProductCategories.jhtml",
    function(t) {
        var r = t.datas;
        r.WapSiteUrl = WapSiteUrl;
        var a = template.render("category-one", t);
        $("#categroy-cnt").html(a);
        e = new IScroll("#categroy-cnt", {
            mouseWheel: true,
            click: true
        })
    });
    
    //获取品牌
    get_brand_recommend();
    
    $("#categroy-cnt").on("click", ".category",
    function() {
        $(".pre-loading").show();
        $(this).parent().addClass("selected").siblings().removeClass("selected");
        var t = $(this).attr("date-id");
        $.getJSON(ApiUrl + "/productCategory/getProductCategories.jhtml", {
            gc_id: t
        },
        function(e) {
            var t = e.datas;
            t.WapSiteUrl = WapSiteUrl;
            var r = template.render("category-two", e);
            $("#categroy-rgt").html(r);
            $(".pre-loading").hide();
            new IScroll("#categroy-rgt", {
                mouseWheel: true,
                click: true
            })
        });
        e.scrollToElement(document.querySelector(".categroy-list li:nth-child(" + ($(this).parent().index() + 1) + ")"), 1e3)
    });
    $("#categroy-cnt").on("click", ".brand",
    function() {
        $(".pre-loading").show();
        get_brand_recommend()
    })
});

function get_brand_recommend() {
    $(".category-item").removeClass("selected");
    $(".brand").parent().addClass("selected");
    $.getJSON(ApiUrl + "/productCategory/getBrand.jhtml",
    function(t) {
        var e = t.datas;
        var r = template.render("brand-one", t);
        $("#categroy-rgt").html(r);
        $(".pre-loading").hide();
        new IScroll("#categroy-rgt", {
            mouseWheel: true,
            click: true
        })
    })
}