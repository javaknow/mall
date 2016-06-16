$(function(){
    var key = getCookie('username');
    if (key) {
        window.location.href = WapSiteUrl+'index.jhtml';
        return;
    }
    
    //获取合作登陆的方式
    $.getJSON(ApiUrl + '/login/get_state.jhtml?act=connect&op=get_state', function(result){
        var ua = navigator.userAgent.toLowerCase();
        var allow_login = 0;
        if (result.datas.pc_qq == '1') {
            allow_login = 1;
            $('.qq').parent().show();
        }
        if (result.datas.pc_sn == '1') {
            allow_login = 1;
            $('.weibo').parent().show();
        }
        if ((ua.indexOf('micromessenger') > -1) && result.datas.connect_wap_wx == '1') {
            allow_login = 1;
            $('.wx').parent().show();
        }
        if (allow_login) {
            $('.joint-login').show();
        }
    });
	var referurl = document.referrer;//上级网址
	$.sValid.init({
        rules:{
            username:"required",
            userpwd:"required"
        },
        messages:{
            username:"用户名必须填写！",
            userpwd:"密码必填!"
        },
        callback:function (eId,eMsg,eRules){
            if(eId.length >0){
                var errorHtml = "";
                $.map(eMsg,function (idx,item){
                    errorHtml += "<p>"+idx+"</p>";
                });
                errorTipsShow(errorHtml);
            }else{
                errorTipsHide();
            }
        }  
    });
    var allow_submit = true;
	$('#loginbtn').click(function(){//会员登陆
        if (!$(this).parent().hasClass('ok')) {
            return false;
        }
        if (allow_submit) {
            allow_submit = false;
        } else {
            return false;
        }
		var username = $('#username').val();
		var pwd = $('#userpwd').val();
		var client = 'wap';
		if($.sValid()){
			$.ajax({
				url: url+"/common/public_key.jhtml",
				type: "GET",
				dataType: "json",
				cache: false,
				beforeSend: function() {
					$("#loginbtn").prop("disabled", true);
				},
				success: function(data) {
					var rsaKey = new RSAKey();
					rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
					var enPassword = hex2b64(rsaKey.encrypt(pwd));
					 $.ajax({
							type:'post',
							url:ApiUrl1+"/login/submit.jhtml?act=login",	
							data:{username:username,enPassword:enPassword,client:client},
							dataType:'json',
							success:function(result){
							    allow_submit = true;
								if(result.type=="success"){
									if(typeof(result.type)=='undefined'){
										return false;
									}else{
									    //var expireHours = 0;
									   // if ($('#checkbox').prop('checked')) {
									    //    expireHours = 188;
									   // }
									    // 更新cookie购物车
									    //updateCookieCart(result.datas.key);
										//addCookie('username',result.datas.username, expireHours);
										//addCookie('key',result.datas.key, expireHours);
										location.href = referurl;
									}
					                errorTipsHide();
								}else{
					                errorTipsShow('<p>' + result.content + '</p>');
								}
							}
						 });  
				}
			});
        }
	});
	
	$('.weibo').click(function(){
	    location.href = ApiUrl+'/index.php?act=connect&op=get_sina_oauth2';
	})
    $('.qq').click(function(){
        location.href = ApiUrl+'/index.php?act=connect&op=get_qq_oauth2';
    })
    $('.wx').click(function(){
        location.href = ApiUrl+'/index.php?act=connect&op=index';
    })
});