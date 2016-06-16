
package com.igomall.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.igomall.dao.DeliveryCorpDao;
import com.igomall.entity.DeliveryCorp;
import com.igomall.service.DeliveryCorpService;

@Service("deliveryCorpServiceImpl")
public class DeliveryCorpServiceImpl extends BaseServiceImpl<DeliveryCorp, Long> implements DeliveryCorpService {

	@Resource(name = "deliveryCorpDaoImpl")
	private DeliveryCorpDao deliveryCorpDao;
	
	@Resource(name = "deliveryCorpDaoImpl")
	public void setBaseDao(DeliveryCorpDao deliveryCorpDao) {
		super.setBaseDao(deliveryCorpDao);
	}

}