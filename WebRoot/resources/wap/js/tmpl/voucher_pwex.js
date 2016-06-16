$(function() {
	var e = getCookie("username");
	if (!e) {
		window.location.href = WapSiteUrl + "/login.jhtml";
		return
	}
	loadSeccode();
	$("#refreshcode").bind("click", function() {
		loadSeccode()
	});
	$.sValid.init({
		rules : {
			pwd_code : "required",
			captcha : "required"
		},
		messages : {
			pwd_code : "请填写代金券卡密",
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
	$("#saveform").click(function() {
		if (!$(this).parent().hasClass("ok")) {
			return false
		}
		if ($.sValid()) {
			var r = $.trim($("#pwd_code").val());
			var a = $.trim($("#captcha").val());
			var c = $.trim($("#codekey").val());
			$.ajax({
				type : "post",
				url : ApiUrl + "/voucher/voucher_pwex.jhtml?act=member_voucher&op=voucher_pwex",
				data : {
					key : e,
					code : r,
					captcha : a,
					captchaId : c
				},
				dataType : "json",
				success : function(e) {
					if (e.code == 200&&e.message.type=="success") {
						location.href = WapSiteUrl + "/member/voucher/list.jhtml"
					} else {
						loadSeccode();
						errorTipsShow("<p>" + e.message.content + "</p>")
					}
				}
			})
		}
	})
});