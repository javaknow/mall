$(function() {
	var a = getCookie("username");
	$.sValid.init({
		rules : {
			true_name : "required",
			mob_phone : "required",
			area_info : "required",
			address : "required",
			zipCode : "required"
		},
		messages : {
			true_name : "姓名必填！",
			mob_phone : "手机号必填！",
			area_info : "地区必填！",
			address : "街道必填！",
			zipCode : "邮编必填"
		},
		callback : function(a, e, r) {
			if (a.length > 0) {
				var i = "";
				$.map(e, function(a, e) {
					i += "<p>" + a + "</p>"
				});
				errorTipsShow(i)
			} else {
				errorTipsHide()
			}
		}
	});
	$("#header-nav").click(function() {
		$(".btn").click()
	});
	$(".btn").click(function() {
		if ($.sValid()) {
			$("a[class='btn']").prop("disabled", true);
			var e = $("#true_name").val();
			var r = $("#mob_phone").val();
			var i = $("#address").val();
			var zipCode = $("#zipCode").val();
			var d = $("#area_info").attr("data-areaid2");
			var t = $("#area_info").attr("data-areaid");
			var n = $("#area_info").val();
			var o = $("#is_default").attr("checked") ? true : false;
			$.ajax({
				type : "post",
				url : ApiUrl+ "/receiver/address_add.jhtml?act=member_address&op=address_add",
				data : {
					key : a,
					consignee : e,
					phone : r,
					areaId : t,
					address : i,
					zipCode : zipCode,
					area_info : n,
					isDefault : o
				},
				dataType : "json",
				success : function(a) {
					if (a.message.type=="success") {
						location.href = WapSiteUrl+ "/member/receiver/list.jhtml"
					} else {
						//location.href = WapSiteUrl
						errorTipsShow(a.message.content);
					}
				}
			})
		}
	});
	$("#area_info").on("click",function() {
		$.areaSelected({
			success : function(a) {
				$("#area_info").val(a.area_info).attr({
					"data-areaid" : a.area_id,
					"data-areaid2" : a.area_id_2 == 0 ? a.area_id_1: a.area_id_2
				})
			}
		})
	})
});