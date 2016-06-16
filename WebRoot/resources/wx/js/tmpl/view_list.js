var key = getCookie("key");
$(function() {
    var e = new ncScrollLoad;
    e.loadInit({
        url: ApiUrl + "/history/list.jhtml",
        getparam: {
            key: key
        },
        tmplid: "viewlist_data",
        containerobj: $("#viewlist"),
        iIntervalId: true,
        data: {
            WapSiteUrl: WapSiteUrl
        }
    });
    $("#clearbtn").click(function() {
        $.ajax({
            type: "post",
            url: ApiUrl + "/index.php?act=member_goodsbrowse&op=browse_clearall",
            data: {
                key: key
            },
            dataType: "json",
            async: false,
            success: function(e) {
                if (e.code == 200) {
                    location.href = WapSiteUrl + "/tmpl/member/views_list.html"
                } else {
                    $.sDialog({
                        skin: "red",
                        content: e.datas.error,
                        okBtn: false,
                        cancelBtn: false
                    })
                }
            }
        })
    })
});