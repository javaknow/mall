
package com.igomall.job;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.igomall.service.BonusCouponService;

@Component("bonusCouponJob")
@Lazy(false)
public class BonusCouponJob {

	@Resource(name = "bonusCouponServiceImpl")
	private BonusCouponService bonusCouponService;

	@Scheduled(cron = "${job.bonusCoupon.cron}")
	public void deveryBonusCoupon() {
		bonusCouponService.deveryBonusCoupns();
	}

}