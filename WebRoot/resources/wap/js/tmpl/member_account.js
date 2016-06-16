$(function() {
	var e = getCookie("username");
	if (!e) {
		window.location.href = WapSiteUrl + "/login.jhtml";
		return
	}
	//获取手机号码信息
	$.ajax({
		type : "get",
		url : ApiUrl + "/get_mobile_info.jhtml?act=member_account&op=get_mobile_info",
		data : {
			key : e
		},
		dataType : "json",
		success : function(e) {
			if (e.code == 200) {
				if (e.state) {
					$("#mobile_link").attr("href", "member_mobile_modify.jhtml");
					$("#mobile_value").html(e.mobile)
				}
			} else {
			}
		}
	});
	//获取支付密码是否设置了
	$.ajax({
		type : "get",
		url : ApiUrl + "/get_paypwd_info.jhtml?act=member_account&op=get_paypwd_info",
		data : {
			key : e
		},
		dataType : "json",
		success : function(e) {
			if (e.code == 200) {
				if (!e.state) {
					$("#paypwd_tips").html("未设置")
				}
			} else {
			}
		}
	})
});