$(function() {
	var e = getCookie("username");
	if (!e) {
		window.location.href = WapSiteUrl + "/login.jhtml";
		return
	}
	$.ajax({
		type : "get",
		url : ApiUrl + "/modify_password_step4.jhtml?act=member_account&op=modify_password_step4",
		data : {
			key : e
		},
		dataType : "json",
		success : function(e) {
			if (e.code != 200) {
				errorTipsShow("<p>"+e.message.content+"</p>");
				setTimeout("location.href = WapSiteUrl+'/member/member_password_step1.jhtml'",2e3)
			}
		}
	});
	$.sValid.init({
		rules : {
			password : {
				required : true,
				minlength : 6,
				maxlength : 20
			},
			password1 : {
				required : true,
				equalTo : "#password"
			}
		},
		messages : {
			password : {
				required : "请填写登录密码",
				minlength : "请正确填写登录密码",
				maxlength : "请正确填写登录密码"
			},
			password1 : {
				required : "请填写确认密码",
				equalTo : "两次密码输入不一致"
			}
		},
		callback : function(e, r, a) {
			if (e.length > 0) {
				var s = "";
				$.map(r, function(e, r) {
					s += "<p>" + e + "</p>"
				});
				errorTipsShow(s)
			} else {
				errorTipsHide()
			}
		}
	});
	$("#nextform").click(function() {
		if (!$(this).parent().hasClass("ok")) {
			return false
		}
		if ($.sValid()) {
			var r = $.trim($("#password").val());
			var a = $.trim($("#password1").val());
			$.ajax({
				type : "post",
				url : ApiUrl + "/modify_password_step5.jhtml?act=member_account&op=modify_password_step5",
				data : {
					key : e,
					password : r,
					password1 : a
				},
				dataType : "json",
				success : function(e) {
					if (e.code == 200) {
						$.sDialog({
							skin : "block",
							content : "密码修改成功",
							okBtn : false,
							cancelBtn : false
						});
						setTimeout("location.href = WapSiteUrl+'/member/member_account.jhtml'",2e3)
					} else if(e.code == 201){
						errorTipsShow("<p>"+e.message.content+"</p>");
						setTimeout("location.href = WapSiteUrl+'/member/member_password_step1.jhtml'",2e3)
					}else{
						errorTipsShow("<p>"+e.message.content+"</p>");
					}
				}
			})
		}
	})
});