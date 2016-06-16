$(function() {
	var e = getCookie("username");
	if (!e) {
		window.location.href = WapSiteUrl + "login.jhtml";
		return
	}
	loadSeccode();
	
	//刷新验证码
	$("#refreshcode").bind("click", function() {
		loadSeccode();
	});
	
	//表单验证
	$.sValid.init({
		rules : {
			rc_sn : "required",
			captcha : "required"
		},
		messages : {
			rc_sn : "请输入平台充值卡号",
			captcha : "请填写验证码"
		},
		callback : function(e, r, a) {
			if (e.length > 0) {
				var c = "";
				$.map(r, function(e, r) {
					c += "<p>" + e + "</p>"
				});
				errorTipsShow(c)
			} else {
				errorTipsHide()
			}
		}
	});
	
	//表单提交
	$("#saveform").click(function() {
		if (!$(this).parent().hasClass("ok")) {
			return false
		}
		if ($.sValid()) {
			var r = $.trim($("#rc_sn").val());
			var a = $.trim($("#captcha").val());
			var c = $.trim($("#codekey").val());
			$.ajax({
				type : "post",
				url : ApiUrl + "/recharge/rechargecard_add.jhtml?act=member_fund&op=rechargecard_add",
				data : {
					key : e,
					code : r,
					captcha : a,
					captchaId : c
				},
				dataType : "json",
				success : function(e) {
					if (e.message.type == "success") {
						location.href = WapSiteUrl + "/member/recharge/rechargecardlog_list.jhtml"
					} else {
						loadSeccode();
						errorTipsShow("<p>" + e.message.content + "</p>")
					}
				}
			})
		}
	})
});