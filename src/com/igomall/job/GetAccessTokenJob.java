
package com.igomall.job;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.igomall.service.MailService;
import com.igomall.wechat.CommonWeChatAttributes;
import com.igomall.wechat.util.GetAccessTokenUtil;


@Component("getAccessTokenJob")
@Lazy(false)
public class GetAccessTokenJob {
	@Resource(name = "mailServiceImpl")
	private MailService mailService;
	@Scheduled(cron = "${job.getAccessToken.cron}")
	public void getAccessToken() {
		System.out.println("获取accessToken:"+new Date());
		GetAccessTokenUtil.getAccessToken(CommonWeChatAttributes.APPID, CommonWeChatAttributes.APPSECRET);
	}

}