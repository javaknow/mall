var goods_cod = "0";
var store_price = "133.00";
jQuery(document).ready(function() {

});
jQuery(function() {

    if (goods_cod == "0") {
        jQuery("#show_count").html("x" + jQuery("#count").val() + "&nbsp&nbsp货到付款");
    } else {
        jQuery("#show_count").html("x" + jQuery("#count").val());
    }
    jQuery("#show_spec").click(function(e) {
        if (jQuery("#spec_count").css("display") == "block") {
            jQuery("#img_spec").attr("src", "../images/details_right.gif");
            jQuery("#img_spec").attr("width", "9");
            jQuery("#img_spec").attr("height", "18");
            if (goods_cod == "0") {
                jQuery("#show_count").html("x" + jQuery("#count").val() + "&nbsp&nbsp货到付款");
            } else {
                jQuery("#show_count").html("x" + jQuery("#count").val());
            }
        } else {
            jQuery("#img_spec").attr("src", "../images/details_right_b.gif");
            jQuery("#img_spec").attr("width", "18");
            jQuery("#img_spec").attr("height", "9");
            if (goods_cod == "0") {
                jQuery("#show_count").html("x" + jQuery("#count").val() + "&nbsp&nbsp货到付款");
            } else {
                jQuery("#show_count").html("x" + jQuery("#count").val());
            }

        }
        jQuery("#spec_count").toggle();
    });

    jQuery(".details_standard>a").click(function() {
        jQuery(".standard_bottom").slideToggle();
        if (jQuery(this).find("ul").is(".standard_active")) {
            jQuery(this).find("ul").removeClass("standard_active");
        } else {
            jQuery(this).find("ul").addClass("standard_active");
        }
    });
    jQuery(".standard_check").not(":eq(0)").each(function() {
        var th = jQuery(this).find("a").first();
        goods_spec_set(th);
    });
    
});

function goods_spec_set(obj) {
    var spec = jQuery(obj).attr("spec");
    var gsp = "";
    var sname = "";
    var load = true;
    var count = 0;
    jQuery(" li[mark^=spec_] a[spec=" + spec + "]").each(function(index, element) {
        jQuery(element).removeClass("this");
    });
    jQuery(obj).addClass("this");
    jQuery("li[mark^=spec]").each(function() {
        jQuery.each(jQuery(this).find("a[class=this]"),
        function() {
            gsp = jQuery(this).attr("gsp") + "," + gsp;
            if (count == 0) {
                sname = " " + jQuery(this).attr("sname") + " ";
            } else {
                sname = " " + jQuery(this).attr("sname") + " " + sname;
            }
            count++;
        });
    });
    jQuery("li[mark^=spec_]").each(function() {
        if (jQuery(this).find("a[class=this]").length == 0) {
        	load = false;
        }
    });
    if (sname == "") {} else {
        jQuery("#show_spec span[class=fl]").html(sname);
    }
    if (load) {
        jQuery.post("http://www.i-gomall.com/wap/load_goods_gsp.htm", {
            "id": "96",
            "gsp": gsp
        },
        function(data) {
            goods_inventory = data.count;
            store_price = data.price;
            if (data.act_price != null) {
                act_price = data.act_price;
                jQuery("#act_price").html("¥" + act_price);
            }
            jQuery("#goods_inventory").attr("inventory", goods_inventory);
            jQuery("#goods_inventory").html(goods_inventory + "件");
            var goods_count = jQuery("#count").val();
            if (goods_inventory < goods_count) {
                jQuery("#count").val(goods_inventory);
            }
            jQuery("#store_price").html("¥" + store_price);
        },
        "json");
    }
}
function add_cart() {
    var add = true
    var gsp = "";
    jQuery("li[mark^=spec_]").each(function() {
        if (jQuery(this).find("a[class=this]").length == 0) add = false;
    });
    if (add) {
        jQuery("li[mark^=spec_]").each(function() {
            gsp = jQuery(this).find("a[class=this]").attr("gsp") + "," + gsp;
        });
    }

    if (add) {
        var count = jQuery("#count").val() - 0;
        var goods_inventory = jQuery("#goods_inventory").attr("inventory") - 0;
        if (goods_inventory != 0) {
            if (goods_inventory < count) {
                count = goods_inventory;
            }
            jQuery.ajax({
                type: 'POST',
                url: 'http://www.i-gomall.com/wap/add_goods_cart.htm',
                data: {
                    "id": "96",
                    "price": store_price,
                    "gsp": gsp,
                    "count": count
                },
                success: function(data) {
                    window.location.href = "http://www.i-gomall.com/wap/goods_cart0.htm"
                }
            });
        }
    } else {
        alert("请选择规格！")
    }
}
function buy_goods() {
    var add = true
    var gsp = "";
    jQuery("li[mark^=spec_]").each(function() {
        if (jQuery(this).find("a[class=this]").length == 0) add = false;
    });
    if (add) {
        jQuery("li[mark^=spec_]").each(function() {
            gsp = jQuery(this).find("a[class=this]").attr("gsp") + "," + gsp;
        });
    }
    if (add) {
        var count = jQuery("#count").val() - 0;
        var goods_inventory = jQuery("#goods_inventory").attr("inventory") - 0;
        if (goods_inventory != 0) {
            if (goods_inventory < count) {
                count = goods_inventory;
            }
            jQuery.ajax({
                type: 'POST',
                url: 'http://www.i-gomall.com/wap/add_goods_cart.htm',
                data: {
                    "id": "96",
                    "price": store_price,
                    "gsp": gsp,
                    "count": count
                },
                success: function(data) {
                    window.location.href = "http://www.i-gomall.com/wap/goods_cart1.htm";
                }
            });
        }

    } else {
        alert("请选择规格！")
    }
}

jQuery(function() {
    jQuery("#count").keyup(function(e) {
        var count = jQuery("#count").val().replace(/\D/g, '');
        if (count == "") {
            count = 1;
        }
        var goods_inventory = jQuery("#goods_inventory").attr("goods_inventory") - 0;
        if (count > goods_inventory) {
            jQuery("#count").val(goods_inventory);
        } else if (count < 1) {
            jQuery("#count").val(1);
        } else {
            jQuery("#count").val(count);
        }
        if (goods_cod == "0") {
            jQuery("#show_count").html("x" + jQuery("#count").val() + "&nbsp&nbsp货到付款");
        } else {
            jQuery("#show_count").html("x" + jQuery("#count").val());
        }
    });

});
function query_area_data(area_1) {
    var aid = jQuery("#area_1").val();
    jQuery.ajax({
        type: 'POST',
        url: 'http://www.i-gomall.com/wap/buyer/address_add_ajax.htm',
        data: {
            "aid": aid
        },
        success: function(data) {
            var dataObj = eval("(" + data + ")");
            jQuery(".brand_class_box ul").html("");
            var html = "";
            jQuery(dataObj.data).each(function(index) {
                html = html + "<option value=" + dataObj.data[index].addr_name + ">" + dataObj.data[index].addr_name + "</option>"
            });
            jQuery("#city").html(html);
            jQuery(".sel1").show();
        }
    },
    "json");
}
function trans_fee() {
    var city_name = jQuery("#city").val();
    jQuery.ajax({
        type: 'POST',
        url: 'http://www.i-gomall.com/trans_fee.htm',
        dataType: 'json',
        data: {
            "city_name": city_name,
            "goods_id": "96"
        },
        success: function(json) {
            jQuery("#trans_fee").show();
        }
    });
}
function plus() {
    var cc = jQuery("#count").val() - 0;
    var goods_inventory = jQuery("#goods_inventory").attr("goods_inventory") - 0;
    if (cc >= goods_inventory) {
        jQuery("#count").val(goods_inventory);
    } else {
        jQuery("#count").val(cc + 1);
    }
    if (goods_cod == "0") {
        jQuery("#show_count").html("x" + jQuery("#count").val() + "&nbsp&nbsp货到付款");
    } else {
        jQuery("#show_count").html("x" + jQuery("#count").val());
    }
}
function minus() {
    var cc = jQuery("#count").val() - 0;
    if (cc <= 1) {
        jQuery("#count").val(1);
    } else {
        jQuery("#count").val(cc - 1);
    }
    if (goods_cod == "0") {
        jQuery("#show_count").html("x" + jQuery("#count").val() + "&nbsp&nbsp货到付款");
    } else {
        jQuery("#show_count").html("x" + jQuery("#count").val());
    }
}