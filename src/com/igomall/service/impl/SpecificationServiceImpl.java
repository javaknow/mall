
package com.igomall.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.igomall.dao.SpecificationDao;
import com.igomall.entity.Specification;
import com.igomall.service.SpecificationService;

@Service("specificationServiceImpl")
public class SpecificationServiceImpl extends BaseServiceImpl<Specification, Long> implements SpecificationService {

	 @Resource(name = "specificationDaoImpl")
	 private SpecificationDao specificationDao;
	
	@Resource(name = "specificationDaoImpl")
	public void setBaseDao(SpecificationDao specificationDao) {
		super.setBaseDao(specificationDao);
	}

	@Override
	public Specification findByTaoBaoId(Long taobaoId) {
		return specificationDao.findByTaoBaoId(taobaoId);
	}

	@Override
	public Specification createSpecificationTaoBaoId(Long taobaoId) {
		return specificationDao.createSpecificationTaoBaoId(taobaoId);
	}

}