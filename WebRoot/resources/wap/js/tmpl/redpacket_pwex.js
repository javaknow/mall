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
			pwd_code : "请填写红包卡密",
			captcha : "请填写验证码"
		},
		callback : function(e, a, r) {
			if (e.length > 0) {
				var c = "";
				$.map(a, function(e, a) {
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
			var a = $.trim($("#pwd_code").val());
			var r = $.trim($("#captcha").val());
			var c = $.trim($("#codekey").val());
			$.ajax({
				type : "post",
				url : ApiUrl + "/redpacket/rp_pwex.jhtml?act=member_redpacket&op=rp_pwex",
				data : {
					key : e,
					code : a,
					captcha : r,
					captchaId : c
				},
				dataType : "json",
				success : function(e) {
					if (e.code == 200&&e.message.type=="success") {
						location.href = WapSiteUrl + "/member/redPacket/list.jhtml"
					} else {
						loadSeccode();
						errorTipsShow("<p>" + e.message.content + "</p>")
					}
				}
			})
		}
	})
});