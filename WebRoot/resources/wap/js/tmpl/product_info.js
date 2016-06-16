$(function() {
	var o = getQueryString("goods_id");
	$.ajax({
		url : ApiUrl + "/product/info.jhtml?act=goods&op=goods_body",
		data : {
			id : o
		},
		type : "get",
		success : function(o) {
			$(".fixed-tab-pannel").html(o)
		}
	});
	$("#goodsDetail").click(function() {
		window.location.href = WapSiteUrl + "/product/detail.jhtml?goods_id=" + o
	});
	$("#goodsBody").click(function() {
		window.location.href = WapSiteUrl + "/product/info.jhtml?goods_id=" + o
	});
	$("#goodsEvaluation").click(function() {
		window.location.href = WapSiteUrl + "/product/eval_list.jhtml?goods_id=" + o
	})
});