$(function() {
	var e = getCookie("username");
	if (!e) {
		window.location.href = WapSiteUrl + "/login.jhtml";
		return
	}
	var a = getQueryString("sn");
	$.getJSON(ApiUrl + "/order/evaluate_again.jhtml?act=member_evaluate&op=again",{
		key : e,
		sn : a
	},function(r) {
		if (r.message.type!="success") {
			$.sDialog({
				skin : "red",
				content : r.message.content,
				okBtn : false,
				cancelBtn : false
			});
			return false
		}
		var l = template.render("member-evaluation-script", r);
		$("#member-evaluation-div").html(l);
		$('input[name="file"]').ajaxUploadImage({
			url : ApiUrl + "/upload.jhtml?act=sns_album&op=file_upload",
			data : {
				key : e
			},
			start : function(e) {
				e.parent().after('<div class="upload-loading"><i></i></div>');
				e.parent().siblings(".pic-thumb").remove()},
				success : function(e, a) {
//checkLogin(a.login);
				if (a.message.type!="success") {
					e.parent().siblings(".upload-loading").remove();
					$.sDialog({
						skin : "red",
						content : a.message.content,
						okBtn : false,
						cancelBtn : false
					});
					return false
				}
				e.parent().after('<div class="pic-thumb"><img src="' + a.url + '"/></div>');
				e.parent().siblings(".upload-loading").remove();
				e.parents("a").next().val(a.url)
			}
		});
		$(".star-level").find("i").click(function() {
			var e = $(this).index();
			for (var a = 0; a < 5; a++) {
				var r = $(this).parent().find("i").eq(a);
				if (a <= e) {
					r.removeClass("star-level-hollow").addClass("star-level-solid")
				} else {
					r.removeClass("star-level-solid").addClass("star-level-hollow")
				}
			}
			$(this).parent().next().val(e + 1)
		});
		$(".btn-l").click(function() {
			var r = $("form").serializeArray();
			var l = {};
			l.key = e;
			l.order_id = a;
			for (var t = 0; t < r.length; t++) {
				l[r[t].name] = r[t].value
			}
			$.ajax({
				type : "post",
				url : ApiUrl + "/order/evaluate/save_again.jhtml?act=member_evaluate&op=save_again",
				data : l,
				dataType : "json",
				async : false,
				success : function(e) {
//checkLogin(e.login);
					if (e.datas.error) {
						$.sDialog({
							skin : "red",
							content : e.datas.error,
							okBtn : false,
							cancelBtn : false
						});
						return false
					}
					window.location.href = WapSiteUrl + "/member/order/list.jhtml"
				}
			})
		})
	})
});