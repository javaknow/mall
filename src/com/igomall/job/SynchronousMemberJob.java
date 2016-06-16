
package com.igomall.job;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.igomall.service.MemberService;

/**
 * 定时同步会员的信息
 * @author xiali
 *
 */
@Component("synchronousMemberJob")
@Lazy(false)
public class SynchronousMemberJob {
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	//
	@Scheduled(cron = "${job.synchronousMemberInfo.cron}")
	public void synchronousMemberInfo() {
		//memberService.synchronousMemberInfo();
	}

}