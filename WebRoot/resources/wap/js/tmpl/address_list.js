$(function() {
	var e = getCookie("username");
	if (!e) {
		location.href = WapSiteUrl+"/login.jhtml"
	}
	
	//加载地址列表
	function s() {
		$.ajax({
			type : "post",
			url : ApiUrl + "/receiver/address_list.jhtml?act=member_address&op=address_list",
			data : {
				key : e
			},
			dataType : "json",
			success : function(e) {
				//checkLogin(e.login);
				
				if (e.address_list == null) {
					return false
				}
				var s = e;
				var t = template.render("saddress_list", s);
				
				$("#address_list").empty();
				$("#address_list").append(t);
				$(".deladdress").click(function() {
					var e = $(this).attr("address_id");
					$.sDialog({
						skin : "block",
						content : "确认删除吗？",
						okBtn : true,
						cancelBtn : true,
						okFn : function() {
							a(e)
						}
					})
				})
			}
		})
	}
	s();
	
	//地址的删除
	function a(a) {
		$.ajax({
			type : "post",
			url : ApiUrl + "/receiver/delete.jhtml",
			data : {
				address_id : a,
				key : e
			},
			dataType : "json",
			success : function(e) {
//checkLogin(e.login);
				if (e.message.type=="success") {
					s()
				}
			}
		})
	}
});