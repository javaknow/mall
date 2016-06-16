package com.igomall.wechat.entity;



/** 
 * 微信通用接口凭证 
 *  
 * @author zhanglei 
 */

public class AccessTokenTemp extends BaseEntity{  
	
	private static final long serialVersionUID = 4114694401977024205L;
	// 获取到的凭证  
    private String access_token;  
    // 凭证有效时间，单位：s 
    private int expires_in;  
    
    
    /*
     * 下面几个参数是采用web方式获取accesstoken返回的
     */
    private String refresh_token = "";//用户刷新access_token
    private String openid = "";//用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
    private String scope = "";//用户授权的作用域，使用逗号（,）分隔
    private String unionid = "";//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
    
    private String ticket;//用来获取jsapi_ticket用的字段

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getRefresh_token() {
		return refresh_token;
	}



	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}



	public String getOpenid() {
		return openid;
	}



	public void setOpenid(String openid) {
		this.openid = openid;
	}



	public String getScope() {
		return scope;
	}



	public void setScope(String scope) {
		this.scope = scope;
	}



	public String getUnionid() {
		return unionid;
	}



	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}



	public String getAccess_token() {
		return access_token;
	}



	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}



	public int getExpires_in() {
		return expires_in;
	}



	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	@Override
	public String toString() {
		return "AccessTokenTemp [access_token=" + access_token
				+ ", expires_in=" + expires_in + ", refresh_token="
				+ refresh_token + ", openid=" + openid + ", scope=" + scope
				+ ", unionid=" + unionid + ", ticket=" + ticket + "]";
	}
	
	
	
}  