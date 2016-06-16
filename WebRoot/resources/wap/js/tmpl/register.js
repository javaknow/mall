$(function() {
	var e = getCookie("username");
	if (e) {
		window.location.href = WapSiteUrl + "/member/index.jhtml";
		return
	}
	//检测系统是否允许手机注册
	$.getJSON(ApiUrl + "/connect_sms_reg.jhtml?act=connect&op=get_state&t=connect_sms_reg", function(e) {
		if (e.datas != 1) {
			$(".register-tab").show()
		}
	});
	$.sValid.init({
		rules : {
			username : "required",
			userpwd : "required",
			password_confirm : "required",
			email : {
				required : true,
				email : true
			}
		},
		messages : {
			username : "用户名必须填写！",
			userpwd : "密码必填!",
			password_confirm : "确认密码必填!",
			email : {
				required : "邮件必填!",
				email : "邮件格式不正确"
			}
		},
		callback : function(e, r, a) {
			if (e.length > 0) {
				var i = "";
				$.map(r, function(e, r) {
					i += "<p>" + e + "</p>"
				});
				errorTipsShow(i)
			} else {
				errorTipsHide()
			}
		}
	});
	$("#registerbtn").click(function() {
		if (!$(this).parent().hasClass("ok")) {
			return false
		}
		var e = $("input[name=username]").val();
		var r = $("input[name=pwd]").val();
		var a = $("input[name=password_confirm]").val();
		var i = $("input[name=email]").val();
		var t = "wap";
		if ($.sValid()) {
			$.ajax({
				url: url+"/common/public_key.jhtml",
				type: "GET",
				dataType: "json",
				cache: false,
				beforeSend: function() {
					$("#registerbtn").prop("disabled", true);
				},
				success: function(data) {
					var rsaKey = new RSAKey();
					rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
					var enPassword = hex2b64(rsaKey.encrypt(r));
					$.ajax({
						type : "post",
						url : ApiUrl1 + "/register/submit.jhtml?act=login&op=register",
						data : {
							username : e,
							email : i,
							client : t,
							parentNumber: "mq1688",
							enPassword: enPassword,
						},
						dataType : "json",
						success : function(e) {
							if (e.type=="success") {
								//if (typeof e.datas.key == "undefined") {
									//return false
								//} else {
									//updateCookieCart(e.datas.key);
									//addCookie("username", e.datas.username);
									//addCookie("key", e.datas.key);
									location.href = WapSiteUrl + "//member/index.jhtml"
								//}
								errorTipsHide()
							} else {//注册失败
								errorTipsShow("<p>" + e.content + "</p>")
							}
						}
					})
				}
			});
		}
	})
});