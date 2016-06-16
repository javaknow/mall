$(function() {
	//获取产品的id
	var id = getQueryString("id");
	$.ajax({
		url : ApiUrl + "/product/product_introduce.jhtml",
		data : {
			"id" : id
		},
		type : "get",
		success : function(o) {
			$(".fixed-tab-pannel").html(o.datas)
		}
	});
	$("#goodsDetail").click(function() {
		window.location.href = WapSiteUrl+ "/promotion/detail.jhtml?id=" + id
	});
	$("#goodsBody").click(function() {
		window.location.href = WapSiteUrl+ "/tmpl/product_info.html?id=" + id
	});
	$("#goodsEvaluation").click(function() {
		window.location.href = WapSiteUrl+ "/tmpl/product_eval_list.html?id=" + id
	})
});