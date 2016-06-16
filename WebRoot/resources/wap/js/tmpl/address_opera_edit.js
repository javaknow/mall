$(function() {
	var a = getQueryString("address_id");
	var e = getCookie("username");
	$.ajax({
		type : "post",
		url : ApiUrl + "/receiver/address_info.jhtml?act=member_address&op=address_info",
		data : {
			key : e,
			address_id : a
		},
		dataType : "json",
		success : function(a) {
//checkLogin(a.login);
			if(a.message.type=="success"){
				$("#true_name").val(a.address_info.consignee);
				$("#mob_phone").val(a.address_info.phone);
				$("#area_info").val(a.address_info.areaName).attr({
					"data-areaid" : a.address_info.area.id,
					"data-areaid2" : a.address_info.area.id
				});
				$("#zipCode").val(a.address_info.zipCode);
				$("#address").val(a.address_info.address);
				var e = a.address_info.isDefault;
				$("#is_default").prop("checked", e);
				if (e) {
					$("#is_default").parents("label").addClass("checked")
				}
			}else{
				errorTipsShow(a.message.content);
			}
			
		}
	});
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
			zipCode : "邮编必填！"
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
			var o = $("#is_default").attr("checked") ? true : false;
			$.ajax({
				type : "post",
				url : ApiUrl+ "/receiver/address_edit.jhtml?act=member_address&op=address_edit",
				data : {
					key : e,
					consignee : r,
					phone : d,
					city_id : s,
					areaId : t,
					address : i,
					area_info : n,
					zipCode : zipCode,
					isDefault : o,
					id : a
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