//加载购物车信息
function load_cart_information1(){
	$.ajax({
		url: igomall.base+"/cart.jhtml",
		type: "POST",
		cache: false,
		success: function(data) {
			$(".my-cart-dd").html(data);
		}
	});
}

//头部删除购物车信息
function drop_topcart_item1(cartItem_id){
	$.ajax({
		url: igomall.base+"/cart/delete.jhtml",
		type: "POST",
		dataType: "json",
		data:{id:cartItem_id},
		cache: false,
		success: function(data) {
			if(data.message.type=="success"){
				load_cart_information();
			}else{
				
			}
		}
	});
}

//侧边栏购物车的加载
function load_cart_information2(){
	$.ajax({
		url: igomall.base+"/getCartInfo.jhtml",
		type: "POST",
		cache: false,
		success: function(data) {
			$("#rtoolbar_cartlist").html(data);
		}
	});
}

//侧边栏购物车的删除
function drop_topcart_item3(cartItem_id){
	$.ajax({
		url: igomall.base+"/cart/delete.jhtml",
		type: "POST",
		dataType: "json",
		data:{id:cartItem_id},
		cache: false,
		success: function(data) {
			if(data.message.type=="success"){
				load_cart_information2();
			}else{
				
			}
		}
	});
}

function getMemberInfo(dataType,barInfo){
	$.ajax({
		url: igomall.base+"/userInfo.jhtml",
		type: "POST",
		data: {pageType:dataType},
		cache: false,
		success: function(data) {
			$("div[nctype='"+barInfo+"']").after(data);
		}
	});	
}

function checkMember(){
	$.ajax({
		url: igomall.base+"/checkMember.jhtml",
		type: "POST",
		dataType: "json",
		cache: false,
		success: function(data) {
			var dataType;
			if(data==null){
				$("div[class='user']").attr("nctype","a-barLoginBox");
				dataType="loginInfo";
				$("span[class='tit']").text("会员登陆");
				if($("div[nctype='a-barLoginBox']").hasClass("nologin")){
					$("div[nctype='a-barLoginBox']").removeClass("nologin");
					$("div[nctype='barLoginBox']").remove();
				}else{
					$("div[nctype='a-barLoginBox']").addClass("nologin");
					getMemberInfo(dataType,"a-barLoginBox");
				}
				
			}else{
				$("div[class='user']").attr("nctype","a-barUserInfo");
				$("span[class='tit']").text("我的账户");
				dataType="userInfo";
				
				if($("div[nctype='a-barUserInfo']").hasClass("login")){
					$("div[nctype='a-barUserInfo']").removeClass("login");
					$("div[nctype='barUserInfo']").remove();
				}else{
					$("div[nctype='a-barUserInfo']").addClass("login");
					getMemberInfo(dataType,"a-barUserInfo");
				}
			}
		}
	});		
}