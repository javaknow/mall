jQuery(document).ready(function() {
	// 鼠标经过左侧分类
	jQuery(".sale_two_img img").lazyload({
		effect : "fadeIn",
		width : 170,
		height : 170
	});
	jQuery(".left_menu_dl").mouseover(function() {
		var child_count = jQuery(this).attr("child_count");
		if (child_count > 0) {
			var id = jQuery(this).attr("id");
			jQuery("#dts_" + id).addClass("left_menu_this").removeClass("left_menu_dt");
			jQuery("#child_" + id).show();
		}
		var gc_id = jQuery(this).attr("id");
		var gc_color = jQuery(this).attr("gc_color");
		jQuery("#dts_" + gc_id).attr("style","border:1px solid "+ gc_color+ "; border-left:3px solid "+ gc_color+ ";border-right:none");
		jQuery("#left_menu_con_" + gc_id).attr("style","border:1px solid "+ gc_color+ "; border-left:1px solid "+ gc_color+ ";").find(".menu_con_right_top").css("background-color",gc_color);
		// 设置div高度为每行div中最高的高度
		var begin = 0;
		var end = 2;
		jQuery("#child_" + gc_id).find(".left_menu_con_center_left").each(function() {
			var max_height = 0;
			var index = jQuery(this).index();
			var height = jQuery(this).height();
			if (index > end	|| index < begin) {
				begin = begin + 3;
				end = end + 3;
			}
			if (height > max_height) {
				max_height = height;
			}
			for (var i = begin; i <= end; i++) {
				var temp_height = jQuery("#child_"+ gc_id).find(".left_menu_con_center_left").eq(i).height();
				if (temp_height > max_height) {
					max_height = temp_height;
				}
				jQuery("#child_"+ gc_id).find(".left_menu_con_center_left").eq(i).height(max_height);
			}
		});

		var top = jQuery("#child_" + gc_id).offset().top;
		var scroll_top = jQuery(document).scrollTop();
		var height = jQuery("#left_menu_con_" + gc_id).height();
		var all_h = top - scroll_top + height;
		var doc_h = jQuery(window).height();
		var margin_top = doc_h - all_h;
		if (margin_top <= 5) {
			margin_top = margin_top - 20;
			jQuery("#left_menu_con_" + gc_id).css('margin-top',margin_top + 'px');
		} else {
			jQuery("#left_menu_con_" + gc_id).css('margin-top', '0px');
		}
	}).mouseleave(function() {
		jQuery("dt[id^=dts_]").removeAttr("style");
		jQuery("div[id^=left_menu_con_]").removeAttr("style");
		var child_count = jQuery(this).attr("child_count");
		if (child_count > 0) {
			var id = jQuery(this).attr("id");
			jQuery("#dts_" + id).removeClass("left_menu_this").addClass("left_menu_dt");
			jQuery("#child_" + id).hide();
		}
	});
	// 鼠标经过推荐商品行
	jQuery("#index_sale_tab ul li").mouseover(function() {
		jQuery(this).siblings().removeClass("this");
		jQuery(this).addClass("this");
		var i = jQuery(this).index();
		jQuery("#index_sale_tab").siblings().hide();
		jQuery("#index_sale_tab").siblings().eq(i).show();
		jQuery("#sale_change").attr("mark",jQuery(this).attr("id").replace("goodscase", ""));
	});
	// 鼠标经过楼层标题
	jQuery(".storey_tab ul li").mouseover(function() {
		jQuery(this).siblings().removeClass("this");
		jQuery(this).addClass("this");
		var i = jQuery(this).index();
		jQuery(this).parent().parent().siblings().not(".hot_sell").hide();
		jQuery(this).parent().parent().siblings().eq(i).show();
	});
	var head_h = jQuery("#head_h").height();
	jQuery("#head_unbomb").height(head_h);
	jQuery(window).scroll(function() {
		var top = jQuery(document).scrollTop();
		// 顶部搜索框跟随
		if (top == 0) {
			jQuery("#top").attr("style", "");
		} else {
			jQuery("#top").attr("style","position:fixed;top:0px");
		}
		if (top > head_h) {
			jQuery("#head_h").addClass("head_fixd");
		} else {
			jQuery("#head_h").removeClass("head_fixd");
		}
		// 楼层导航跟随
		jQuery("li[floor_id^=floor_] b").css("display","block");
		jQuery("div[id^=floor_]").each(function() {
			var floor_top = jQuery(this).offset().top- top;
			if (floor_top <= 580&& floor_top >= 0) {
				var floor_id = jQuery(this).attr("id");
				jQuery("li[floor_id="+ floor_id+ "] b").css("display", "none");
			}
		});
	});
	// 右上角公告切换
	jQuery(".top_mr_tab li").mouseover(function() {
		jQuery(this).siblings().removeClass("this");
		jQuery(this).addClass("this");
		var i = jQuery(this).index();
		jQuery(".top_mr_box").hide();
		jQuery(".top_mr_box").eq(i).show();
	});

	// 用户登录、商家入驻tab页切换
	jQuery("#top_mid_login_tab>li").css("cursor", "pointer").mouseover(function() {
		var id = jQuery(this).attr("id");
		jQuery("#top_mid_login_tab>li").removeClass("this");
		jQuery(this).addClass("this");
		if (id == "top_mid_login_tab_user") {
			jQuery("#top_mid_login_tab_seller_info").hide();
			jQuery("#top_mid_login_tab_user_info").show();
		}
		if (id == "top_mid_login_tab_seller") {
			jQuery("#top_mid_login_tab_user_info").hide();
			jQuery("#top_mid_login_tab_seller_info").show();
		}
	});

	$.ajax({
		type : "POST",
		url : "userInfo.jhtml",
		data : "",
		success : function(data) {
			$("#userInfo").empty();
			$("#userInfo").html(data);
		}
	});
});