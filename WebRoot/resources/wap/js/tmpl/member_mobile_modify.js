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
					$("#mobile").html(e.mobile)
				} else {
					location.href = WapSiteUrl+ "/member/member_mobile_bind.jhtml"
				}
			}
		}
	});
	$.sValid.init({
		rules : {
			captcha : {
				required : true,
				minlength : 4
			}
		},
		messages : {
			captcha : {
				required : "请填写图形验证码",
				minlength : "图形验证码不正确"
			}
		},
		callback : function(e, a, t) {
			if (e.length > 0) {
				var o = "";
				$.map(a, function(e, a) {
					o += "<p>" + e + "</p>"
				});
				errorTipsShow(o)
			} else {
				errorTipsHide()
			}
		}
	});
	$("#send").click(function() {
		if ($.sValid()) {
			var a = $.trim($("#captcha").val());
			var t = $.trim($("#codekey").val());
			$.ajax({
				type : "post",
				url : ApiUrl + "/modify_mobile_step2.jhtml?act=member_account&op=modify_mobile_step2",
				data : {
					key : e,
					captcha : a,
					captchaId : t
				},
				dataType : "json",
				success : function(e) {
					if (e.code == 200) {
						$("#send").hide();
						$(".code-countdown").show().find("em").html(e.sms_time);
						$.sDialog({
							skin : "block",
							content : "短信验证码已发出",
							okBtn : false,
							cancelBtn : false
						});
						var a = setInterval(function() {
							var e = $(".code-countdown").find("em");
							var t = parseInt(e.html() - 1);
							if (t == 0) {
								$("#send").show();
								$(".code-countdown").hide();
								clearInterval(a);
								//$("#codeimage").attr("src",ApiUrl+ "/makecode.jhtml?act=seccode&op=makecode&captchaId=" + $( "#codekey") .val() + "&t=" + Math.random())
							} else {
								e.html(t)
							}
						}, 1e3)
					} else {
						errorTipsShow("<p>" + e.datas.error + "</p>"); 
						$("#codeimage").attr("src",ApiUrl + "/makecode.jhtml?act=seccode&op=makecode&captchaId=" + $("#codekey") .val() + "&t=" + Math .random());
						$("#captcha").val("")
					}
				}
			})
		}
	});
	
	$("#nextform").click(function() {
		if (!$(this).parent().hasClass("ok")) {
			return false
		}
		var a = $.trim($("#auth_code").val());
		if (a) {
			$.ajax({
				type : "post",
				url : ApiUrl + "/modify_mobile_step3.jhtml?act=member_account&op=modify_mobile_step3",
				data : {
					key : e,
					auth_code : a
				},
				dataType : "json",
				success : function(e) {
					if (e.code == 200) {
						$.sDialog({
							skin : "block",
							content : "解绑成功",
							okBtn : false,
							cancelBtn : false
						});
						setTimeout( "location.href = WapSiteUrl+'/member/member_mobile_bind.jhtml'", 2e3)
					} else {
						errorTipsShow("<p>" + e.message.content + "</p>")
					}
				}
			})
		}
	})
});