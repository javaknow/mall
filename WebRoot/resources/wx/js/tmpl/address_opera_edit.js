$(function() {
	var a = getQueryString("address_id");
	var e = getCookie("username");
	$.ajax({
		type : "get",
		url : ApiUrl + "/member/receiver/edit.jhtml",
		data : {
			key : e,
			id : a
		},
		dataType : "json",
		success : function(a) {
			console.info(a.datas.area);
			//checkLogin(a.login);
			$("#true_name").val(a.datas.consignee);
			$("#mob_phone").val(a.datas.phone);
			$("#zipCode").val(a.datas.zipCode);
			$("#area_info").val(a.datas.areaName).attr({
				"data-areaid" : a.datas.area.id,
				"data-areaid2" : a.datas.area.id
			});
			$("#address").val(a.datas.address);
			var e = a.datas.isDefault;
			$("#is_default").prop("checked", e);
			if (e) {
				$("#is_default").parents("label").addClass("checked")
			}
		}
	});
	$.sValid.init({
		rules : {
			true_name : "required",
			mob_phone : "required",
			area_info : "required",
			address : "required",
			zipCode: "required"
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
				var d = "";
				$.map(e, function(a, e) {
					d += "<p>" + a + "</p>"
				});
				errorTipsShow(d)
			} else {
				errorTipsHide()
			}
		}
	});
	$(".btn").click(function() {
		if ($.sValid()) {
			var r = $("#true_name").val();
			var d = $("#mob_phone").val();
			var i = $("#address").val();
			var s = $("#area_info").attr("data-areaid2");
			var t = $("#area_info").attr("data-areaid");
			var n = $("#area_info").val();
			var zipCode = $("#zipCode").val();
			var o = $("#is_default").attr("checked") ? 1 : 0;
			alert("abc");
			alert(ApiUrl+ "/area/update.jhtml");
			$.ajax({
				type : "post",
				url : ApiUrl+ "/area/update.jhtml",
				data : {
					key : e,
					consignee : r,
					phone : d,
					city_id : s,
					areaId : t,
					address : i,
					area_info : n,
					is_default : o,
					id : a,
					zipCode : zipCode
				},
				dataType : "json",
				success : function(a) {
					if (a) {
						location.href = WapSiteUrl+ "/member/receiver/list.jhtml"
					} else {
						location.href = WapSiteUrl
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