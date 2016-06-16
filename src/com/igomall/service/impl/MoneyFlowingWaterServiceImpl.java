
package com.igomall.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.MemberDao;
import com.igomall.dao.MoneyFlowingWaterDao;
import com.igomall.entity.Member;
import com.igomall.entity.MoneyFlowingWater;
import com.igomall.entity.MoneyFlowingWater.Type;
import com.igomall.service.MoneyFlowingWaterService;

@Service("moneyFlowingWaterServiceImpl")
public class MoneyFlowingWaterServiceImpl extends BaseServiceImpl<MoneyFlowingWater, Long> implements MoneyFlowingWaterService {

	@Resource(name = "moneyFlowingWaterDaoImpl")
	private MoneyFlowingWaterDao moneyFlowingWaterDao;
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "moneyFlowingWaterDaoImpl")
	public void setBaseDao(MoneyFlowingWaterDao moneyFlowingWaterDao) {
		super.setBaseDao(moneyFlowingWaterDao);
	}

	@Override
	public void create(Member member, BigDecimal balance, Type type,String content,HttpServletRequest request) {
		MoneyFlowingWater moneyFlowingWater = new MoneyFlowingWater();
		if(type==Type.balance1){
			moneyFlowingWater.setBeforeBalance(member.getBalance1());
			member.setBalance(member.getBalance1().add(balance));
			moneyFlowingWater.setAfterBalance(member.getBalance1());
		}else if(type==Type.recharge){//充值
			moneyFlowingWater.setBeforeBalance(member.getBalance());
			member.setBalance(member.getBalance().add(balance));
			moneyFlowingWater.setAfterBalance(member.getBalance());
			
		}else if(type==Type.withdraw){//提现
			moneyFlowingWater.setBeforeBalance(member.getBalance());
			member.setBalance(member.getBalance().add(balance));
			moneyFlowingWater.setAfterBalance(member.getBalance());
			
		}
		memberDao.merge(member);
		
		moneyFlowingWater.setIp(request.getRemoteAddr());
		moneyFlowingWater.setContent(content);
		moneyFlowingWater.setType(type);
		
		moneyFlowingWaterDao.persist(moneyFlowingWater);
		
	}

	@Override
	public Page<MoneyFlowingWater> findPage(Member member, Pageable pageable) {
		return moneyFlowingWaterDao.findPage(member,pageable);
	}

}