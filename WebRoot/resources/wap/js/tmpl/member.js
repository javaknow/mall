$(function(){
    if (getQueryString('key') != '') {
        var key = getQueryString('key');
        var username = getQueryString('username');
        addCookie('key', key);
        addCookie('username', username);
        updateCookieCart(key);
    } else {
        var key = getCookie('username');
    }
    
	if(key){//如果用户登录了 获取相关数据
		loadingShow();
        $.ajax({
            type:'post',
            url:ApiUrl+"/member/member_index.jhtml?act=member_index",
            data:{key:key},
            dataType:'json',
            //jsonp:'callback',
            success:function(result){
                checkLogin(result.login);
                var html = '<div class="member-info">'
                    + '<div class="user-avatar"> <img src="' + result.avatar + '"/> </div>'
                    + '<div class="user-name"> <span>'+result.username+'<sup>' + result.memberRank + '</sup></span> </div>'
                    + '</div>'
                    + '<div class="member-collect"><span><a href="favorites/favorites.jhtml"><em>' + result.favorites_goods + '</em>'
                    + '<p>商品收藏</p>'
                    + '</a> </span><span><a href="favorites/store.jhtml"><em>' +result.favorites_store + '</em>'
                    + '<p>店铺收藏</p>'
                    + '</a> </span><span><a href="history.jhtml"><i class="goods-browse"></i>'
                    + '<p>我的足迹</p>'
                    + '</a> </span></div>';
                //渲染页面
                
                $(".member-top").html(html);
                
                var html = '<li><a href="order/list.jhtml?data-state=state_new">'+ (result.waitingPaymentOrderCount > 0 ? '<em></em>' : '') +'<i class="cc-01"></i><p>待付款</p></a></li>'
                    + '<li><a href="order/list.jhtml?data-state=state_send">' + (result.waitingPaymentOrderCount > 0 ? '<em></em>' : '') + '<i class="cc-02"></i><p>待收货</p></a></li>'
                    + '<li><a href="order/list.jhtml?data-state=state_notakes">' + (result.waitingPaymentOrderCount > 0 ? '<em></em>' : '') + '<i class="cc-03"></i><p>待自提</p></a></li>'
                    + '<li><a href="order/list.jhtml?data-state=state_noeval">' + (result.waitingPaymentOrderCount > 0 ? '<em></em>' : '') + '<i class="cc-04"></i><p>待评价</p></a></li>'
                    + '<li><a href="member/refund.jhtml">' + (result.waitingPaymentOrderCount > 0 ? '<em></em>' : '') + '<i class="cc-05"></i><p>退款/退货</p></a></li>';
                //渲染页面
                
                $("#order_ul").html(html);
                
                var html = '<li><a href="deposit/predepositlog_list.jhtml"><i class="cc-06"></i><p>预存款</p></a></li>'
                    + '<li><a href="recharge/rechargecardlog_list.jhtml"><i class="cc-07"></i><p>充值卡</p></a></li>'
                    + '<li><a href="voucher/list.jhtml"><i class="cc-08"></i><p>代金券</p></a></li>'
                    + '<li><a href="redPacket/list.jhtml"><i class="cc-09"></i><p>红包</p></a></li>'
                    + '<li><a href="pointList.jhtml"><i class="cc-10"></i><p>积分</p></a></li>';
                $('#asset_ul').html(html);
                loadingHide();
                return false;
            }
        });
	} else {
		loadingShow();
	    var html = '<div class="member-info">'
	        + '<a href="../login.jhtml" class="default-avatar" style="display:block;"></a>'
	        + '<a href="../login.jhtml" class="to-login">点击登录</a>'
	        + '</div>'
	        + '<div class="member-collect"><span><a href="../login.jhtml"><i class="favorite-goods"></i>'
	        + '<p>商品收藏</p>'
	        + '</a> </span><span><a href="../login.jhtml"><i class="favorite-store"></i>'
	        + '<p>店铺收藏</p>'
	        + '</a> </span><span><a href="../login.jhtml"><i class="goods-browse"></i>'
	        + '<p>我的足迹</p>'
	        + '</a> </span></div>';
	    //渲染页面
	    $(".member-top").html(html);
	    
        var html = '<li><a href="../login.jhtml"><i class="cc-01"></i><p>待付款</p></a></li>'
        + '<li><a href="../login.jhtml"><i class="cc-02"></i><p>待收货</p></a></li>'
        + '<li><a href="../login.jhtml"><i class="cc-03"></i><p>待自提</p></a></li>'
        + '<li><a href="../login.jhtml"><i class="cc-04"></i><p>待评价</p></a></li>'
        + '<li><a href="../login.jhtml"><i class="cc-05"></i><p>退款/退货</p></a></li>';
        //渲染页面
        $("#order_ul").html(html);
        loadingHide();
        return false;
	}

	  //滚动header固定到顶部
	  $.scrollTransparent();
});