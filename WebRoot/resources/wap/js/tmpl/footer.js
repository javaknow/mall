$(function (){
    if (getQueryString('key') != '') {
        var key = getQueryString('key');
        var username = getQueryString('username');
        addCookie('key', key);
        addCookie('username', username);
    } else {
        var key = getCookie('username');
    }
    var html = '<div class="nctouch-footer-wrap posr">'
        +'<div class="nav-text">';
    if(key){
        html += '<a href="'+WapSiteUrl+'/member/index.jhtml">我的商城</a>'
            + '<a id="logoutbtn" href="javascript:void(0);">注销</a>'
            + '<a href="'+WapSiteUrl+'/member/member_feedback.jhtml">反馈</a>';
            
    } else {
        html += '<a href="'+WapSiteUrl+'/login.jhtml">登录</a>'
            + '<a href="'+WapSiteUrl+'/register.jhtml">注册</a>'
            + '<a href="'+WapSiteUrl+'/login.jhtml">反馈</a>';
    }
    html += '<a href="javascript:void(0);" class="gotop">返回顶部</a>'
        +'</div>'
        +'<div class="nav-pic">'
			+'<a href="'+SiteUrl+'/index.php?act=mb_app" class="app"><span><i></i></span><p>客户端</p></a>'
			+'<a href="javascript:void(0);" class="touch"><span><i></i></span><p>触屏版</p></a>'
            +'<a href="'+SiteUrl+'" class="pc"><span><i></i></span><p>电脑版</p></a>'
         +'</div>'
		 +'<div class="copyright">'
		 +'Copyright&nbsp;&copy;&nbsp;2015-2016 爱购商城<a href="javascript:void(0);">i-gomall.com</a>版权所有'
    	 +'</div>';
	$("#footer").html(html);
    var key = getCookie('key');
	$('#logoutbtn').click(function(){
		var username = getCookie('username');
		var key = getCookie('key');
		var client = 'wap';
		$.ajax({
			type:'get',
			url:ApiUrl+'/logout.jhtml?act=logout',
			data:{username:username,key:key,client:client},
			success:function(result){
				if(result.code==200&&result.message.type=="success"){
					delCookie('username');
					delCookie('key');
					location.href = WapSiteUrl+"/index.jhtml";
				}
			}
		});
	});
});