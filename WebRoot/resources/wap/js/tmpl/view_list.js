var key = getCookie("username");
$(function() {
	var e = new ncScrollLoad;
	var historyProduct = getCookie("historyProduct");
	var historyProductIds = historyProduct != null ? historyProduct.split(",") : new Array();
	e.loadInit({
		url : ApiUrl + "/member/browse_list.jhtml?act=member_goodsbrowse&op=browse_list",
		getparam : {
			key : key,
			ids : historyProductIds
		},
		tmplid : "viewlist_data",
		containerobj : $("#viewlist"),
		iIntervalId : true,
		data : {
			WapSiteUrl : WapSiteUrl
		}
	});
	
	//浏览器记录的全部清除
	$("#clearbtn").click(function() {
		delCookie("historyProduct");
		$.ajax({
			type : "post",
			url : ApiUrl+ "/product/browse_clearall.jhtml?act=member_goodsbrowse&op=browse_clearall",
			data : {
				key : key
			},
			dataType : "json",
			async : false,
			success : function(e) {
				if (e.code == 200&&e.message.type=="success") {
					location.href = WapSiteUrl + "/member/history.jhtml"
				} else {
					$.sDialog({
						skin : "red",
						content : e.message.content,
						okBtn : false,
						cancelBtn : false
					})
				}
			}
		})
	})
});