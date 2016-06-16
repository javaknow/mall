
package com.igomall.wechat;

public final class CommonWeChatAttributes {
	
	/**
	 * 事件的类型
	 * @author blackboy
	 *
	 */
	public enum EventType{
		
		SUBSCRIBE,//订阅。对应的MsyType是EVENT
		
		UNSUBSCRIBE,//取消订阅。对应的MsyType是EVENT
		
		LOCATION,//上报地理位置事件。对应的MsyType是EVENT
		
		CLICK,//点击菜单拉取消息时的事件推送。对应的MsyType是EVENT
		
		VIEW,//点击菜单跳转链接时的事件推送。对应的MsyType是EVENT
		
		SCAN//用户已关注时的事件推送。对应的MsyType是EVENT
	};
	
	
	/**
	 * 消息的类型
	 * @author blackboy
	 *
	 */
	public enum MsgType{
		
		TEXT,//文本
		
		IMAGE,//图片
		
		VOICE,//语音
		
		VIDEO,//视频
		
		SHORTVIDEO,//短视频
		
		LOCATION,//位置
		
		LINK,//链接
		
		EVENT,//
		
	}
	
	/**
	 * 测试账号：
	 * appID wx0c8aff4b4b779491
	 *	appsecret 441f60471237bb2fbaf31bba6be784f7
	 */
	
	/**
	 * APPID
	 */
	public static final String APPID = "wxc284282dbf28ab83";
	/**
	 * SECRET
	 */
	public static final String APPSECRET = "027162ba14a536213760a06ba24ae3fb";
	
	public static final String TOKEN = "igomall";
	
	/**
	 * 菜单文件
	 */
	public static final String MENU_PROPERTIES = "D:\\workspace\\ROOT\\src\\menu.txt";
	
	/**
	 * 生成的二维码保存路径
	 */
	public static final String IMAGE_PATH="D:\\apache-tomcat-7.0.62\\webapps\\ImageServer\\webapps\\igomall\\upload\\";
	
	public static final String logoPath = "D:\\workspace\\ROOT\\WebRoot\\upload\\image\\qrCode\\logo.png";
	
	/**
	/**
	 * ACCESS_TOKEN有效时间(单位：ms)
	 */
	public static int EFFECTIVE_TIME = 700000;
    
    /*
	 * 获取access_token
	 * http请求方式: GET
	 */
	public static final String GET_ACCESSTOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	public static final String GET_ACCESSTOKEN_URL_BY_WEB = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	
	public static final String GET_QY_ACCESSTOKEN_URL="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=APPID&corpsecret=APPSECRET";
	/*
	 * 创建菜单
	 * http请求方式：POST（请使用https协议） 
	 */
	public static final String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	/*
	 * 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
	 * form-data中媒体文件标识，有filename、filelength、content-type等信息
	 */
	/*
	 * 上传文件
	 */
	public static final String UPLOADURL="http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	public static final String UPLOADUR1="https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN";
	
	/*
	 * 下载文件
	 */
	public static final String DOWNLOADURL="http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=_media_id";
	
	/*
	 *给用户发送消息
	 */
	public static final String SENDMSG="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

	
	/*
	 * 生成带参数二维码
	 */
	public static final String CREATE_QRCODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";

	/*
	 * 获取带参数的二维码
	 */
	public static final String GET_QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
	
	
	/**
	 * 获取关注用户的列表
	 * http请求方式: GET（请使用https协议
	 * access_token		调用接口凭证
	 * next_openid		第一个拉取的OPENID，不填默认从头开始拉取
	 * 
	 */
	
	public static final String GET_ALL_USER = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";

	/**
	 * 获取用户基本信息
	 * http请求方式: GET（请使用https协议)
	 * access_token		调用接口凭证
	 * openid		普通用户的标识，对当前公众号唯一
	 * lang		返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
	 * 
	 */
	public static final String GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	/**
	 * http请求方式: GET（请使用https协议）
	 */
	public static final String GET_ALL_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";

	/*
	 * 获得jsapi_ticket
	 */
	public static final String GET_JSAPI = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	//                                    https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect
	//                                    https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect
	public static final String AUTHURL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=igomall#wechat_redirect";
}