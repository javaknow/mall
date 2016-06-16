package com.igomall.wechat.entity;


/** 
 * 微信通用接口凭证 
 *  
 * @author zhanglei 
 */

public class Ticket{  
	public String ticket;//获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
	
	public String expire_seconds;//二维码的有效时间，以秒为单位。最大不超过1800。
	
	public String url;//维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
	
	public String errcode="0";// 错误码
	
	public String errmsg="0";//  错误信息

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getExpire_seconds() {
		return expire_seconds;
	}

	public void setExpire_seconds(String expire_seconds) {
		this.expire_seconds = expire_seconds;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	@Override
	public String toString() {
		return "Ticket [ticket=" + ticket + ", expire_seconds="
				+ expire_seconds + ", url=" + url + ", errcode=" + errcode
				+ ", errmsg=" + errmsg + "]";
	}
	
	
}  