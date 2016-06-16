
package com.igomall;

import org.apache.shiro.authc.UsernamePasswordToken;

public class AuthenticationToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 5898441540965086534L;

	private String captchaId;

	private String captcha;
	
	private String ip;
	
	private String hostName;

	public AuthenticationToken(String username, String password, String captchaId, String captcha, boolean rememberMe, String host,String ip) {
		super(username, password, rememberMe, host);
		this.captchaId = captchaId;
		this.captcha = captcha;
		this.ip = ip;
	}
	
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}



	public String getCaptchaId() {
		return captchaId;
	}

	public void setCaptchaId(String captchaId) {
		this.captchaId = captchaId;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

}