
package com.igomall.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.igomall.dao.SpecificationValueDao;
import com.igomall.entity.Specification;
import com.igomall.entity.SpecificationValue;
import com.igomall.service.SpecificationValueService;

@Service("specificationValueServiceImpl")
public class SpecificationValueServiceImpl extends BaseServiceImpl<SpecificationValue, Long> implements SpecificationValueService {

	 @Resource(name = "specificationValueDaoImpl")
	 private SpecificationValueDao specificationValueDao;
	
	@Resource(name = "specificationValueDaoImpl")
	public void setBaseDao(SpecificationValueDao specificationValueDao) {
		super.setBaseDao(specificationValueDao);
	}

	@Override
	public SpecificationValue findByTaoBaoId(Specification specification,Long taobaoId) {
		return specificationValueDao.findByTaoBaoId(specification,taobaoId);
	}

}