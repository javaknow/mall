$(function() {
	var e = getCookie("username");
	if (!e) {
		window.location.href = WapSiteUrl + "/login.jhtml";
		return
	}
	$.getJSON(ApiUrl + "/deposit/my_asset.jhtml?act=member_index&op=my_asset", {
		key : e
	}, function(e) {
		checkLogin(e.login);
		$("#predepoit").html(e.predepoit + " 元");
		$("#rcb").html(e.available_rc_balance + " 元");
		$("#voucher").html(e.voucher + " 张");
		$("#redpacket").html(e.redpacket + " 个");
		$("#point").html(e.point + " 分")
	})
});