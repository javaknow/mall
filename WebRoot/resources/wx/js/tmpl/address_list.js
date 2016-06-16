$(function() {
	var e = getCookie("key");
	//if (!e) {
		//location.href = "login.html"
	//}
	
	function s() {
		$.ajax({
			type : "get",
			url : ApiUrl + "/receiver/list.jhtml",
			data : {
				key : e
			},
			dataType : "json",
			success : function(e) {
				//checkLogin(e.login);
				console.info(e);
				console.info(e.datas);
				if (e.datas == null) {
					return false
				}
				var s = e.datas;
				var t = template.render("saddress_list", e);
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