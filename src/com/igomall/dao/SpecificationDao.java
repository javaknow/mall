
package com.igomall.dao;

import com.igomall.entity.Specification;

public interface SpecificationDao extends BaseDao<Specification, Long> {

	Specification findByTaoBaoId(Long taobaoId);

	Specification createSpecificationTaoBaoId(Long taobaoId);

}