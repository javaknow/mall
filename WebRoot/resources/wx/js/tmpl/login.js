$(function() {
	var e = getCookie("key");
	if (e) {
		window.location.href = WapSiteUrl + "/tmpl/member/member.html";
		return
	}
	var r = document.referrer;
	$.sValid.init({
		rules : {
			username : "required",
			userpwd : "required"
		},
		messages : {
			username : "用户名必须填写！",
			userpwd : "密码必填!"
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
	var a = true;
	$("#loginbtn").click(function() {
		if (!$(this).parent().hasClass("ok")) {
			return false
		}
		if (a) {
			a = false
		} else {
			return false
		}
		var e = $("#username").val();
		var i = $("#userpwd").val();
		var t = "wap";
		if ($.sValid()) {
			$.ajax({
				type : "post",
				url : WapSiteUrl + "/login/submit.jhtml",
				data : {
					username : e,
					password : i,
					client : t
				},
				dataType : "json",
				success : function(e) {
					a = true;
					if (e.msg.type=="success") {
						if (typeof e.msg.content == "undefined") {
							return false
						} else {
							var i = 0;
							if ($("#checkbox").prop("checked")) {
								i = 188
							}
							location.href = WapSiteUrl + "/index/index.jhtml";
						}
						errorTipsHide()
					} else {
						errorTipsShow("<p>" + e.msg.content + "</p>")
					}
				}
			})
		}
	});
	$(".weibo").click(function() {
		location.href = ApiUrl + "/index.php?act=connect&op=get_sina_oauth2"
	});
	$(".qq").click(function() {
		location.href = ApiUrl + "/index.php?act=connect&op=get_qq_oauth2"
	})
});